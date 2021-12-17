package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 17
 */
public class D17 {

    record Area(int x1, int y1, int x2, int y2) {
        static Area parse(String xStr, String yStr) {
            int[] x = Stream.of(xStr.substring(3).split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            int[] y = Stream.of(yStr.substring(3).split("\\.\\.")).mapToInt(Integer::parseInt).toArray();            
            return new Area(x[0],y[1],x[1],y[0]);
        }
    }

    record ShotResult(boolean hit, int maxY){}

    public static void main(String... args) throws IOException {

        Area target = Files.lines(Path.of("../input/17a.txt"))
            .map(l -> l.substring("target area:".length()).split(","))
            .map(parts -> Area.parse(parts[0], parts[1])).findFirst().get();

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