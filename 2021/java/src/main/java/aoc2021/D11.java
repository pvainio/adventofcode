package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 11 part 1 and 2
 */
public class D11 {

    record Point(int x, int y) {};

    public static void main(String ... args) throws IOException {

        var map = Files.lines(Path.of("../input/11a.txt"))
            .map(row -> row.chars().map(i -> i - '0').toArray())
            .toArray(int[][]::new);
        
        var countFlashes = IntStream.range(0, 100)
            .mapToLong(i -> step(map)).sum();

        System.out.printf("part 1: %d\n", countFlashes);

        int step = 100;
        while(!allFlashes(map)) {
            step(map);
            step++;
        }

        System.out.printf("part 2: %d\n", step);
    }

    /** Return true if all points in the map flashed */
    static boolean allFlashes(int[][] map) {
        return !allPoints(map).anyMatch(p -> map[p.y][p.x] != 0);
    }

    /** Run one step and return flashes. */
    static long step(int[][] map) {
        allPoints(map).forEach(p -> map[p.y][p.x]++); // increase energy by one
        return allPoints(map).mapToLong(p -> flash(map, p)).sum(); // flash 
    }

    /** Flash given point and adjacent points if enough energy.
     * @return return count of flashes triggered
     */
    static long flash(int[][] map, Point p) {
        if (map[p.y][p.x] < 10) { // only flash 10 or larger
            return 0;
        }
        map[p.y][p.x] = 0; // reset to 0 after flashing
        return 1 + adjacentPoints(p, map) // count this + adjacent flashesh
            .filter(a -> map[a.y][a.x] != 0) // can flash only once, filter out already flashed (energy 0)
            .mapToLong(a -> { map[a.y][a.x]++; return flash(map, a); }) // increase adjacent energy and flash if needed
            .sum();
    }

    /** Return stream of all adjacent points for given point inside map */
    static Stream<Point> adjacentPoints(Point origin, int[][] map) {
        return pointsByBox(new Point(origin.x-1, origin.y-1), new Point(origin.x+1, origin.y+1))
            // filter out points outside map and origin point itself
            .filter(p -> p.x >= 0 && p.y >= 0 && p.y < map.length && p.x < map[p.y].length && !(p.x == origin.x && p.y == origin.y));
    }
    
    /** Return stream of all points for map */
    static Stream<Point> allPoints(int[][] map) {
        return pointsByBox(new Point(0, 0), new Point(map[0].length-1, map.length-1));
    }

    /** Return stream of all points inside box defined by points p1 and p2 */
    static Stream<Point> pointsByBox(Point p1, Point p2) {
        return IntStream.range(p1.y, p2.y+1).mapToObj(i -> i)
            .flatMap(y -> IntStream.range(p1.x, p2.x+1).mapToObj(x -> new Point(x, y)));
    }
}