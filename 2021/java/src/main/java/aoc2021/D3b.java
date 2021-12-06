package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Advent of Code (AOC) 2021 Day 3 part 2
 */
public class D3b {

    static char mostCommon(List<String> data, int pos) {
        var ones = data.stream()
            .map(l -> l.charAt(pos))
            .filter(c -> c == '1')
            .count();
        var zeros = data.size() - ones;
        return ones >= zeros ? '1' : '0';
    }

    static String filter(List<String> data, boolean mostCommon) {
        int pos = 0;
        while(data.size() > 1) {
            int p2 = pos;
            char mcChar = mostCommon(data, pos);
            char accept = mostCommon ? mcChar : mcChar == '1' ? '0' : '1';
            data = data.stream().filter(l -> l.charAt(p2) == accept).toList();
            pos++;
        }
        return data.get(0);
    }

    public static void main(String ... args) throws Exception {
        List<String> data = Files.lines(Path.of("../input/3a.txt")).toList();

        String o2 = filter(data, true);
        String co2 = filter(data, false);

        int o2Rate = Integer.parseInt(o2, 2);
        int co2Rate = Integer.parseInt(co2, 2);
        System.out.printf("%d %d %d\n", o2Rate, co2Rate, o2Rate * co2Rate);
    }
}