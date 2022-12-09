import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day9 {

    public static void main(String ... args) throws Exception {
        var moves = parseMoves(Files.readString(Path.of("../input/day9.txt")));
        var head = tieKnots(10);
        head = runMoves(moves, head);
        System.out.printf("part 1: %s\n", nth(head, 1).history.stream().distinct().count());
        System.out.printf("part 2: %s\n", nth(head, 9).history.stream().distinct().count());
    }

    static Knot nth(Knot k, int count) {
        return count == 0 ? k : nth(k.tail, count-1);
    }

    static Knot runMoves(List<Move> moves, Knot head) {
        return moves.stream().reduce(head, (k, m) -> k.apply(m), (a,b) -> {return a;});
    }

    static Knot tieKnots(int count) {
        return new Knot(new XY(0,0), Set.of(new XY(0,0)), count > 1 ? tieKnots(count-1): null);
    }

    static List<Move> parseMoves(String input) {
        return input.lines()
            .map(l -> new Move(vector.get(l.charAt(0)), Integer.parseInt(l.substring(2))))
            .toList();
    }

    record Knot(XY pos, Set<XY> history, Knot tail) {
        Knot apply(Move m) {
            return IntStream.range(0, m.steps).mapToObj(i -> m)
                .reduce(this, (k, move) -> k.move(move.vector), (a,b) -> {return a;});
        }

        Knot move(XY vector) {
            var newPos = pos.move(vector);
            var newHistory = Stream.concat(history.stream(), Stream.of(newPos)).collect(Collectors.toSet());
            return new Knot(newPos, newHistory, tail != null ? tail.follow(newPos) : null);
        }

        Knot follow(XY head) {
            if (head.touching(pos)) {
                return this;
            }
            var dx = (head.x-pos.x)/2 + (head.x-pos.x)%2;
            var dy = (head.y-pos.y)/2 + (head.y-pos.y)%2;
            return move(new XY(dx, dy));
        }
    }

    record Move(XY vector, int steps) {}
    
    record XY(int x, int y) {
        XY move(XY vector) {
            return new XY(x + vector.x, y + vector.y);
        }
        boolean touching(XY o) {
            return Math.abs(x-o.x) <= 1 && Math.abs(y-o.y) <= 1;
        }
    }
    
    static Map<Character,XY> vector = Map.of(
        'U', new XY(0, -1), 'D', new XY(0, +1),
        'L', new XY(-1, 0), 'R', new XY(+1, 0));
}
