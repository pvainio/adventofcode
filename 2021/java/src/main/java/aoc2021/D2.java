package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Advent of Code (AOC) 2021 Day 2
 */
public class D2 {
    
    public static void main(String ... args) throws Exception {

        var pos = Files.lines(Path.of("../input/2a.txt"))
            .map(line -> Instruction.parse(line))
            .reduce(new Position(0, 0), (p, i) -> switch(i.dir()) {
                case "forward" -> new Position(p.horizontal() + i.amount(), p.depth(), p.aim());
                case "down" -> new Position(p.horizontal(), p.depth() + i.amount(), p.aim());
                case "up" -> new Position(p.horizontal(), p.depth() - i.amount(), p.aim());
                default -> throw new IllegalArgumentException("invalid instruction " + i.dir());
            }, (a,b) -> b);
               
        System.out.printf("part 1: %d\n", pos.depth() * pos.horizontal());

        pos = Files.lines(Path.of("../input/2a.txt"))
            .map(line -> Instruction.parse(line))
            .reduce(new Position(0, 0, 0), (p, i) -> switch(i.dir()) {
                case "forward" -> new Position(p.horizontal() + i.amount(), p.depth() + i.amount() * p.aim(), p.aim());
                case "down" -> new Position(p.horizontal(), p.depth(), p.aim() + i.amount());
                case "up" -> new Position(p.horizontal(), p.depth(), p.aim() - i.amount());
                default -> throw new IllegalArgumentException("invalid instruction " + i.dir());
            }, (a,b) -> b);

        System.out.printf("part 2: %d\n", pos.depth() * pos.horizontal());
    }

    record Instruction(String dir, int amount) {
        static Instruction parse(String line) {
            String parts[] = line.split(" ");
            return new Instruction(parts[0], Integer.parseInt(parts[1]));
        }
    }
    
    record Position(int horizontal, int depth, int aim) {
        Position(int horizontal, int depth) {
           this(0, 0, 0);
        }
    }
}