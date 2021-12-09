package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Advent of Code (AOC) 2021 Day 9 part 1
 */
public class D9a {

    record Point(int x, int y) {}

    public static void main(String ... args) throws IOException {

        var map = readMap("../input/9a.txt");

        var lowPoints = findLowPoints(map);

        var sumOfRiskLevels = lowPoints.stream()
            .mapToInt(p -> map[p.y][p.x] + 1)
            .sum();

        System.out.printf("%d\n", sumOfRiskLevels);
    }

    static List<Point> findLowPoints(int[][] map) {
        List<Point> lowPoints = new ArrayList<>();
        for(int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (isLowPoint(x,y, map)) {
                    lowPoints.add(new Point(x, y));
                }
            }
        }
        return lowPoints;
    }

    static boolean isLowPoint(int x, int y, int[][]map) {
        int h = height(x, y, map);
        int adjacentMin = IntStream.of(height(x-1, y, map), height(x+1, y, map), height(x, y-1, map), height(x, y+1, map)).min().getAsInt();
        return h < adjacentMin;
    }

    static int height(int x, int y, int [][]map) {
        if (x < 0 || y < 0 || y >= map.length || x >= map[0].length) {
            return 9;
        }
        return map[y][x];
    }

    static int[][] readMap(String file) throws IOException {
        return Files.lines(Path.of(file))
            .map(row -> row.chars().map(i -> i - '0').toArray())
            .toArray(int[][]::new);
    }
}