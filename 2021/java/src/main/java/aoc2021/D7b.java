package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Advent of Code (AOC) 2021 Day 7 part 2
 */
public class D7b {

    public static void main(String ... args) throws Exception {

        var crab = Files.lines(Path.of("../input/7a.txt"))
            .flatMap(l -> Arrays.asList(l.split(",")).stream())
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        int pos = (int) crab.stream().mapToInt(i -> i).average().getAsDouble();

        var cost = D7a.findMinCost(pos, crab, d -> { int s=0; for(int i = 1; i <= d; i++) { s+=i; } return s;});

        System.out.printf("%d\n", cost);
    }
}