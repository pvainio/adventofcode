import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day3d {
    public static void main(String ... args) throws Exception {
        var input = Files.readString(Path.of("../input/day3.txt"));
        var sum = input.lines().mapToInt(l -> {
            var p1 = l.substring(0, l.length()/2);
            var p2 = l.substring(l.length()/2, l.length());
            return commonPriority(List.of(p1, p2));
        }).sum();
        System.out.printf("part 1: %d\n", sum);

        var counter = IntStream.range(0, input.length()).iterator();
        var sum2 = input.lines()
            .collect(Collectors.groupingBy(c -> counter.nextInt()/3)).values().stream()
            .mapToInt(l -> commonPriority(l)).sum();
        System.out.printf("part 2: %d\n", sum2); 
    }

    static int commonPriority(List<String> strings) {
        // common character in list of strings
        var c = strings.stream().reduce((s1, s2) -> commonChars(s1, s2)).get().charAt(0);
        return c < 'a' ? c - 'A' + 27 : c - 'a' + 1; // convert to priority
    }

    static String commonChars(String a, String b) {
        return a.chars().filter(i1 -> b.chars().anyMatch(i2 -> i1 == i2)).distinct()
            .mapToObj(c -> Character.toString(c)).collect(Collectors.joining());
    }
}