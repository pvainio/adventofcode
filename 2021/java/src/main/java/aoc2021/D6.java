package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 6
 */
public class D6 {

    public static void main(String ... args) throws Exception {
        var fish = Files.lines(Path.of("../input/6a.txt"))
            .flatMap(l -> Arrays.asList(l.split(",")).stream()).map(Integer::parseInt).toList();

        part1(fish);

        part2(fish);
    }

    static void part1(List<Integer> fish) {
        for(int i = 0; i < 80; i++) {
            fish = spawn1(fish);
        }

        System.out.printf("part 1: %d\n", fish.size());
    }

    static List<Integer> spawn1(List<Integer> fish) {
        return fish.stream()
            .flatMap(c -> c == 0 ? Stream.of(8, 6) : Stream.of(c -1))
            .toList();
    }

    record Fish(int timer, long count) {}

    static void part2(List<Integer> data) {
        var fish = data.stream()
            .map(c -> new Fish(c, 1))
            .collect(Collectors.groupingBy(i -> i.timer(), Collectors.summingLong(i -> i.count())));

        for(int i = 0; i < 256; i++) {
            fish = spawn2(fish);
        }

        System.out.printf("part 2: %s\n", fish.values().stream().mapToLong(i -> i).sum());
    }

    static Map<Integer,Long> spawn2(Map<Integer, Long> fish) {
        return fish.entrySet().stream().map(e -> new Fish(e.getKey(), e.getValue()))
            .flatMap(f -> f.timer == 0 ? 
                Stream.of(new Fish(8, f.count), new Fish(6, f.count)) :
                Stream.of(new Fish(f.timer - 1, f.count)))
            .collect(Collectors.groupingBy(f -> f.timer, Collectors.summingLong(f -> f.count)));
    }
}