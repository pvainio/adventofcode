package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Advent of Code (AOC) 2021 Day 22 Reactor Reboot
 */
public class D22 {

    record Range(int start, int end) {
        Range intersect(Range other) { // common part for both ranges
            return new Range(max(start, other.start), min(end, other.end));
        }

        Stream<Range> split(Range other) { // return all different ranges that can be built from these two ranges
            return Stream.of(
                    new Range(start, min(other.start - 1, end)),
                    new Range(max(start, other.start), min(end, other.end)),
                    new Range(max(other.end + 1, start), end))
                    .filter(r -> r.size() > 0);
        }

        static Range parse(String str) {
            int p[] = Arrays.stream(str.substring(2).split("\\.\\.")).mapToInt(Integer::parseInt).toArray();
            return new Range(p[0], p[1]);
        }

        long size() {
            return end - start + 1;
        }
    }

    record Cube(boolean on, Range x, Range y, Range z) {
        static Cube parse(String str) {
            String parts[] = str.split(" ");
            Range[] p = Arrays.stream(parts[1].split(",")).map(Range::parse).toArray(Range[]::new);
            return new Cube(parts[0].equals("on"), p[0], p[1], p[2]);
        }

        long countOn() { // number of cubes set on
            return on ? x.size() * y.size() * z.size() : 0;
        }

        Cube intersect(Cube c) { // intersect of two cubes
            return new Cube(!c.on, x.intersect(c.x), y.intersect(c.y), z.intersect(c.z));
        }

        boolean valid() { // if this cube is a valid cube
            return !Stream.of(x, y, z).anyMatch(i -> i.size() <= 0);
        }

        // subtract given cube from this cube, result is all the cubes exploded from this cube except the given input cube
        List<Cube> subtract(Cube c) {
            Cube intersect = intersect(c);
            if (!intersect.valid()) {
                return List.of(this);
            }

            return this.x.split(c.x) // loop through all x splits
                    .flatMap(ix -> this.y.split(c.y) // loop through all y splits
                            .flatMap(iy -> this.z.split(c.z) // loop through all z splits
                                    // skip if this part of the given input cube
                                    .filter(iz -> !(ix.equals(intersect.x) && iy.equals(intersect.y) && iz.equals(intersect.z)))
                                    .map(iz -> new Cube(on, ix, iy, iz)))) // otherwise add to result
                    .toList();
        }
    }

    public static void main(String... args) throws IOException {
        List<Cube> cubes = Files.lines(Path.of("../input/22a.txt"))
                .map(Cube::parse).toList();

        List<Cube> part1cubes = cubes.stream()
                .filter(c -> c.intersect(new Cube(true, new Range(-50, 50), new Range(-50, 50), new Range(-50, 50))).valid()).toList();

        System.out.printf("part 1: %s\n", reboot(part1cubes));

        System.out.printf("part 2: %s\n", reboot(cubes));
    }

    static long reboot(List<Cube> steps) {
        List<Cube> result = new CopyOnWriteArrayList<>();
        for (Cube step : steps) { // go trough all the steps
            for (Cube existing : result) { // go through current result
                result.remove(existing); // remove the result
                result.addAll(existing.subtract(step)); // ad it back with the current step subtracted
            }
            result.add(step); // add current step to result

        }
        return result.stream().mapToLong(Cube::countOn).sum(); // count on
    }
}