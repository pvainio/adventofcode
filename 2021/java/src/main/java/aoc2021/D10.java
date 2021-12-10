package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Advent of Code (AOC) 2021 Day 10 part 1 and 2
 */
public class D10 {

    record NavigationLine(Optional<Character> firstIllegal, List<Character> missing) {};

    public static void main(String ... args) throws IOException {

        var navLines = Files.lines(Path.of("../input/10a.txt"))
            .map(row -> parseLine(row))
            .toList();
            
        long scorePart1 = navLines.stream()
            .flatMap(l -> l.firstIllegal.stream())
            .mapToLong(c -> scoresPart1.get(c))
            .sum();

        System.out.printf("part 1: %d\n", scorePart1);

        var scorePart2 = navLines.stream()
            .map(l -> l.missing)
            .filter(m -> !m.isEmpty())
            .map(m -> scorePart2(m))
            .sorted()
            .toList();

        System.out.printf("part 2: %d\n", scorePart2.get(scorePart2.size()/2));
    }

    static Map<Character,Character> braces = Map.of('(',')','{','}','[',']','<','>');

    static NavigationLine parseLine(String line) {
        LinkedList<Character> stack = new LinkedList<>();
        for(char c : line.toCharArray()) {
            if (braces.containsKey(c)) {
                stack.push(braces.get(c));
            } else if (stack.pop() != (char)c) {
                return new NavigationLine(Optional.of(c), List.of());
            } 
        }
        return new NavigationLine(Optional.empty(), stack);
    }

    static Map<Character,Long> scoresPart1 = Map.of(')', 3l, ']', 57l, '}', 1197l, '>', 25137l);
    static Map<Character,Long> scoresPart2 = Map.of(')', 1l, ']', 2l, '}', 3l, '>', 4l);

    static long scorePart2(List<Character> row) {
        return row.stream()
            .map(c -> scoresPart2.get(c))
            .reduce(0l, (a, b) -> a*5+b, (a,b) -> b);
    }
}