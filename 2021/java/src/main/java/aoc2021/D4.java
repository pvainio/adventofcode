package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 4
 */
public class D4 {

    public record Board(List<List<Integer>> numbers, LinkedList<Integer> marks) {
        Board() {
            this(new ArrayList<>(), new LinkedList<>());
        }

        boolean mark(Integer n) {
            for(int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (numbers.get(r).get(c) == n) {
                        numbers.get(r).set(c, -1);
                    }
                }
            }
            marks.add(n);
            return bingo();
        }

        boolean bingo() {
            for(int i = 0; i < 5; i++) {
                int rcount = 0;
                int ccount = 0;
                for (int j = 0; j < 5; j++) {
                    if (numbers.get(i).get(j) == -1) {
                        rcount++;
                    }
                    if (numbers.get(j).get(i) == -1) {
                        ccount++;
                    }
                }
                if (rcount == 5 || ccount == 5) {
                    return true;
                }
            }
            return false;
        }

        int sumUnmarked() {
            return numbers.stream().flatMap(r -> r.stream())
                .filter(n -> n > 0)
                .mapToInt(i -> i)
                .sum();
    
        }
    }

    static List<Board> parseBoards(Stream<String> data) {
        LinkedList<Board> boards = new LinkedList<>();
        data.reduce(boards, (bs, l) -> {
            if (l.isBlank()) {
                bs.add(new Board());
                return bs;
            }
            String[] parts = l.split("\\s+");
            List<Integer> row = Arrays.asList(parts).stream().filter(s -> !s.isBlank()).map(Integer::parseInt).toList();
            Board b = bs.getLast();
            b.numbers().add(new ArrayList<>(row));
            return bs;
        }, (a,b) -> b);
        return boards;
    }

    public static void main(String ... args) throws Exception {
        List<String> data = Files.lines(Path.of("../input/4a.txt")).toList();

        List<Board> boards = parseBoards(data.stream().skip(1));
        List<Integer> numbers = Arrays.stream(data.get(0).split(",")).map(Integer::parseInt).toList();

        Board winner1 = play(boards, numbers);

        int sum = winner1.numbers.stream().flatMap(row -> row.stream())
            .filter(number -> number > 0)
            .mapToInt(i -> i)
            .sum();

        int last = winner1.marks.getLast();

        System.out.printf("part 1: %d\n", last * sum);

        boards = parseBoards(data.stream().skip(1));
        Board winner2 = null;
        while(!boards.isEmpty()) {
            winner2 = play(boards, numbers);
            boards.remove(winner2);
        }

        int sum2 = winner2.sumUnmarked();
        int last2 = winner2.marks().get(winner2.marks().size()-1);

        System.out.printf("part 2: %d\n", last2 * sum2);
    }

    /** Play numbers and return winning board. */
    static Board play(List<Board> boards, List<Integer> numbers) {
        return numbers.stream()
            .flatMap(number -> boards.stream().filter(b -> b.mark(number)))
            .findFirst().orElse(null);
    }
}