package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 14
 */
public class D14 {

    public static void main(String... args) throws IOException {
        List<String> data = Files.lines(Path.of("../input/14a.txt")).toList();
        String template = data.get(0);

        Map<String, String> pairToCharRules = data.stream().skip(2)
                .map(l -> l.split(" -> "))
                .collect(Collectors.toMap(l -> l[0], l -> l[1]));

        var part1 = solve(template, 10, pairToCharRules);
        System.out.printf("part 1: %d\n", part1);

        var part2 = solve(template, 40, pairToCharRules);
        System.out.printf("part 2: %d\n", part2);
    }

    static long solve(String template, int steps, Map<String, String> pairToCharRules) {
        Map<String, Long> pairs = countPairs(template);

        for (int s = 0; s < steps; s++) {
            pairs = doStep(pairs, pairToCharRules);
        }

        Map<Character, Long> sumByChar = countFirstCharsOfPairs(pairs);
        // Have to add last char of template, since not included in the sumByChar sum
        sumByChar.merge(template.charAt(template.length() - 1), 1l, (a, b) -> a + b);

        // Calculate and return the difference between max and min char counts
        long max = sumByChar.values().stream().mapToLong(i -> i).max().getAsLong();
        long min = sumByChar.values().stream().mapToLong(i -> i).min().getAsLong();
        return max - min;
    }

    static Map<String, Long> doStep(Map<String, Long> pairs, Map<String, String> pairToCharRules) {
        return pairs.entrySet().stream()
                // explode one pair to two pairs by the rules
                .flatMap(pair -> pairToTwoPairs(pair.getKey(), pair.getValue(), pairToCharRules))
                // sum the counts by pair
                .collect(Collectors.groupingBy(pair -> pair.getKey(), Collectors.summingLong(pair -> pair.getValue())));
    }

    static Stream<Map.Entry<String, Long>> pairToTwoPairs(String pair, long count, Map<String, String> pairToCharRules) {
        String pairToChar = pairToCharRules.get(pair);
        String pair1 = pair.charAt(0) + pairToChar;
        String pair2 = pairToChar + pair.charAt(1);
        return Stream.of(Map.entry(pair1, count), Map.entry(pair2, count));
    }

    static Map<Character, Long> countFirstCharsOfPairs(Map<String, Long> pairs) {
        return pairs.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getKey().charAt(0),
                        Collectors.summingLong(e -> e.getValue())));
    }

    static Map<String, Long> countPairs(String str) {
        return stringAsPairs(str).stream()
                .collect(Collectors.groupingBy(pair -> pair, Collectors.counting()));
    }

    static List<String> stringAsPairs(String template) {
        List<String> pairs = new ArrayList<>();
        template.chars().reduce((a, b) -> {
            pairs.add(new String(new char[] { (char) a, (char) b }));
            return b;
        });
        return pairs;
    }
}