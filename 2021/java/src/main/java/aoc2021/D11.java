package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.lang.Math.max;
/**
 * Advent of Code (AOC) 2021 Day 11 part 1 and 2
 */
public class D11 {

    public static void main(String ... args) throws IOException {

        var map = Files.lines(Path.of("../input/11a.txt"))
            .map(row -> row.chars().map(i -> i - '0').toArray())
            .toArray(int[][]::new);
        
        var count = IntStream.range(0, 100)
            .mapToLong(i -> step(map)).sum();

        System.out.printf("part 1: %d\n", count);

        int step = 100;
        while(!allFlashes(map)) {
            step(map);
            step++;
        }

        System.out.printf("part 2: %d\n", step);
    }

    static boolean allFlashes(int[][] map) {
        return !Arrays.stream(map).flatMapToInt(row -> Arrays.stream(row))
            .anyMatch(v -> v != 0);
    }

    static long step(int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
               map[y][x]++;
            }
        }
        long count = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                count += flash(map, x, y);
            }
        }
        return count;
    }

    static long flash(int[][] map, int x, int y) {
        if (map[y][x] < 10) { // only flash 10 or larger
            return 0;
        }
        long count = 1;
        map[y][x] = 0; // reset to 0 after flashing
        for (int nx = max(0, x-1); nx <= min(x+1, map[y].length-1); nx++) {
            for (int ny = max(0, y-1); ny <= min(y+1, map.length-1); ny++) {
                if (map[ny][nx] == 0) { // can only flash once per step, 0 is already flashed
                    continue;
                }
                map[ny][nx]++;
                count += flash(map, nx, ny);
            }
        }
        return count;
    }

}