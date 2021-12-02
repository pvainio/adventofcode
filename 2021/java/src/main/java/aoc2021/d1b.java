package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class d1b {
    public static void main(String ... args) throws Exception {

        Counter counter = Files.lines(Path.of("../input/1a.txt"))
            .map(Integer::parseInt)
            .reduce(new Counter(), (c, d) -> c.accept(d), (a,b) -> b);
        
        System.out.println(counter.count);
    }

    record Counter(int prev, int count, LinkedList<Integer> slider) {

        Counter() {
            this(Integer.MAX_VALUE, 0, new LinkedList<>());
        }

        Counter accept(int depth) {
            slider.add(depth);
            if (slider.size() < 3) {
                return new Counter(prev, count, slider);
            } else if (slider.size() > 3) {
                slider.removeFirst();
            }
            int sum = slider.stream().mapToInt(Integer::intValue).sum();
            return new Counter(sum, prev != Integer.MAX_VALUE && sum > prev ? count +1 : count, slider);
        }
    }
}