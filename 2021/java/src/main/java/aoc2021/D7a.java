package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Advent of Code (AOC) 2021 Day 7 part 1
 */
public class D7a {

    public static void main(String ... args) throws Exception {

        var crab = Files.lines(Path.of("../input/7a.txt"))
            .flatMap(l -> Arrays.asList(l.split(",")).stream())
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        int pos = (int) crab.stream().mapToInt(i -> i).average().getAsDouble();

        var cost = findMinCost(pos, crab, d -> d);

        System.out.printf("%d\n", cost);
    }

    static int findMinCost(int pos, List<Integer> crab, Function<Integer,Integer> costFn) {
        int cost = calculateCost(pos, crab, costFn);
        while (cost > calculateCost(pos-1, crab, costFn)) {
            pos -= 1;
            cost = calculateCost(pos, crab, costFn);
        }
        while (cost > calculateCost(pos+1, crab, costFn)) {
            pos += 1;
            cost = calculateCost(pos, crab, costFn);
        }
        return cost;
    }

    static int calculateCost(int pos, List<Integer> crab, Function<Integer,Integer> costFn) {
        return crab.stream().mapToInt(i -> costFn.apply(Math.abs(pos-i))).sum();
    }
}