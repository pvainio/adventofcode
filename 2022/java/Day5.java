import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

record Move(int count, int from, int to) {
    static Move parse(String line) {
        var m = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)").matcher(line);
        m.matches();
        return new Move(parseInt(m.group(1)), parseInt(m.group(2))-1, parseInt(m.group(3))-1);
    }

    Stack[] apply(int count, Stack[] stacks) {
        var s = Arrays.copyOf(stacks, stacks.length);
        s[to] = s[to].add(s[from].get(count));
        s[from] = s[from].remove(count);
        return s;
    }
}

record Stack(List<Character> crates) {
    Stack add(Stack b) {
        return new Stack(Stream.concat(crates.stream(), b.crates.stream()).toList());
    }
    Stack get(int count) {
        return new Stack(crates.subList(crates().size()-count, crates().size()));
    }
    Stack remove(int count) {
        return new Stack(crates.subList(0, crates().size()-count));
    }
}

class Day5 {
    public static void main(String ... args) throws Exception {
        var input = Files.readString(Path.of("../input/day5.txt"));
        var parts = input.split("\n\n");
        var stacks = parseStacks(parts[0]);
        var moves = parseMoves(parts[1]);

        var part1 = crateMover9000(stacks, moves);
        System.out.printf("part 1: %s\n", part1);

        var part2 = crateMover9001(stacks, moves);
        System.out.printf("part 2: %s\n", part2);
    }

    static String crateMover9000(Stack[] stacks, List<Move> moves) {
        for (Move m : moves) {
            for (int i = 0; i < m.count(); i++) {
                stacks = m.apply(1, stacks);
            }
        }
        return result(stacks);
    }

    static String crateMover9001(Stack[] stacks, List<Move> moves) {
        for (Move m : moves) {
            stacks = m.apply(m.count(), stacks);
        }
        return result(stacks);
    }

    static String result(Stack[] stacks) {
        return Stream.of(stacks).map(e -> e.get(1).crates().get(0))
            .map(c -> "" + c).collect(Collectors.joining());
    }

    static Stack[] parseStacks(String input) {
        var lines = input.lines().filter(l -> !l.contains("1")).map(l -> parseLine(l)).toList();
        Stack[] stacks = new Stack[lines.get(0).length()];
        Arrays.fill(stacks, new Stack(List.of()));
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != ' ') {
                    stacks[i] = new Stack(List.of(line.charAt(i))).add(stacks[i]);
                }
            }
        }
        return stacks;
    }

    static String parseLine(String line) {
        return Pattern.compile(".(.)..?").matcher(line).results().map(r -> r.group(1))
            .collect(Collectors.joining());
    }

    static List<Move> parseMoves(String input) {
        return input.lines().map(Move::parse).toList();
    }
}

