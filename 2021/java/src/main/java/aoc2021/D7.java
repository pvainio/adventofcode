package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Advent of Code (AOC) 2021 Day 7
 */
public class D7 {

    public static void main(String ... args) throws Exception {

        var crab = Files.lines(Path.of("../input/7a.txt"))
            .flatMap(l -> Arrays.asList(l.split(",")).stream())
            .map(Integer::parseInt)
            .sorted().toList();

        int median = crab.get(crab.size()/2);
        var cost1 = calculateCost(median, crab, d -> d);
        System.out.printf("part 1: %d\n", cost1);

        int mean = (int) crab.stream().mapToInt(i -> i).average().getAsDouble();
        var cost2 = calculateCost(mean, crab, d -> IntStream.rangeClosed(1, d).sum());
        System.out.printf("part 2: %d\n", cost2);
    }

    static int calculateCost(int pos, List<Integer> crab, Function<Integer,Integer> costFn) {
        return crab.stream().mapToInt(i -> costFn.apply(Math.abs(pos-i))).sum();
    }
}