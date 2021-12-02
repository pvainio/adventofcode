package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;

public class d2a {

    public static void main(String ... args) throws Exception {

        Position pos = new Position(0, 0);

        pos = Files.lines(Path.of("../input/2a.txt"))
            .map(line -> Instruction.parse(line))
            .reduce(pos, (p, i) -> switch(i.dir()) {
                case "forward" -> new Position(p.horizontal() + i.amount(), p.depth(), p.aim());
                case "down" -> new Position(p.horizontal(), p.depth() + i.amount(), p.aim());
                case "up" -> new Position(p.horizontal(), p.depth() - i.amount(), p.aim());
                default -> throw new IllegalArgumentException("invalid instruction " + i.dir());
            }, (a,b) -> b);
               
        System.out.printf("%d %d %d\n", pos.horizontal(), pos.depth(), pos.depth() * pos.horizontal());
    }
}