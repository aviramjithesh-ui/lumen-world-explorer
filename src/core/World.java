package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class World {

    public static int WINDOW_SIZE_X = 80;
    public static int WINDOW_SIZE_Y = 40;


    public static int HUD_SPACE_LENGTH = 3;
    public static int MAX_ROOM_WIDTH = 10;
    public static int MAX_ROOM_LENGTH = 10;
    public static int MIN_ROOM_WIDTH = 3;
    public static int MIN_ROOM_LENGTH = 3;
    public static int BUFFER = 5;
    public static int MAX_COORD_WIDTH = WINDOW_SIZE_X - MAX_ROOM_WIDTH;
    public static int MAX_COORD_LENGTH = WINDOW_SIZE_Y - MAX_ROOM_LENGTH - HUD_SPACE_LENGTH;

    public long seed = 3668721473601085579L;//9157198120406614623L; // 1366435223448741313L //5840930912313687631 // 5013995466868426345 // 3668721473601085579
    private Random rand = new Random(seed);

    private TETile[][] worldTiles;
    private ArrayList<Room> rooms = new ArrayList<>();
    private TERenderer ter;
    private Pathfinder pathfinder;
    private static ArrayList<Light> lights = new ArrayList<>();

    public World(long seed) {
        this.ter = new TERenderer();
        this.seed = seed;
        this.rand = new Random(seed);
        // Initializes the window
        ter.initialize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
        StdDraw.enableDoubleBuffering();


        // Intialize tiles
        this.worldTiles = new TETile[WINDOW_SIZE_X][WINDOW_SIZE_Y];
        for (int x = 0; x < WINDOW_SIZE_X; x += 1) {
            for (int y = 0; y < WINDOW_SIZE_Y; y += 1) {
                worldTiles[x][y] = Tileset.NOTHING;
            }
        }

        this.pathfinder = new Pathfinder(worldTiles);


        // Initialize room layout
        int count = rand.nextInt(15, 16);
        //generateHallways(world, rooms.get(0), rooms.get(10));

        // Setup Rooms
        for (int i = 0; i <= count; i++) {
            rooms.add(new Room(worldTiles, rand));

        }


        PriorityQueue<RoomComparable> priorityQueue = new PriorityQueue<>();
        // Extract counts and add to queue
        for (Room room : rooms) {
            priorityQueue.add(new RoomComparable(room, room.getCenterX()));
        }

        List<Room> topRooms = new ArrayList<>();

        // add sorted priority queue to list
        while (!priorityQueue.isEmpty()) {
            topRooms.add(priorityQueue.poll().room);
        }

        // Sort by popularity
        for (int i = 0; i < rooms.size() - 1; i++) {
            generateHallways(worldTiles, topRooms.get(i), topRooms.get(i + 1));
        }

        // generate Walls
        generateWalls(worldTiles);

        generateLightsRandomly();

        ter.renderFrame(worldTiles);
    }

    private class RoomComparable implements Comparable<RoomComparable> {
        Room room;
        double distance;

        public RoomComparable(Room word, double distance) {
            this.room = word;
            this.distance = distance;
        }

        @Override
        public int compareTo(RoomComparable other) {
            return Double.compare(other.distance, this.distance);
        }
    }



    private void generateHallways(TETile[][] worldTiles, Room room, Room room2) {
        int x1 = room.getCenterX();
        int y1 = room.getCenterY();
        int x2 = room2.getCenterX();
        int y2 = room2.getCenterY();

        //make it so that you are always starting from the smaller coord to get to the larger coord
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y += 1) {
                worldTiles[x2][y] = Tileset.FLOOR;
            }
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x += 1) {
                worldTiles[x][y1] = Tileset.FLOOR;
            }
    }



    // X X X
    // X x X
    // X X X
    private void generateWalls(TETile[][] worldTiles) {
        for (int x = 0; x < WINDOW_SIZE_X; x += 1) {
            for (int y = 0; y < WINDOW_SIZE_Y; y += 1) {
                if (worldTiles[x][y] == Tileset.NOTHING) {
                    if (has3x3FloorNeighbor(worldTiles, x, y)) {

                        worldTiles[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private boolean has3x3FloorNeighbor(TETile[][] worldTiles , int startx, int starty) {
        for (int x = startx - 1; x <= startx + 1; x += 1) {
            for (int y = starty -1; y <= starty + 1; y += 1) {

                if (x < 0 || y < 0 || x > WINDOW_SIZE_X -1 || y > WINDOW_SIZE_Y -1 ) {
                    continue;
                }

                // should check
                if (worldTiles[x][y] == Tileset.FLOOR) {

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper function.
     * If the tile at position (x,y) is FLOOR, change it to WALL.
     * If the tile at position (x,y) is WALL, change it to FLOOR.
     */
    public boolean toggle(int x, int y) {
        if (isWalkableFloor(worldTiles[x][y])) {
            worldTiles[x][y] = Tileset.AVATAR;
            return true;
        } else {
            return false;
        }
    }

    public List<Integer> determineStartingPosition(World world) {
        int testX = rand.nextInt(1, World.MAX_COORD_WIDTH - 1);
        int testY = rand.nextInt(1, World.MAX_COORD_LENGTH - 1);

        if (worldTiles[testX][testY] == (Tileset.FLOOR)) {
            return List.of(testX, testY);
        } else {
            return determineStartingPosition(world);
        }
    }


    public TERenderer getTer() {
        return ter;
    }


    public TETile[][] getTiles() {
        return worldTiles;
    }

    public Pathfinder getPathfinder() { return pathfinder;}

    public String getItemAtTile(double x, double y) {
        if (x >= 80 || y >= 40) {
            return "The Void";
        }
        TETile item = worldTiles[(int) x][(int) y];
        if (item == Tileset.AVATAR) {
            return "Avatar";
        } else if (item == Tileset.WALL) {
            return "Wall";
        } else  if (item == Tileset.FLOOR) {
            return "Floor";
        } else if (item == Tileset.CELL) {
            return "Path";
        } else if (item == Tileset.LIGHT_ON || item == Tileset.LIGHT_FLOOR_1 || item == Tileset.LIGHT_FLOOR_2
                || item == Tileset.LIGHT_FLOOR_3 || item == Tileset.LIGHT_FLOOR_4) {
            return "Lit Up Floor";
        }
        return "The Void";
    }

    public void generateLightsRandomly() {
        for (Room room : rooms) {
            int lightRightBound = room.getRightBound() - 1;
            int lightLeftBound = room.getLeftBound() + 1;
            int lightUpperBound = room.getUpperBound() - 1;
            int lightLowerBound = room.getLowerBound() + 1;
            Light roomLight = new Light(rand.nextInt(lightLeftBound, lightRightBound), rand.nextInt(lightLowerBound, lightUpperBound), room, rand, worldTiles);
            worldTiles[roomLight.getX()][roomLight.getY()] = Tileset.LIGHT_OFF;
            lights.add(roomLight);
        }
    }

    public Light findLightInRoom(int x, int y) {
        for (Light light : lights) {
            if (light.getRoom().coordInRoom(x, y)) {
                return light;
            }
        }
        return lights.get(0);
    }
    // @source From https://stackoverflow.com/questions/11160952/goto-next-iteration-in-for-loop-in-java
    // We needed to skip a specific tile in the case that it was not a floor tile
    // but still needed to run the subsequent iterations of y, so instead of break we used "continue"
    // which would allow us to skip just this specific tile.
    public void turnOnLight(Light light) {
        Room room = light.getRoom();
        int roomLeft = room.getLeftBound();
        int roomRight = room.getRightBound();
        int roomLower = room.getLowerBound();
        int roomUpper = room.getUpperBound();
        int lightX = light.getX();
        int lightY = light.getY();
        for (int x = roomLeft; x < roomRight; x++) {
            for (int y = roomLower; y < roomUpper; y++) {
                if (worldTiles[x][y] != Tileset.FLOOR) {
                    continue;
                }
                int distance = (int) Math.sqrt(Math.pow(x - lightX, 2) + Math.pow(y - lightY, 2));
                if (distance <= 1) {
                    worldTiles[x][y] = Tileset.LIGHT_FLOOR_1;
                } else if (distance <= 2) {
                    worldTiles[x][y] = Tileset.LIGHT_FLOOR_2;
                } else if (distance <= 3) {
                    worldTiles[x][y] = Tileset.LIGHT_FLOOR_3;
                } else if (distance <= 4) {
                    worldTiles[x][y] = Tileset.LIGHT_FLOOR_4;
                } else if (distance <= 5) {
                    worldTiles[x][y] = Tileset.LIGHT_FLOOR_5;
                } else if (distance <= 6) {
                    worldTiles[x][y] = Tileset.LIGHT_FLOOR_6;
                }
            }
        }
        worldTiles[lightX][lightY] = Tileset.LIGHT_ON;
    }

    public void turnOffLight(Light light) {
        Room room = light.getRoom();
        int roomLeft = room.getLeftBound();
        int roomRight = room.getRightBound();
        int roomLower = room.getLowerBound();
        int roomUpper = room.getUpperBound();
        for (int x = roomLeft; x < roomRight; x++) {
            for (int y = roomLower; y < roomUpper; y++) {
                if (isWalkableFloor(worldTiles[x][y])) {
                    worldTiles[x][y] = Tileset.FLOOR;
                }
            }
        }
        worldTiles[light.getX()][light.getY()] = Tileset.LIGHT_OFF;
    }

    public static boolean isWalkableFloor(TETile tile) {
        return tile == Tileset.FLOOR || tile == Tileset.LIGHT_FLOOR_1 || tile == Tileset.LIGHT_FLOOR_2
                || tile == Tileset.LIGHT_FLOOR_3 || tile == Tileset.LIGHT_FLOOR_4 || tile == Tileset.LIGHT_FLOOR_5
                || tile == Tileset.LIGHT_FLOOR_6 || tile == Tileset.CELL || tile == Tileset.AVATAR;
    }

    public boolean isWalkableFloor(int x, int y) {
        return isWalkableFloor(worldTiles[x][y]);
    }
    public void refreshLights() {
        for (Light light : lights) {
            if (light.lightStatus()) {
                turnOnLight(light);
            }
        }
    }

    public ArrayList<Integer> getLightOnArray() {
        ArrayList<Integer> lightOn = new ArrayList<>();
        for (Light light: lights) {
            if (light.lightStatus()) {
                lightOn.add(1);
            } else {
                lightOn.add(0);
            }
        }
        return lightOn;
    }

    public void setLightsBasedOnSave(int[] boolList) {
        boolean intToBool;
        for (int i = 0; i < boolList.length; i++) {
            if (boolList[i] == 1) {
                intToBool = true;
            } else {
                intToBool = false;
            }
            lights.get(i).setLight(intToBool);
        }
    }

}
