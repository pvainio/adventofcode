package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class D4b {

    public static void main(String ... args) throws Exception {
        List<String> data = Files.lines(Path.of("../input/4a.txt")).toList();

        List<D4a.Board> boards = D4a.parseBoards(data.stream().skip(1));

        String[] numbers = data.get(0).split(",");

        D4a.Board winner = null;
        while(!boards.isEmpty()) {
            winner = D4a.play(boards, numbers);
            boards.remove(winner);
        }

        int sum = winner.sumUnmarked();
        int last = winner.marks().get(winner.marks().size()-1);

        System.out.printf("%d %d %d\n", sum, last, last * sum);
    }
}