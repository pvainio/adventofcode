package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Advent of Code (AOC) 2021 Day 13
 */
public class D13 {

    static final String FOLD_ALONG = "fold along ";

    public static void main(String ... args) throws IOException {
        List<String> data = Files.lines(Path.of("../input/13a.txt")).filter(l -> !l.isBlank()).toList();

        List<Dot> paper = data.stream().filter(l -> !l.startsWith(FOLD_ALONG)).map(Dot::parse).toList();
        List<Fold> folds = data.stream().filter(l -> l.startsWith(FOLD_ALONG)).map(Fold::parse).toList();

        paper = folds.get(0).apply(paper);
        System.out.printf("part 1: %s\n", paper.size());

        paper = folds.stream().skip(1)
            .reduce(paper, (a,b) -> b.apply(a), (a,b) -> b);
        System.out.printf("part 2:\n");
        print(paper);
    }

    record Dot(int x, int y) {
        static Dot parse(String dot) {
            String[] parts = dot.split(",");
            return new Dot(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    record Fold(char axle, int pos) {
        static Fold parse(String fold) {
            String parts[] = fold.replace(FOLD_ALONG, "").split("=");
            return new Fold(parts[0].charAt(0), Integer.parseInt(parts[1]));
        }

        List<Dot> apply(List<Dot> paper) {
            return paper.stream().map(p -> apply(p)).distinct().toList();
        }

        Dot apply(Dot d) {
            if (axle == 'x' && d.x > pos) {
                return new Dot(2*pos-d.x, d.y);
            } else if (axle == 'y' && d.y > pos) {
                return new Dot(d.x, 2*pos-d.y);
            }
            return d;
        }
    }

    static void print(List<Dot> paper) {
        int height = paper.stream().mapToInt(d -> d.y).max().getAsInt();
        int width = paper.stream().mapToInt(d -> d.x).max().getAsInt();
        for (int y = 0; y <= height; y++) {
            for(int x = 0; x <= width; x++) {
                System.out.print(charAt(x, y, paper));
            }
            System.out.println();
        }
    }

    static String charAt(int x, int y, List<Dot> paper) {
        return paper.stream().anyMatch(d -> d.x == x && d.y == y) ? "█" : " ";
    }
}