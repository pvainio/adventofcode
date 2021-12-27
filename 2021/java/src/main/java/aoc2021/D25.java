package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advent of Code (AOC) 2021 Day 25 Sea Cucumber
 */
public class D25 {

    record Location(int x, int y){}

    public static void main(String... args) throws IOException {

        List<String> data = Files.readAllLines(Path.of("../input/25a.txt"));
        Map<Location,Character> map = new HashMap<>();
        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                char ch = data.get(y).charAt(x);
                if (data.get(y).charAt(x) != '.') {
                    map.put(new Location(x,y), ch);    
                }
            }
        }

        boolean moving = true;
        int steps = 0;
        while(moving) {
            var next = move('>', map, data.get(0).length(), data.size());
            next = move('v', next, data.get(0).length(), data.size());
            steps++;
            moving = !next.equals(map);
            map = next;
        }
        System.out.printf("part 1: %s\n", steps);
    }


    static Map<Location,Character> move(char ch, Map<Location,Character> map, int width, int height) {
        Map<Location,Character> next = new HashMap<>();
        for (var e : map.entrySet()) {
            if (e.getValue() == ch) {
                var moveTo = ch == '>' ? new Location((e.getKey().x+1) % width, e.getKey().y) : new Location(e.getKey().x, (e.getKey().y+1)%height);
                if (!map.containsKey(moveTo)) {
                    next.put(moveTo, ch);
                } else {
                    next.put(e.getKey(), ch);
                }
            } else {
                next.put(e.getKey(), e.getValue());
            }
        }
        return next;
    }
}