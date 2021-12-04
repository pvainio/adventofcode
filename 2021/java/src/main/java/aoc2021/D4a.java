package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class D4a {

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
                .mapToInt(i -> i.intValue())
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

        String[] numbers = data.get(0).split(",");

        Board winner = play(boards, numbers);

        int sum = winner.numbers.stream().flatMap(r -> r.stream())
            .filter(n -> n > 0)
            .mapToInt(i -> i.intValue())
            .sum();

        int last = winner.marks.getLast();

        System.out.printf("%d %d %d\n", sum, last, last * sum);
    }

    static Board play(List<Board> boards, String[] numbers) {
        for (String number : numbers) {
            Integer n = Integer.parseInt(number);
            for (Board b : boards) {
                if(b.mark(n)) {
                    return b;
                }
            }
        }
        return null;
    }
}