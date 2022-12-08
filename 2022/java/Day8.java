import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day8 {

    public static void main(String ... args) throws Exception {
        var map = parseMap(Files.readString(Path.of("../input/day8.txt")));

        System.out.printf("part 1: %s\n", countVisible(map));
        System.out.printf("part 2: %s\n", highestScenicScore(map));
    }

    static int highestScenicScore(int[][] map) {
        return IntStream.range(0, map.length)
            .flatMap(y -> IntStream.range(0, map[y].length)
                .map(x -> scenicScore(map, new Point(x, y)))).max().getAsInt();
    }

    static int scenicScore(int[][] map, Point op) {
        var up = visibleTrees(map, op, p -> p.move(0, -1));
        var down = visibleTrees(map, op, p -> p.move(0, 1));
        var left = visibleTrees(map, op, p -> p.move(-1, 0));
        var right = visibleTrees(map, op, p -> p.move(1, 0));

        return up * down * left * right;
    }

    static int visibleTrees(int[][] map, Point op, Function<Point,Point> move) {
        int count = 0;
        for (var p = move.apply(op); p.inside(map); p = move.apply(p)) {
            count++;
            if (map[p.y()][p.x()] >= map[op.y()][op.x()]) {
                return count;
            }
        }
        return count;
    }

    static long countVisible(int[][] map) {
        return IntStream.range(0, map.length)
            .flatMap(y -> IntStream.range(0, map[y].length).filter(x -> visible(map, y, x))).count();
    }

    static boolean visible(int[][] map, int y, int x) {
        if (y == 0 || y == map.length-1 || x == 0 || x == map[y].length-1) {
            return true; // edge cases
        }
        var height = map[y][x];
        return Stream.of(
            IntStream.of(map[y]).limit(x).max(), // left max height
            IntStream.of(map[y]).skip(x+1).max(), // right max
            IntStream.of(col(map,x)).limit(y).max(), // up
            IntStream.of(col(map,x)).skip(y+1).max()) // down
            .mapToInt(m -> m.getAsInt()).anyMatch(h -> h < height); // any shorter than given tree
    }

    static int[] col(int[][] map, int x) {
        return IntStream.range(0, map.length).map(y -> map[y][x]).toArray();
    }

    static int[][] parseMap(String input) {
        return input.lines().map(l -> l.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
    }
}

record Point(int x, int y) {
    Point move(int mx, int my) { return new Point(x + mx, y + my);}
    boolean inside(int[][] map) { return y >= 0 && y < map.length && x >= 0 && x < map[y].length; }
}