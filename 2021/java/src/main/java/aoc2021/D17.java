package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 17
 */
public class D17 {

    record Area(int x1, int x2, int y1, int y2) {}
    record Probe(int x, int y, int xv, int yv, int maxHeight) {
        Probe move() {
            return new Probe(x+xv, y+yv, Math.max(0, xv-1), yv - 1, Math.max(maxHeight, y+yv));
        }
        boolean hit(Area target) {
            return x >= target.x1 && x <= target.x2 && y <= target.y1 && y >= target.y2;
        }
        boolean miss(Area target) {
            return x > target.x2 || y < target.y2;
        }
    }

    public static void main(String... args) throws IOException {
        int[] numbers = Pattern.compile("(-?\\d+)")
            .matcher(Files.readString(Path.of("../input/17a.txt"))).results()
            .mapToInt(mr -> Integer.parseInt(mr.group(1))).toArray();
        Area target = new Area(numbers[0], numbers[1], numbers[3], numbers[2]);

        var hits = IntStream.rangeClosed(0, target.x2).boxed() // iterate x velocities
            .flatMap(xv -> IntStream.range(target.y2, 1000) // and y velocitios
                .mapToObj(yv -> shoot(xv, yv, target))) // shoot each x,y
            .filter(r -> r.hit(target)).toList(); // collect hits to list

        System.out.printf("part 1: %d\n", hits.stream().mapToInt(i -> i.maxHeight).max().getAsInt());
        System.out.printf("part 2: %d\n", hits.size());
    }

    static Probe shoot(int xv, int yv, Area target) {
        return Stream.iterate(new Probe(0, 0, xv, yv, 0), Probe::move) // iterate probe moves
            .dropWhile(p -> !p.hit(target) && !p.miss(target)) // until hit or miss
            .findFirst().get();
    }
}