package aoc2021;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class D3a {

    public static void main(String ... args) throws Exception {
        List<String> data = Files.lines(Path.of("../input/3a.txt")).collect(Collectors.toList());
        var lines = data.size();
        var lineLen = data.get(0).length();
        var ones = data.stream().reduce(new int[lineLen], (prev, line) -> {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '1') {
                    prev[i]++;
                }
            }
            return prev;
        }, (a,b) -> b);

        String gamma = "";
        String epsilon = "";
        for (var one : ones) {
            var zero = lines - one;
            if (one > zero) {
                gamma += "1";
                epsilon += "0";
            } else {
                gamma += "0";
                epsilon += "1";
            }
        }
        int gammaRate = Integer.parseInt(gamma, 2);
        int epsilonRate = Integer.parseInt(epsilon, 2);
        System.out.printf("%d %d %d\n", gammaRate, epsilonRate, gammaRate * epsilonRate);
    }
}