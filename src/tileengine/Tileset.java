package tileengine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you", 0);
    public static final TETile WALL = new TETile('#', new Color(131, 119, 119), new Color(131, 119, 119),
            "wall", 1);
    public static final TETile FLOOR = new TETile('.', new Color(131, 119, 119), Color.black, "floor", 2);
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", 3);
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass", 4);
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water", 5);
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower", 6);
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", 7);
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door", 8);
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand", 9);
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain", 10);
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree", 11);
    public static final TETile LIGHT_ON = new TETile('❀', new Color(90, 35, 165), new Color(190, 120, 255), "light", 6);
    public static final TETile LIGHT_OFF = new TETile('❀', new Color(90, 35, 165), Color.black, "light", 6);
    public static final TETile CELL = new TETile('█', Color.white, Color.black, "cell", 12);
    public static final TETile LIGHT_FLOOR_1 = new TETile('·', Color.WHITE, new Color(190, 120, 255), "bright floor", 20);
    public static final TETile LIGHT_FLOOR_2 = new TETile('·', Color.WHITE, new Color(140, 70, 220), "medium bright floor", 21);
    public static final TETile LIGHT_FLOOR_3 = new TETile('·', Color.WHITE, new Color(90, 35, 165), "dim floor", 22);
    public static final TETile LIGHT_FLOOR_4 = new TETile('·', Color.WHITE, new Color(45, 15, 90), "very dim floor", 23);
    public static final TETile LIGHT_FLOOR_5 = new TETile('·', Color.WHITE, new Color(35, 8, 73), "very dim floor", 23);
    public static final TETile LIGHT_FLOOR_6 = new TETile('·', Color.WHITE, new Color(26, 4, 60), "very dim floor", 23);


}


