package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Advent of Code (AOC) 2021 Day 5 part 1
 */
public class D5a {

    record Point(int x, int y) {
        static Point parse(String str) {
            var parts = str.split(",");
            return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    record Line(Point p1, Point p2) {
        static Line parse(String str) {
            var parts = str.split(" -> ");
            return new Line(Point.parse(parts[0]), Point.parse(parts[1]));
        }
    }

    private static List<Integer> range(int x1, int x2) {
        if (x1 < x2) {
            return IntStream.rangeClosed(x1, x2).boxed().toList();
        } else {
            var l = IntStream.rangeClosed(x2, x1).boxed().collect(Collectors.toList());
            Collections.reverse(l);
            return l;
        }
    }

    private static void plot(Map<Integer,Map<Integer,Integer>> coords, int x, int y) {
        var row = coords.computeIfAbsent(y, ys -> new TreeMap<>());
        row.compute(x, (k, v) -> (v == null) ? 1 : v + 1);
    }

    static void plot(Map<Integer,Map<Integer,Integer>> coords, Line line) {

        var xs = range(line.p1.x, line.p2.x);
        var ys = range(line.p1.y, line.p2.y);

        int len = Math.max(xs.size(), ys.size());

        for (int i = 0; i < len; i++) {
            int x = i < xs.size() ? xs.get(i) : xs.get(xs.size()-1);
            int y = i < ys.size() ? ys.get(i) : ys.get(ys.size()-1);
            plot(coords, x, y);
        }
    }

    static long sum(Map<Integer,Map<Integer,Integer>> coords) {
       return coords.values().stream().flatMap(r -> r.values().stream())
            .filter(v -> v > 1)
            .count();
    }

    public static void main(String ... args) throws Exception {

        var data = Files.lines(Path.of("../input/5a.txt"))
            .map(Line::parse)
            .filter(l -> l.p1.x == l.p2.x || l.p1.y == l.p2.y)
            .toList();

        Map<Integer,Map<Integer,Integer>> coords = new TreeMap<>();
        data.forEach(l -> plot(coords, l));

        System.out.printf("%d\n", sum(coords));
    }
}