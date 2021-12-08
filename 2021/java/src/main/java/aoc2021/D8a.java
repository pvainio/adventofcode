package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Advent of Code (AOC) 2021 Day 8 part 1
 */
public class D8a {

    public static void main(String ... args) throws Exception {

        var count = Files.lines(Path.of("../input/8a.txt"))
            .map(l -> l.split("\\|"))
            .map(parts -> List.of(List.of(parts[0].split(" ")), List.of(parts[1].split(" "))) )
            .flatMap(parts -> parts.get(1).stream())
            .filter(digit -> isSimple(digit))
            .count();
        System.out.printf("%d\n", count);
    }

    static boolean isSimple(String digit) {
        System.out.printf("%s %d\n", digit, digit.length());
        return switch(digit.length()) {
            case 2 -> true;
            case 3 -> true;
            case 4 -> true;
            case 7 -> true;
            default -> false;
        };
    }
}