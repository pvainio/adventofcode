import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day15 {

    public static void main(String ... args) throws Exception {
        var lines = Files.readString(Path.of("../input/day15.txt")).split("\n");
        var sensors = Arrays.stream(lines).map(Sensor::parse).toList();

        var count = rangesForY(sensors, 2000000).stream().mapToLong(r -> r.end() - r.start()).sum();
        System.out.printf("part1: %d\n", count);

        var freq = IntStream.range(0, 4000000)
            .mapToObj(y -> rangesForY(sensors, y))
            .filter(rs -> rs.size() > 1) // find the one hole in the middle, two ranges
            .map(rs -> rs.get(0)) // get the first range, location is after it end
            .map(r -> (r.end + 1)*4000000+r.y) // calculate the frequency
            .findFirst().get();
    
        System.out.printf("part2: %d\n", freq);
    }

    static List<Range> rangesForY(List<Sensor> sensors, int y) {
        var ranges = sensors.stream().flatMap(s -> {
            var beaconDist = Math.abs(s.loc.x - s.beacon.x) + Math.abs(s.loc.y - s.beacon.y);
            var dist = beaconDist - Math.abs(s.loc.y - y); // x distance for this sensor for this y
            return dist > 0 ? Stream.of(new Range(s.loc.x - dist, s.loc.x + dist, y)) : Stream.of();
        }).sorted((a,b) -> (int) (a.start()-b.start())).toList(); // sorted ranges
        // merge adjoining or overlapping ranges
        LinkedList<Range> res = new LinkedList<>(List.of(ranges.get(0)));
        for (var next : ranges.subList(1, ranges.size())) {
            var prev = res.getLast();
            if (prev.end() >= next.start() - 1) {
                res.removeLast();
                res.add(new Range(prev.start(), Math.max(prev.end(), next.end()), y));
            } else {
                res.add(next);
            }
        }
        return res;
    }
    
    record Range (long start, long end, long y) {}
    record XY(long x, long y) {}
    record Sensor(XY loc, XY beacon) {
        static Sensor parse(String line) {
            var m = Pattern.compile("=(-?\\d+)").matcher(line);
            var ns = m.results().mapToInt(r -> Integer.parseInt(r.group(1))).toArray();
            return new Sensor(new XY(ns[0], ns[1]), new XY(ns[2], ns[3]));
        }
    }
 }