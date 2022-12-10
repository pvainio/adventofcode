import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day10 {

    public static void main(String ... args) throws Exception {
        var program = parseProgram(Files.readString(Path.of("../input/day10.txt")));
        System.out.printf("part1: %d\n", part1(program));
        System.out.printf("part2:\n%s", part2(program));
    }

    static String part2(List<Instruction> program) {
        return IntStream.range(1, 6*40+1)
            .mapToObj(cycle -> pixelAtCycle(cycle, program) + (cycle%40 == 0 ? "\n" : ""))
            .collect(Collectors.joining());
    }

    static String pixelAtCycle(int cycle, List<Instruction> program) {
        var middle = getState(cycle, program).x;
        var sprite = Set.of(middle, middle-1, middle+1);
        return sprite.contains((cycle-1) % 40) ? "#" : " ";
    }
    
    static int part1(List<Instruction> program) {
        return IntStream.of(20,60,100,140,180,220).map(c -> c * getState(c, program).x).sum();
    }
    
    static State getState(int atCycle, List<Instruction> program) {
        var state = new State(1, 1);
        for (var i : program) {
            var newState = run(i, state);
            if (newState.cycle > atCycle) {
                break;
            }
            state = newState;
        }
        return state;
    }

    static State run(Instruction i, State state) {
        return switch (i.op) {
            case "noop" -> new State(state.cycle+1, state.x);
            case "addx" -> new State(state.cycle+2, state.x + i.value);
            default -> throw new IllegalArgumentException("invalid operation");
        };
    }

    static List<Instruction> parseProgram(String input) {
        return input.lines().map(l -> Instruction.parse(l.split(" "))).toList();
    }

    record State(int cycle, int x) {}

    record Instruction(String op, int value) {
        static Instruction parse(String ... parts) {
            return new Instruction(parts[0], parts[0].equals("noop") ? 0 : Integer.parseInt(parts[1]));
        }
    }
}
