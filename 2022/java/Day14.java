import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day14 {

    public static void main(String ... args) throws Exception {
        System.out.printf("part1: %d\n", pourSand(false));
        System.out.printf("part2: %d\n", pourSand(true));
    }

    static long pourSand(boolean haveFloor) throws Exception {
        var cave = readRocks(Files.readString(Path.of("../input/day14.txt")));
        int floor = haveFloor ? cave.keySet().stream().mapToInt(r -> r.y).max().getAsInt() + 2 : 0;
        long count = -1;
        while(count != countSand(cave)) {
            count = countSand(cave);
            dropSand(cave, floor);
        }
        return count;
    }

    static void dropSand(Map<XY,Entry> cave, int floor) {
        XY sand = new XY(500, 0), prev;
        do {
            prev = sand;
            XY old = sand;
            sand = moves.stream().map(m -> new XY(old.x + m.x, old.y + m.y))
                .filter(n -> !cave.containsKey(n)).findFirst().orElse(sand); // find next place for sand
        } while (prev != sand && sand.y + 1 != floor && sand.y < 999);

        if (cave.containsKey(new XY(sand.x, sand.y+1)) || sand.y + 1 == floor) {
            cave.put(sand, Entry.SAND); // We have support below, place sand
        }
    }

    static long countSand(Map<XY,Entry> cave) {
        return cave.values().stream().filter(e -> e == Entry.SAND).count();
    }

    static Map<XY,Entry> readRocks(String input) {
        return Arrays.stream(input.split("\n")).flatMap(l -> toRocks(l).stream())
            .collect(Collectors.toMap(r -> r, r -> Entry.ROCK, (a,b) -> a, () -> new HashMap<XY,Entry>()));
    }

    static List<XY> toRocks(String line) {
        String[] parts = line.split(" -> ");
        var res = new ArrayList<XY>();
        for (int i = 1; i < parts.length; i++) {
            res.addAll(draw(XY.parse(parts[i-1]), XY.parse(parts[i])).toList());
        }
        return res;
    }

    static Stream<XY> draw(XY p1, XY p2) {
        if (p1.equals(p2)) { return Stream.of(p1); }
        return Stream.concat(Stream.of(p1), draw(p1.moveTowards(p2), p2));
    }

    enum Entry{ROCK, SAND};

    record XY(int x, int y) {
        static XY parse(String input) {
            var parts = input.split(",");
            return new XY(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        XY moveTowards(XY o) {
            if (x == o.x && y < o.y) return new XY(x, y+1);
            if (x == o.x && y > o.y) return new XY(x, y-1);
            if (y == o.y && x < o.x) return new XY(x+1, y);
            if (y == o.y && x > o.x) return new XY(x-1, y);
            throw new IllegalStateException();
        }
    }

    static List<XY> moves = List.of(new XY(0, 1), new XY(-1, 1), new XY(1, 1));
 }