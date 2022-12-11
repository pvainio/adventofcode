import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Day11 {

    public static void main(String ... args) throws Exception {
        System.out.printf("part1: %d\n", monkeyBusiness(20, true));
        System.out.printf("part2: %d\n", monkeyBusiness(10_000, false));
    }

    static long monkeyBusiness(int rounds, boolean manageLevel) throws Exception {
        var monkeys = readMonkeys(Files.readString(Path.of("../input/day11.txt")));
        var commonMultiplier = monkeys.stream().mapToInt(m -> m.divisible).reduce(1, (a,b) -> a*b);
        long[] inspects = new long[monkeys.size()];

        for (int j = 0; j < rounds; j++) {
            for (int mi = 0; mi < monkeys.size(); mi++) {
                var m = monkeys.get(mi);
                for (var i : m.items) {
                    inspects[mi]++;
                    var level = m.worryLevel(i) / (manageLevel ? 3 : 1) % commonMultiplier;
                    var target = level % m.divisible == 0 ? m.trueTo : m.falseTo;
                    monkeys.get(target).items.add(level);
                }
                m.items.clear();
            }    
        }
        return Arrays.stream(inspects).mapToObj(i -> (Long)i).sorted((a,b) -> b.compareTo(a))
            .limit(2).reduce(1l, (a,b) -> a*b);
    }
 
    static List<Monkey> readMonkeys(String input) {
        return Arrays.stream(input.split("\n\n"))
            .map(Monkey::parse).collect(Collectors.toCollection(() -> new ArrayList<>()));
    }

    record Monkey (List<Long> items, String[] oper, int divisible, int trueTo, int falseTo) {
        static Monkey parse(String input) {
            var lines = input.split("\n");
            var items = Arrays.stream(lines[1].split(":")[1].split(","))
                .map(i -> Long.parseLong(i.trim())).collect(Collectors.toCollection(ArrayList::new));
            var operation = lines[2].split("= old ")[1].split(" ");
            var divisible = Integer.parseInt(lines[3].split("by ")[1]);
            var trueTo = Integer.parseInt(lines[4].split("monkey ")[1]);          
            var falseTo = Integer.parseInt(lines[5].split("monkey ")[1]); 
            return new Monkey(items, operation, divisible, trueTo, falseTo);         
        }

        long worryLevel(long level) {
            var val = oper[1].equals("old") ? level : Integer.parseInt(oper[1]);
            return switch (oper[0]) {
                case "*" -> level * val;
                case "+" -> level + val;
                default -> throw new IllegalArgumentException();
            };
        }
    }
}
