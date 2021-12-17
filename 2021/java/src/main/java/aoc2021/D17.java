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
    record StepResult(int x, int y, int maxY){}
    enum Hit {HIT, YVELOCITY_HIGH, LONG, SHORT}
    record ShotResult(Hit hit, int maxY){}

    public static void main(String... args) throws IOException {

        Area target = Files.lines(Path.of("../input/17a.txt"))
            .map(l -> l.substring("target area:".length()).split(","))
            .map(parts -> Area.parse(parts[0], parts[1])).findFirst().get();

        int hits = 0;
        int maxHeight = 0;
        
        xloop:
        for (int xv = 0; xv <= target.x2; xv++) {
            for (int yv = target.y2;;yv++) {
                ShotResult res = shoot(xv, yv, target);
                if (res.hit == Hit.HIT) {
                    hits++;
                    maxHeight = res.maxY > maxHeight ? res.maxY : maxHeight;
                } else if (res.hit == Hit.YVELOCITY_HIGH || res.hit == Hit.LONG) {
                    continue xloop;
                }
            }
        }

        System.out.printf("part 1: %d\n", maxHeight);
        System.out.printf("part 2: %d\n", hits);
    }

    static ShotResult shoot(int xv, int yv, Area target) {
        StepResult prev = null;
        for (int step = 1; ; step++) {
            StepResult res = atStep(step, xv, yv);
            if (res.x >= target.x1 && res.x <= target.x2 && res.y <= target.y1 && res.y >= target.y2) {
                return new ShotResult(Hit.HIT, res.maxY);
            } else if (res.y < target.y2 && prev.y > target.y1 && prev.y-res.y > (target.y1-target.y2)*2) {
                return new ShotResult(Hit.YVELOCITY_HIGH, 0); // y velocity so high it missing the target withing one step
            } else if (res.x > target.x2) {
                return new ShotResult(Hit.LONG, 0);
            } else if (res.y < target.y2) {
                return new ShotResult(Hit.SHORT, 0);
            }
            prev = res;
        }
    }

    static StepResult atStep(int steps, int xv, int yv) {
        int x = 0, y = 0, maxHeight = 0;
        for (int step = 0; step < steps; step++) {
            x += xv;
            y += yv;
            xv = xv > 0 ? xv - 1 : 0;
            yv = yv - 1;
            maxHeight = maxHeight < y ? y : maxHeight;
        }
        return new StepResult(x, y, maxHeight);
    }
}