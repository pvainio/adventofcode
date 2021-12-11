package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Advent of Code (AOC) 2021 Day 3
 */
public class D3 {

    public static void main(String ... args) throws Exception {
        List<int[]> data = Files.lines(Path.of("../input/3a.txt"))
            .map(line -> line.chars().map(c -> c - '0').toArray())
            .toList();

        int bitLen = data.get(0).length;
        int[] oneCounts = data.stream().reduce(new int[bitLen], (count, line) -> sum(count, line), (a,b) -> b); // count ones in int array      
        int[] gammaBits = Arrays.stream(oneCounts).map(oneCount -> oneCount*2 >= data.size() ? 1 : 0).toArray(); // translate most common bits
        int[] epsilonBits = Arrays.stream(oneCounts).map(oneCount -> oneCount*2 < data.size() ? 1 : 0).toArray(); // translate least common bits

        System.out.printf("part 1: %d\n", bitsToNumber(gammaBits) * bitsToNumber(epsilonBits));

        int[] o2Bits = filter(data, true);
        int[] co2Bits = filter(data, false);

        System.out.printf("part 2: %d\n", bitsToNumber(o2Bits) * bitsToNumber(co2Bits));
    }

    static int bitsToNumber(int[] bits) {
        return Integer.parseInt(Arrays.stream(bits).mapToObj(i -> Integer.toString(i)).collect(Collectors.joining()), 2);
    }

    static int[] sum(int[] a, int[] b) {
        int[] r = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            r[i] = a[i] + b[i];
        }
        return r;
    }

    static int mostCommonBitInPos(List<int[]> data, int pos) {
        var ones = data.stream().map(l -> l[pos]).filter(c -> c == 1).count();
        return ones*2 >= data.size() ? 1 : 0;
    }

    static int[] filter(List<int[]> data, boolean selectMostCommon) {
        int pos = 0;
        while(data.size() > 1) {
            int p2 = pos;
            int mostCommonBit = mostCommonBitInPos(data, pos);
            int accept = selectMostCommon ? mostCommonBit : mostCommonBit == 1 ? 0 : 1;
            data = data.stream().filter(l -> l[p2] == accept).toList();
            pos++;
        }
        return data.get(0);
    }
}