package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Advent of Code (AOC) 2021 Day 17
 */
public class D17 {

    record Area(int x1, int x2, int y1, int y2) {}
    record ShotResult(boolean hit, int maxY){}

    public static void main(String... args) throws IOException {
        int[] numbers = Pattern.compile("(-?\\d+)")
            .matcher(Files.readString(Path.of("../input/17a.txt"))).results()
            .mapToInt(mr -> Integer.parseInt(mr.group(1))).toArray();
        Area target = new Area(numbers[0], numbers[1], numbers[3], numbers[2]);

        int hits = 0;
        int maxHeight = 0;

        for (int xv = 0; xv <= target.x2; xv++) {
            for (int yv = target.y2; yv < 1000;yv++) {
                ShotResult res = shoot(xv, yv, target);
                if (res.hit) {
                    hits++;
                    maxHeight = Math.max(maxHeight, res.maxY);
                }
            }
        }

        System.out.printf("part 1: %d\n", maxHeight);
        System.out.printf("part 2: %d\n", hits);
    }

    static ShotResult shoot(int xv, int yv, Area target) {
        int x = 0, y = 0, maxHeight = 0;
        while (true) {
            x += xv;
            y += yv;
            xv = Math.max(0, xv-1);
            yv = yv - 1;
            maxHeight = Math.max(maxHeight, y);

            if (x >= target.x1 && x <= target.x2 && y <= target.y1 && y >= target.y2) {
                return new ShotResult(true, maxHeight); // Hit target
            } else if (x > target.x2 || y < target.y2) {
                return new ShotResult(false, 0); // Miss
            } 
        }
    }
}