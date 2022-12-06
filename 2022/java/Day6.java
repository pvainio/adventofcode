import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

class Day6 {
    public static void main(String ... args) throws Exception {
        var input = Files.readString(Path.of("../input/day6.txt"));
        System.out.printf("part 1: %d\n", findMarker(input, 4));
        System.out.printf("part 2: %d\n", findMarker(input, 14));
    }

    static int findMarker(String input, int len) {
        return IntStream.range(0, input.length()-len)
            .filter(i -> allDifferent(input.substring(i, i + len))).findFirst().getAsInt() + len;
    }

    static boolean allDifferent(String input) {
        return input.chars().distinct().count() == input.length();
    }
}

