package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Advent of Code (AOC) 2021 Day 17
 */
public class D17 {

    record Area(int x1, int x2, int y1, int y2) {}
    record ShotResult(boolean hit, int maxHeight) {}

    public static void main(String... args) throws IOException {
        int[] numbers = Pattern.compile("(-?\\d+)")
            .matcher(Files.readString(Path.of("../input/17a.txt"))).results()
            .mapToInt(mr -> Integer.parseInt(mr.group(1))).toArray();
        Area target = new Area(numbers[0], numbers[1], numbers[3], numbers[2]);

        var hits = IntStream.rangeClosed(0, target.x2).boxed() // iterate x velocities
            .flatMap(xv -> IntStream.range(target.y2, 1000) // and y velocitios
                .mapToObj(yv -> shoot(xv, yv, target))) // shoot each x,y
            .filter(r -> r.hit()).toList(); // collect hits to list

        System.out.printf("part 1: %d\n", hits.stream().mapToInt(i -> i.maxHeight).max().getAsInt());
        System.out.printf("part 2: %d\n", hits.size());
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