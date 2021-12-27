package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Advent of Code (AOC) 2021 Day 24 Arithmetic Logic Unit ALU
 */
public class D24 {

    enum Op {inp, add, mul, div, mod, eql}

    record Inst(Op op, String a, String b) {
        static Inst parse(String str) {
            String parts[] = str.split(" ");
            return new Inst(Op.valueOf(parts[0]), parts[1], parts.length > 2 ? parts[2] : null);
        }
    }

    public static void main(String... args) throws IOException {

        LinkedList<Inst> alu = Files.lines(Path.of("../input/24a.txt"))
            .map(Inst::parse).collect(Collectors.toCollection(LinkedList::new));
        List<Long> solved = solve(0, alu, 0, "", null);
        System.out.printf("part 1: %s\n", solved.stream().mapToLong(i -> i).max().getAsLong());
        System.out.printf("part 2: %s\n", solved.stream().mapToLong(i -> i).min().getAsLong());
    }

    // recursively solve each number (n) using cache for speedup
    static List<Long> solve(int n, List<Inst> alu, int z, String prefix, Map<Byte, Set<Integer>> cache) {
        if (n == 14 && z == 0) { // Found a solution
            return List.of(Long.parseLong(prefix)); 
        } else if (n == 14) {
            return List.of();
        }

        Set<Integer> zcache = null;
        if (n == 2) {
            cache = new HashMap<>(); // Create new cache at level 2, otherwise it will get too big
        } else if (n > 2) {
            zcache = cache.computeIfAbsent((byte)n, (k) -> new HashSet<Integer>());
            if (zcache.contains(z)) {
                return List.of();
            }    
        }

        var solved = new ArrayList<Long>();
        var alun = alu.subList(n*18, (n+1)*18);
        for (int i = 1; i < 10; i++) {
            int nz = run(alun, z, i);  
            solved.addAll(solve(n+1, alu, nz, prefix + Integer.toString(i), cache));
        }

        if (solved.isEmpty() && zcache != null) {
            zcache.add(z); // no solutions for this z for level n, cache the result  
        }

        return solved;
    }

    // The input program has 18 instructions for each digit, manually converted
    // those and use only changing values from the real input.  Z is only variable
    // passed forward from digit to digit.
    static int run(List<Inst> alu, int z, int input) {
        int w = input;
        int x = z % 26;
        z = z / Integer.parseInt(alu.get(4).b);
        x = x + Integer.parseInt(alu.get(5).b);
        x = x == w ? 1 : 0;
        x = x == 0 ? 1 : 0;
        int y = 25;
        y = y * x;
        y = y + 1;
        z = z * y;
        y = w;
        y = y + Integer.parseInt(alu.get(15).b);
        y = y * x;
        z = z + y;
        return z;  // pass z forward
    }
}