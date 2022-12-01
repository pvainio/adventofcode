import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Day1 {
    public static void main(String ... args) throws Exception {
        var cals = new LinkedList<>(List.of(0));
        Files.readAllLines(Path.of("../input/day1.txt")).forEach(line -> {
            if (line.isBlank()) {
                cals.add(0);
            } else {
                cals.add(cals.removeLast() + Integer.parseInt(line));
            }
        });

        Collections.sort(cals, (a,b) -> b-a);  // reverse sort

        System.out.printf("part 1: %d\n", cals.getFirst());
        System.out.printf("part 2: %d\n", cals.stream().limit(3).mapToInt(i -> i).sum());
    }
}

    