package core;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.Tileset;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class Main {
    private final int mainMenuWidth;
    private final int mainMenuHeight;

    private static World world;

    private static int avatarCoordX;
    private static int avatarCoordY;

    private static int extraFilesMade = 0;
    private static Main menu;

    // creates "Main" method object, checks if newWorld should be created,
    // creates new world from user inputted seed
    public static void main(String[] args) {
        extraFilesMade = countSaveSFiles();
        menu = new Main(50, 50);

        char letter = menu.menuChoice();
        if (letter == 'n' || letter == 'N') {
            long seed = menu.charsInput();
            if (seed > 0) {
                int parsedSeed = (int) (seed);
                world = new World(parsedSeed);
            }
            worldLoop(world, 0,0);
        } else if (letter == 'q' || letter == 'Q') {
            System.exit(0);
        } else if (letter == 'l' || letter == 'L') {
            loadFile();
            worldLoop(world, avatarCoordX, avatarCoordY);
        } else if (letter == 'k' || letter == 'K') {
            int worldChoiceNum = menu.charsInputLoadWorld();
            loadSpecificFile(worldChoiceNum);
            worldLoop(world, avatarCoordX, avatarCoordY);
        } else if (letter == ':') {
            // Will leave this empty cuz Im not sure what world to save
        }
    }

    // based this method off the MemoryGame method in Lab 18, also includes the main screen text
    public Main(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.mainMenuWidth = width;
        this.mainMenuHeight = height;
        StdDraw.setCanvasSize(this.mainMenuWidth * 16, this.mainMenuHeight * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.mainMenuWidth);
        StdDraw.setYscale(0, this.mainMenuHeight);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 10, "CS61B: BYOW");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 20, "(N) New Game");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 25, "(L) Load Game");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 30, "(Q) Quit Game");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 35, "(k) Load from Index Game");

        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }

    //this is mainly just for displaying the seed when it's typed into the frame
    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 10, "CS61B: BYOW");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 20, "Enter seed followed by S");
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight / 2, s);
        StdDraw.show();
    }

    // this checks what the character input is for the seed and saves it to the seed value
    // if s or S is typed, stops saving the seed value and just returns the long to the Main.main method
    public long charsInput() {
        StringBuilder seed = new StringBuilder();
        drawFrame("");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                if (letter == 's' || letter == 'S') {
                    break;
                }
                seed.append(letter);
                drawFrame(String.valueOf(seed));
            }
        }
        return Long.parseLong(seed.toString());
    }

    // checks what the initial choice of the user is, whether it's N, L, or Q, and returns
    // the char that was pressed by the user
    // Formerly used to be newWrodl returning the boolean
    public char menuChoice() {
        StringBuilder choice = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                return letter;
            }
        }
    }


    public void drawFrameLoadWorld(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 10, "CS61B: BYOW");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 20, "Enter saved world number followed by S");
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight - 30, "You have " + countSaveSFiles() + " slots saved");

        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(this.mainMenuWidth / 2, this.mainMenuHeight / 2, s);
        StdDraw.show();
    }

    // this checks what the character input is for the seed and saves it to the seed value
    // if s or S is typed, stops saving the seed value and just returns the long to the Main.main method
    public int charsInputLoadWorld() {
        StringBuilder optionChosen = new StringBuilder();
        drawFrameLoadWorld("");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                if (letter == 's' || letter == 'S' || letter == '\n') {
                    break;
                }

                if (Character.isDigit(letter)) {
                    optionChosen.append(letter);
                    drawFrameLoadWorld(String.valueOf(optionChosen));
                }
            }
        }
        try {
            return Integer.parseInt(optionChosen.toString());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input or number too large. Defaulting to 0.");
            return 0; // Provide a safe fallback value to prevent the crash
        }
    }

    public static void worldLoop(World world, int x, int y) {
        if (x != 0 && y != 0) {
            avatarCoordX = x;
            avatarCoordY = y;
            world.getTiles()[avatarCoordX][avatarCoordY] = Tileset.AVATAR;
        } else {
            List<Integer> startCoords = world.determineStartingPosition(world);
            avatarCoordX = startCoords.get(0);
            avatarCoordY = startCoords.get(1);
            world.getTiles()[avatarCoordX][avatarCoordY] = Tileset.AVATAR;
        }
        world.getTiles()[avatarCoordX][avatarCoordY] = Tileset.AVATAR;
        int previousMouseX = -1;
        int previousMouseY = -1;

        refreshWindow(1, 1);
        boolean colonPressed = false;
        boolean mousePressed = false;
        boolean readyToStartPath = false;

        List<Pathfinder.TilePoint> activePath = new ArrayList<>();
        long lastStepTime = System.currentTimeMillis();
        final int STEP_DELAY_MS = 150; // How fast the avatar walks lower is faster

        Pathfinder.TilePoint startPoint = null;
        char c; // Variable for saving the most recent character typed by the user.

        // This outer infinite-loop allows the game to continue indefinitely, until the user quits.
        while (true) {
            // State variables
            boolean stateChanged = false;

            // WASD HANDLING
            // hasNextKeyTyped checks if the user has typed a key that we haven't processed.
            // This loop runs until all unprocessed keys are processed.
            // If there are no unprocessed keys, we go back to the outer infinite loop to wait for the next key.
            while (StdDraw.hasNextKeyTyped()) {
                // nextKeyTyped returns the next key to process.
                // Always check hasNextKeyTyped before calling nextKeyTyped.
                // If you call nextKeyTyped and there's no key to process, code will crash!
                c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);


                int targetX = avatarCoordX;
                int targetY = avatarCoordY;

                // Switch statements can be useful to replace long if-else statements!
                switch (c) {
                    case 'w':
                        targetY = avatarCoordY + 1;
                        break;
                    case 's':
                        targetY = avatarCoordY - 1;
                        break;
                    case 'a':
                        targetX = avatarCoordX - 1;
                        break;
                    case 'd':
                        targetX = avatarCoordX + 1;
                        break;
                    case ':':
                        colonPressed = true;
                        break;
                    case 'l':
                        toggleNearestLight();
                        break;
                    case 'q':
                        if (colonPressed) {
                            saveFile(world);
                            System.exit(0);
                        }
                        break;
                    case 'k':
                        if (colonPressed) {
                            int slot = 0;
                            try {
                                slot = Integer.parseInt(String.valueOf(menu.menuChoice()));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input or number too large. Defaulting to 0.");
                                slot = 0; // Provide a safe fallback value to prevent the crash
                            }
                            saveSpecificFile(world, slot);
                            System.exit(0);
                        }
                        break;
                    default:
                        colonPressed = false;
                        break;
                }

                if (world.isWalkableFloor(world.getTiles()[targetX][targetY])) {
                    world.getTiles()[avatarCoordX][avatarCoordY] = Tileset.FLOOR;
                    world.toggle(targetX, targetY);

                    avatarCoordX = targetX;
                    avatarCoordY = targetY;

                    // Cancel path following
                    if (!activePath.isEmpty()) {
                        readyToStartPath = false;
                        for (Pathfinder.TilePoint point : activePath) {
                            world.getTiles()[point.x][point.y] = Tileset.FLOOR;
                        }
                        activePath.clear();
                        world.refreshLights();

                    }

                    startPoint = null;
                    world.refreshLights();
                    stateChanged = true;

                }

            }

            // MOUSE HANDLING
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            boolean isMouseFramePressed = StdDraw.isMousePressed();

            // Only trigger when we release
            if (!isMouseFramePressed && mousePressed) {
                // Mouse Hovering
                // Set pathfinding only when we are in window frame
                if (mouseX >= 0 && mouseX < world.getTiles().length && mouseY >= 0 && mouseY < world.getTiles()[0].length) {
                    if (startPoint == null) {
                        startPoint = new Pathfinder.TilePoint(avatarCoordX, avatarCoordY);

                        Pathfinder.TilePoint stopPoint = new Pathfinder.TilePoint(mouseX, mouseY);

                        activePath = world.getPathfinder().shortestPath(startPoint, stopPoint);
                        if (!activePath.isEmpty()) {
                            activePath.remove(0);
                        }

                        for (Pathfinder.TilePoint point : activePath) {
                            world.getTiles()[point.x][point.y] = Tileset.CELL;
                        }

                    } else {
                        readyToStartPath = true;
                    }

                    stateChanged = true;
                }
            }
            mousePressed = isMouseFramePressed;


            // Path follwoing
            if (!activePath.isEmpty() && readyToStartPath) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - lastStepTime) > STEP_DELAY_MS) {
                    Pathfinder.TilePoint nextStep = activePath.remove(0);

                    world.getTiles()[avatarCoordX][avatarCoordY] = Tileset.FLOOR; // Cleanup old tiles
                    world.getTiles()[nextStep.x][nextStep.y] = Tileset.AVATAR;

                    avatarCoordX = nextStep.x;
                    avatarCoordY = nextStep.y;
                    world.refreshLights();
                    lastStepTime = currentTime;
                    stateChanged = true;
                }
            } else if (activePath.isEmpty()) {
                readyToStartPath = false;
                startPoint = null;
            }

            boolean mouseChanged = mouseX != previousMouseX || mouseY != previousMouseY;
            if (stateChanged || mouseChanged) {
                // Draws the world to the screen.
                // This is in the while(true) loop, because we want to frequently re-render the world.
                refreshWindow(mouseX, mouseY);
                previousMouseX = mouseX;
                previousMouseY = mouseY;
            }

            StdDraw.pause(10);

        }
    }

    public static void refreshWindow(int mouseX, int mouseY) {
        StdDraw.clear(new Color(0, 0, 0));
        world.getTer().drawTiles(world.getTiles());
        cursorTrack(mouseX, mouseY);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textRight((double) (int) World.WINDOW_SIZE_X - 5, World.WINDOW_SIZE_Y - 3, "Press (L) to turn on the nearest light!");
        StdDraw.show();
    }


    public static void saveFile(World world) {
        String filename = "save0.txt";

        String seed = String.valueOf(world.seed);
        String avatarX = String.valueOf(avatarCoordX);
        String avatarY = String.valueOf(avatarCoordY);
        ArrayList<Integer> lightOnArray = world.getLightOnArray();


        // Step 3: Save the new name to the file
        Out out = new Out(filename);
        out.println(seed);
        out.println(avatarX);
        out.println(avatarY);
        out.println(lightOnArray);
        out.close();
    }

    public static void loadFile() {
        String filename = "save0.txt";
        File file = new File(filename);

        if (file.exists()) {
            In in = new In(file);
            String[] sepFile = in.readAllLines();
            String seed = sepFile[0];
            String x = sepFile[1];
            String y = sepFile[2];
            String lightArray = sepFile[3];
            int[] numbers = Stream.of(lightArray.replaceAll("[\\[\\]]", "").split(",\\s*"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            avatarCoordX = Integer.parseInt(x);
            avatarCoordY = Integer.parseInt(y);
            int parsedSeed = Integer.parseInt(seed);
            world = new World(parsedSeed);
            world.setLightsBasedOnSave(numbers);
            world.refreshLights();

        } else {
            System.out.println("Reverting to basic seed: We did not find a tile");
            world = new World(2924242); // Revert to basic seed
        }
    }

    public static void saveSpecificFile(World world, int select) {
        String filename = "save" + select + ".txt";

        String seed = String.valueOf(world.seed);
        String avatarX = String.valueOf(avatarCoordX);
        String avatarY = String.valueOf(avatarCoordY);
        ArrayList<Integer> lightOnArray = world.getLightOnArray();


        // Step 3: Save the new name to the file
        Out out = new Out(filename);
        out.println(seed);
        out.println(avatarX);
        out.println(avatarY);
        out.println(lightOnArray);
        out.close();
    }

    public static void loadSpecificFile(int selectedCount) {
        String filename = "save" + selectedCount + ".txt";
        File file = new File(filename);

        if (file.exists()) {
            In in = new In(file);
            String[] sepFile = in.readAllLines();
            String seed = sepFile[0];
            String x = sepFile[1];
            String y = sepFile[2];
            String lightArray = sepFile[3];
            int[] numbers = Stream.of(lightArray.replaceAll("[\\[\\]]", "").split(",\\s*"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            avatarCoordX = Integer.parseInt(x);
            avatarCoordY = Integer.parseInt(y);
            int parsedSeed = Integer.parseInt(seed);
            world = new World(parsedSeed);
            world.setLightsBasedOnSave(numbers);
            world.refreshLights();
        } else {
            System.out.println("Reverting to basic seed: We did not find a file");
            world = new World(2924242); // Revert to basic seed
        }
    }

    private static int countSaveSFiles() {
        int count = 0; // setup return count variable
        File currentDir = new File("."); // just use our big directory
        File[] files = currentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.getName().matches("save\\d+\\.txt")) {
                        count++; // return our counter
                    }
                }
            }
        }
        return count;
    }
    
    public static void cursorTrack(int mouseX, int mouseY) {
        String printValue = world.getItemAtTile(mouseX, mouseY);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((double) (int) World.WINDOW_SIZE_X / 2, World.WINDOW_SIZE_Y - 3, printValue);
    }

    //use avatar coords to find the room that contains that coord then
    // go through the lights in the world then choose the light that is within those
    // room bounds then turn the light on
    public static void toggleNearestLight() {
        Light light = world.findLightInRoom(avatarCoordX, avatarCoordY);
        if (light == null) {
            return;
        }
        light.toggleLight();
        if (light.lightStatus()) {
            world.turnOnLight(light);
        } else if (!light.lightStatus()) {
            world.turnOffLight(light);
        }

        refreshWindow(1, 1);
    }


}
