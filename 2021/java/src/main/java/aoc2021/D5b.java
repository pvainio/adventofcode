package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import aoc2021.D5a.Line;

/**
 * Advent of Code (AOC) 2021 Day 5 part 2
 */
public class D5b {

    public static void main(String ... args) throws Exception {

        var data = Files.lines(Path.of("../input/5a.txt"))
            .map(Line::parse)
            .toList();

        Map<Integer,Map<Integer,Integer>> coords = new TreeMap<>();
        data.forEach(l -> D5a.plot(coords, l));

        System.out.printf("%d\n", D5a.sum(coords));
    }
}