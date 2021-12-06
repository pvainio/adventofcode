package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 6 part 1
 */
public class D6a {

    public static void main(String ... args) throws Exception {

        var fish = Files.lines(Path.of("../input/6a.txt"))
            .flatMap(l -> Arrays.asList(l.split(",")).stream()).map(Integer::parseInt).toList();

        for(int i = 0; i < 80; i++) {
            fish = spawn(fish);
        }

        System.out.printf("%s\n", fish.size());
    }

    static List<Integer> spawn(List<Integer> fish) {
        return fish.stream()
            .flatMap(c -> c == 0 ? Stream.of(8, 6) : Stream.of(c -1))
            .toList();
    }
}