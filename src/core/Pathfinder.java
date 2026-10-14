package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Pathfinder {
    private LinkedList<TilePoint>[] adjLists;
    private int vertexCount;

    private TETile[][] worldTiles;


    public Pathfinder(TETile[][] tiles) {
        this.worldTiles = tiles;

        // Graph setup
        int numVertices = worldTiles.length;
        adjLists = (LinkedList<TilePoint>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<TilePoint>();
        }
        vertexCount = numVertices;
    }


    /* Returns a list of all the vertices u such that the Edge (V, u)
   exists in the graph. */
    public List<TilePoint> neighbors(TilePoint currentPoint) {
        ArrayList<TilePoint> results = new ArrayList<>();

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int newX = currentPoint.x + dir[0]; // add the direction of x
            int newY = currentPoint.y + dir[1]; // add direction of y

            // check if world boundraries
            if (newX >= 0 && newX < worldTiles.length && newY >= 0 && newY < worldTiles[0].length) {
                if (World.isWalkableFloor(worldTiles[newX][newY])) {
                    results.add(new TilePoint(newX, newY));
                }
            }
        }

        return results;
    }

    /* Returns a list of the vertices that lie on the shortest path from start to stop.
    If no such path exists, you should return an empty list. If START == STOP, returns a List with START. */
    public ArrayList<TilePoint> shortestPath(TilePoint start, TilePoint stop) {
        if (start.equals(stop)) {
            ArrayList<TilePoint> list = new ArrayList<TilePoint>();
            list.add(start);
            return list;
        }

        // Setup
        Queue<TilePoint> queue = new LinkedList<TilePoint>();
        HashSet<TilePoint> visited = new HashSet<>();
        HashMap<TilePoint, TilePoint> edgeTo = new HashMap<>();

        edgeTo.put(start, null);
        queue.add(start);

        while (!queue.isEmpty()) {
            TilePoint current = queue.poll();
            // We want our path when we visit to be able to still traverse through
//            if (visited.contains(current)) {
//                continue; // No cycling ALLOWED!
//            } else {
//                visited.add(current);
//            }

            // Break early if we end up getting to the stop node on first itteration
            if (current.equals(stop)) {
                break;
            }

            for (TilePoint point : neighbors(current)) {
                if (!visited.contains(point)) {
                    visited.add(point);
                    edgeTo.put(point, current);
                    queue.add(point);
                }
            }
        }

        // Trace back to make path
        if (visited.contains(stop)) {
            ArrayList<TilePoint> returnList = new ArrayList<>();
            TilePoint trackerBack = stop;
            while (trackerBack != start) {
                returnList.add(trackerBack);
                trackerBack = edgeTo.get(trackerBack);
            }
            returnList.add(start);

            Collections.reverse(returnList);
            return returnList;
        } else {
            return new ArrayList<>();
        }
    }

    public static class TilePoint {
        public final int x;
        public final int y;

        public TilePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            TilePoint tilePoint = (TilePoint) obj;
            return (x == tilePoint.x && y == tilePoint.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x,y); // Have a randomizer hash just do the hashing for our object
        }

    }

}
