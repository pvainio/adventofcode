package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import aoc2021.D2a.Position;
import aoc2021.D2a.Instruction;

/**
 * Advent of Code (AOC) 2021 Day 2 part 2
 */
public class D2b {

    public static void main(String ... args) throws Exception {

        Position pos = new Position(0, 0, 0);

        pos = Files.lines(Path.of("../input/2a.txt"))
            .map(line -> Instruction.parse(line))
            .reduce(pos, (p, i) -> switch(i.dir()) {
                case "forward" -> new Position(p.horizontal() + i.amount(), p.depth() + i.amount() * p.aim(), p.aim());
                case "down" -> new Position(p.horizontal(), p.depth(), p.aim() + i.amount());
                case "up" -> new Position(p.horizontal(), p.depth(), p.aim() - i.amount());
                default -> throw new IllegalArgumentException("invalid instruction " + i.dir());
            }, (a,b) -> b);

        System.out.printf("%d %d %d\n", pos.horizontal(), pos.depth(), pos.depth() * pos.horizontal());
    }
}