import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class Day4 {
    public static void main(String ... args) throws Exception {
        var input = Files.readString(Path.of("../input/day4.txt"));

        var pairs = input.lines().map(l -> Range.parsePair(l)).toList();

        var sum1 = pairs.stream()
            .filter(pair -> pair[0].fullyContains(pair[1]) || pair[1].fullyContains(pair[0]))
            .count();
        System.out.printf("part 1: %d\n", sum1);

        var sum2 = pairs.stream()
            .filter(pair -> pair[0].overlap(pair[1]) || pair[1].overlap(pair[0]))
            .count();
        System.out.printf("part 2: %d\n", sum2); 
    }
}

record Range (int start, int end) {
    static Range[] parsePair(String line) {
        return Stream.of(line.split(",")).map(s -> parse(s)).toArray(Range[]::new);
    }

    static Range parse(String range) {
        var ends = Stream.of(range.split("-")).mapToInt(s -> Integer.parseInt(s)).toArray();
        return new Range(ends[0], ends[1]);
    }

    boolean fullyContains(Range o) {
        return start <= o.start && end >= o.end;
    }
    
    boolean overlap(Range o) {
        return start <= o.start && end >= o.start;
    }
}