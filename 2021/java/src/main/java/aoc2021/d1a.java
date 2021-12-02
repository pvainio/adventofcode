package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;

public class d1a {

    record Counter(int prev, int count) {

        Counter() {
            this(Integer.MAX_VALUE, 0);
        }

        Counter apply(int depth) {
            return new Counter(depth, prev == Integer.MAX_VALUE || depth <= prev ? count : count+1);    
        }
    }

    public static void main(String ... args) throws Exception {

        Counter counter = Files.lines(Path.of("../input/1a.txt"))
            .map(Integer::parseInt)
            .reduce(new Counter(), (c, d) -> c.apply(d) , (a,b) -> b);
        
        System.out.println(counter.count);
    }
}