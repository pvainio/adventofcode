package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Advent of Code (AOC) 2021 Day 8
 */
public class D8 {

    record Entry(List<String> patterns, List<String> digits, Map<Integer,String> solved) {
        static Entry parse(String line) {
            String[] parts = line.split("\\|");
            return new Entry(List.of(parts[0].trim().split(" ")), List.of(parts[1].trim().split(" ")), new HashMap<>());
        }
    }

    public static void main(String ... args) throws Exception {

        List<Entry> data = Files.lines(Path.of("../input/8a.txt"))
            .map(Entry::parse)
            .toList();

        var count1 = data.stream()
            .flatMap(e -> e.digits.stream())
            .filter(digit -> isSimple(digit))
            .count();
        System.out.printf("part 1: %d\n", count1);

        var count2 = data.stream()
            .mapToLong(e -> solve(e))
            .sum();
        System.out.printf("part 2: %d\n", count2);
    }

    static Set<Integer> simpleDigitLen = Set.of(2, 3, 4, 7);

    static boolean isSimple(String digit) {
        return simpleDigitLen.contains(digit.length());
    }

    static long solve(Entry e) {
        // Solve simple
        e = solve(e, 1, p -> p.length() == 2);
        e = solve(e, 7, p -> p.length() == 3);
        e = solve(e, 4, p -> p.length() == 4);
        e = solve(e, 8, p -> p.length() == 7);

        // 3 has 5 segments and contains all segments of 1
        String digit1 = e.solved.get(1);
        e = solve(e, 3, p -> p.length() == 5 && containsChars(p, digit1));
        // 6 has 6 segments and does not contain all segments of 1
        e = solve(e, 6, p -> p.length() == 6 && !containsChars(p, digit1));

        // separate 2 and 5 based on top left segment
        var topLeft = solveTopLeft(e.solved);
        e = solve(e, 2, p -> p.length() == 5 && p.indexOf(topLeft) != -1);
        e = solve(e, 5, p -> p.length() == 5 && p.indexOf(topLeft) == -1);

        // separate 9 and 0 based on bottom right segment
        var bottomRight = solveBottomRight(e.solved);
        e = solve(e, 9, p -> p.length() == 6 && p.indexOf(bottomRight) == -1);
        e = solve(e, 0, p -> p.length() == 6 && p.indexOf(bottomRight) != -1);

        var solved = e.solved;
        var str = e.digits.stream().map(d -> toDigit(solved, d)).collect(Collectors.joining(""));

        return Long.parseLong(str);
    }

    static String toDigit(Map<Integer,String> solved, String pattern) {
        return solved.entrySet().stream()
            .filter(e -> e.getValue().length() == pattern.length() && containsChars(e.getValue(), pattern))
            .map(e -> e.getKey().toString())
            .findAny().get();
    }

    static Entry solve(Entry e, int digit, Function<String,Boolean> match) {
        String pattern = e.patterns.stream().filter(p -> match.apply(p)).findFirst().get();
        e.solved.put(digit, pattern);
        List<String> patterns = new ArrayList<>(e.patterns);
        patterns.remove(pattern);
        return new Entry(patterns, e.digits, e.solved);        
    }

    static int solveTopLeft(Map<Integer,String> solved) {
        // char of digit 1 does not exist in digit 6
        return solved.get(1).chars().filter(c -> solved.get(6).indexOf(c) == -1).findFirst().getAsInt();
    }

    static int solveBottomRight(Map<Integer,String> solved) {
        // segments of 6 - segments of 5
        return solved.get(6).chars().filter(c -> solved.get(5).indexOf(c) == -1).findFirst().getAsInt();    
    }

    static boolean containsChars(String str, String chars) {
        return chars.chars().filter(c -> str.indexOf(c) != -1).count() == chars.length();
    }
}