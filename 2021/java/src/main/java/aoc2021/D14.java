package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 14
 */
 public class D14 {

    static Map<String,Map<Character,Long>> cache = new ConcurrentSkipListMap<>();
    static Map<String,Character> pairToCharacterRule;

    public static void main(String ... args) throws IOException {
        List<String> data = Files.lines(Path.of("../input/14a.txt")).toList();
        String template = data.get(0);
        pairToCharacterRule = data.stream().skip(2)
            .map(l -> l.split(" -> "))
            .collect(Collectors.toMap(l -> l[0], l -> l[1].charAt(0)));
        
        var part1 = solve(template, 10);
        System.out.printf("part 1: %d\n", part1);

        var part2 = solve(template, 40);
        System.out.printf("part 2: %d\n", part2);
    }

    static long solve(String template, int depth) {
        Map<Character,Long> result = new TreeMap<>();
        Iterator<char[]> iter = stringAsCharPairs(template).iterator();
        while (iter.hasNext()) { // Iterate template in pairs NNCB -> NN,NC,CB
            char[] pair = iter.next();
            boolean lastPair = !iter.hasNext(); // Identify last pair for calculation
            var countForPair = countCharsForPair(pair, lastPair, depth);
            result = mergeCountsByCharacter(result, countForPair);
        }
        // Calculate and return the difference between max and min char counts
        long max = result.values().stream().mapToLong(i -> i).max().getAsLong();
        long min = result.values().stream().mapToLong(i -> i).min().getAsLong();
        return max-min;
    }


    static Map<Character,Long> countCharsForPair(char[] pair, boolean lastPair, int depth) {
        if (depth == 0) {
            // For last pair count both chars, for others only the first char
            return lastPair ? Map.of(pair[0], 1l, pair[1], 1l) : Map.of(pair[0], 1l);
        }
        return cache.computeIfAbsent(new String(pair)+lastPair+depth, k -> {
            // Calculate pair recursively if not calculate already
            char pairToChar = pairToCharacterRule.get(new String(pair));
            char[] pair1 =  { pair[0],  pairToChar };
            char[] pair2 = { pairToChar,  pair[1]};
            var p1Count = countCharsForPair(pair1, false, depth-1);
            var p2Count = countCharsForPair(pair2, lastPair, depth-1);
            return mergeCountsByCharacter(p1Count, p2Count);
        });
    }

    static Map<Character,Long> mergeCountsByCharacter(Map<Character,Long> a,Map<Character,Long> b) {
        return Stream.concat(a.entrySet().stream(), b.entrySet().stream())
            .collect(Collectors.groupingBy(e -> e.getKey(), Collectors.summingLong(e -> e.getValue())));
    }    
    
    static List<char[]> stringAsCharPairs(String template) {
        List<char[]> pairs = new ArrayList<>();
        template.chars().reduce((a,b) -> { pairs.add(new char[]{(char)a, (char)b}); return b; });
        return pairs;
    }
}


