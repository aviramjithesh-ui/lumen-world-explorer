package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;
import java.util.TreeMap;

public class Room {

    private int x;
    private int y;
    private int width;
    private int length;
    private Random rand;
    private TETile[][] world;

    public Room(TETile[][] world, Random rand) {
        this.world = world;
        this.rand = rand;
        // Bottom left coordinate of our room
        this.x = rand.nextInt(1, World.MAX_COORD_WIDTH - 1);
        this.y = rand.nextInt(1, World.MAX_COORD_LENGTH - 1);
        this.width = rand.nextInt(World.MIN_ROOM_WIDTH, World.MAX_ROOM_WIDTH);
        this.length = rand.nextInt(World.MIN_ROOM_LENGTH, World.MAX_ROOM_LENGTH);

        generateRoomAttempt(world, x, y, width, length);
    }

    private void box(TETile[][] tiles, int startX, int startY, int w, int l) {
        for (int x = startX; x < startX + w; x += 1) {
            for (int y = startY; y < startY + l; y += 1) {
                tiles[x][y] = Tileset.FLOOR;
            }
        }
    }

    private boolean checkEmpty(TETile[][] tiles, int startX, int startY, int w, int l) {
        int testXcoord = startX + w + World.BUFFER;
        int testYcoord = startY + l + World.BUFFER;
        testXcoord = Math.min(World.MAX_COORD_WIDTH, testXcoord);
        testYcoord = Math.min(World.MAX_COORD_LENGTH, testYcoord);

        for (int x = startX; x < testXcoord; x += 1) {
            for (int y = startY; y < testYcoord; y += 1) {
                if (tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }


    private void generateRoomAttempt(TETile[][] tiles, int startX, int startY, int w, int l) {
        if (checkEmpty(tiles, startX, startY, w, l)) {
            box(tiles, startX, startY, w, l);
            return;
        }

        startX = rand.nextInt(1, World.MAX_COORD_WIDTH - 1);
        startY = rand.nextInt(1, World.MAX_COORD_LENGTH - 1);
        this.x = startX;
        this.y = startY;
        generateRoomAttempt(tiles, startX, startY, w, l);
    }


    public int getCenterX() {
        return x + width / 2; // int shoudl always return whole number
    }

    public int getCenterY() {
        return y + length / 2;
    }

    public int getRightBound() {
        return x + width;
    }

    public int getLeftBound() {
        return x;
    }


    public int getUpperBound() {
        return y + length;
    }

    public int getLowerBound() {
        return y;
    }

    public boolean coordInRoom(int x, int y) {
        if (x >= getLeftBound() && x < getRightBound() && y >= getLowerBound() && y < getUpperBound()) {
            return true;
        }
        return false;
    }

}
