package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

/**
 * Advent of Code (AOC) 2021 Day 1
 */
public class D1 {

    public static void main(String ... args) throws Exception {

        Counter part1 = Files.lines(Path.of("../input/1a.txt"))
            .map(Integer::parseInt)
            .reduce(new Counter(), (c, d) -> c.apply(d) , (a,b) -> b);
        
        System.out.printf("part 1: %d\n", part1.count);

        CounterWithSlider part2 = Files.lines(Path.of("../input/1a.txt"))
            .map(Integer::parseInt)
            .reduce(new CounterWithSlider(), (c, d) -> c.accept(d), (a,b) -> b);
    
        System.out.printf("part 2: %d\n", part2.count);
    }

    record Counter(int prev, int count) {

        Counter() {
            this(Integer.MAX_VALUE, 0);
        }

        Counter apply(int depth) {
            return new Counter(depth, prev == Integer.MAX_VALUE || depth <= prev ? count : count+1);    
        }
    }

    record CounterWithSlider(int prev, int count, LinkedList<Integer> slider) {

        CounterWithSlider() {
            this(Integer.MAX_VALUE, 0, new LinkedList<>());
        }

        CounterWithSlider accept(int depth) {
            slider.add(depth);
            if (slider.size() < 3) {
                return new CounterWithSlider(prev, count, slider);
            } else if (slider.size() > 3) {
                slider.removeFirst();
            }
            int sum = slider.stream().mapToInt(Integer::intValue).sum();
            return new CounterWithSlider(sum, prev != Integer.MAX_VALUE && sum > prev ? count +1 : count, slider);
        }
    }
}