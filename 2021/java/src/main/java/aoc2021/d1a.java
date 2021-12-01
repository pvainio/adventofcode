package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class d1a {
    public static void main(String ... args) throws Exception {

        IntStream depths = Files.lines(Path.of("../input/1a.txt"))
            .mapToInt(Integer::parseInt);
        
        Counter c = new Counter();
        depths.forEach(c);
        System.out.println(c.count);
    }

    static class Counter implements IntConsumer {
        int count = 0;
        int prev = Integer.MAX_VALUE;
        @Override
        public void accept(int value) {
            if (prev != Integer.MAX_VALUE && value > prev) {
                count++;
            }
            prev = value;
        }
    }
}