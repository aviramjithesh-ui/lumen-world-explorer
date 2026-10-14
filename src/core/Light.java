package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class Light {
    private int x;
    private int y;
    private int radius;
    private Random rand;
    private TETile[][] world;
    private Room room;
    private boolean on;

    public Light (int x, int y, Room room, Random rand, TETile[][] world) {
        this.x = x;
        this.y = y;
        this.room = room;
        this.rand = rand;
        this.on = false;
        this.world = world;

    }

    public void toggleLight() {
        on = !on;
    }

    public void setLight(boolean onSet) {
        this.on = onSet;
    }

    public boolean lightStatus() {
        return on;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Room getRoom() {
        return this.room;
    }

}
