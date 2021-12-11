package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 9
 */
public class D9 {

    record Point(int x, int y) {
        Point adj(int nx, int ny) {
            return new Point(x + nx, y + ny);
        }
    }

    public static void main(String ... args) throws IOException {

        var map = readMap("../input/9a.txt");

        var lowPoints = findLowPoints(map);

        var sumOfRiskLevels = lowPoints.stream()
            .mapToInt(p -> map[p.y][p.x] + 1)
            .sum();

        System.out.printf("part 1: %d\n", sumOfRiskLevels);

        var biggestBasinsMultiplied = lowPoints.stream()
            .map(p -> findBasin(p, map, new ArrayList<>()))
            .map(b -> b.size())
            .sorted((a,b) -> b-a)
            .limit(3)
            .reduce(1, (a,b) -> a * b, (a,b) -> b);

        System.out.printf("part 2: %s\n",  biggestBasinsMultiplied);
    }

    static List<Point> findLowPoints(int[][] map) {
        return IntStream.range(0, map.length).boxed()
            .flatMap(y -> IntStream.range(0, map[y].length).mapToObj(x -> new Point(x, y)))
            .filter(p -> isLowPoint(p, map))
            .toList();
    }

    static boolean isLowPoint(Point p, int[][]map) {
        int h = height(p, map);
        int adjacentMin = Stream.of(p.adj(-1,0), p.adj(1,0), p.adj(0, -1), p.adj(0, 1)) // adjacent points
            .mapToInt(a -> height(a, map)) // get height
            .min().getAsInt(); // min height
        return h < adjacentMin;
    }

    static int height(Point p, int [][]map) {
        if (p.x < 0 || p.y < 0 || p.y >= map.length || p.x >= map[0].length) {
            return 9;
        }
        return map[p.y][p.x];
    }

    static List<Point> findBasin(Point p, int[][]map, List<Point> basin) {
        int h = height(p, map);
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

    static int[][] readMap(String file) throws IOException {
        return Files.lines(Path.of(file))
            .map(row -> row.chars().map(i -> i - '0').toArray())
            .toArray(int[][]::new);
    }
}