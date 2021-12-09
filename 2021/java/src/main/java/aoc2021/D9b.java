package aoc2021;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aoc2021.D9a.Point;
import static aoc2021.D9a.readMap;
import static aoc2021.D9a.findLowPoints;

/**
 * Advent of Code (AOC) 2021 Day 9 part 2
 */
public class D9b {

    public static void main(String ... args) throws IOException {

        var map = readMap("../input/9a.txt");

        var biggestBasinsMultiplied = findLowPoints(map).stream()
            .map(p -> findBasin(p, map, new ArrayList<>()))
            .map(b -> b.size())
            .sorted((a,b) -> b-a)
            .limit(3)
            .reduce(1, (a,b) -> a * b, (a,b) -> b);

        System.out.printf("%s\n",  biggestBasinsMultiplied);
    }

    static List<Point> findBasin(Point p, int[][]map, List<Point> basin) {
        int h = D9a.height(p.x(), p.y(), map);
        if (h >= 9 || basin.contains(p)) {
            return Collections.emptyList();
        }
        basin.add(p);

        findBasin(new Point(p.x()+1, p.y()), map, basin);
        findBasin(new Point(p.x()-1, p.y()), map, basin);
        findBasin(new Point(p.x(), p.y()+1), map, basin);
        findBasin(new Point(p.x(), p.y()-1), map, basin);

        return basin;
    }
}