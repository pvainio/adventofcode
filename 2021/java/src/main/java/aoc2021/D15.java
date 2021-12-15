package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 15
 */
public class D15 {

    record Point(int x, int y) {
        Point adj(int horizontal, int vertical) {
            return new Point(x+horizontal, y+vertical);
        }
    }

    public static void main(String... args) throws IOException {
        int[][] riskMap = Files.lines(Path.of("../input/15a.txt"))
            .map(line -> line.chars().map(c -> c-'0').toArray())
            .toArray(int[][]::new);

        long part1 = shortestPath(new Point(0,0), new Point(riskMap.length-1, riskMap[0].length-1), riskMap);
        System.out.printf("part 1: %d\n", part1);

        riskMap = createPart2Map(riskMap);

        long part2 = shortestPath(new Point(0,0), new Point(riskMap.length-1, riskMap[0].length-1), riskMap);
        System.out.printf("part 2: %d\n", part2);
    }

    static long shortestPath(Point from, Point to, int[][] riskMap) {
        int[][] leastRiskToPoint = new int[riskMap.length][riskMap[0].length]; // least known risk to a point
        Map<Point, Integer> visitRisk = new HashMap<>(Map.of(from, 0)); // Start with from point with 0 risk
        while(!visitRisk.isEmpty()) { // loop while we have points to visit
            Map.Entry<Point,Integer> current = visitRisk.entrySet().iterator().next();
            visitRisk.remove(current.getKey());
            long existingRiskToPoint = leastRiskToPoint[current.getKey().y][current.getKey().x];
            if (existingRiskToPoint == 0 || current.getValue() < existingRiskToPoint) { 
                // found new or better solution to current point
                leastRiskToPoint[current.getKey().y][current.getKey().x] = current.getValue();
                adjacentPoints(current.getKey(), riskMap).forEach(adjacent -> { // For each adjacent point
                    int risk = riskMap[adjacent.y][adjacent.x] + current.getValue(); // get risk for adjacent point
                    visitRisk.merge(adjacent, risk, (a,b) -> Math.min(a, b)); // merge it to visit map
                });
            }
        }

        return leastRiskToPoint[to.y][to.x];
    }

    static Stream<Point> adjacentPoints(Point p, int[][] map) {
        return Stream.of(p.adj(-1,0), p.adj(1,0), p.adj(0,-1), p.adj(0,1))
            .filter(n -> n.x >= 0 && n.y >= 0 && n.y < map.length && n.x < map[map.length-1].length);
    }

    static int[][] createPart2Map(int[][]map) {
        int[][] target = new int[map.length*5][map[0].length*5];
        for (int y=0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                fillMap(target, x, y, map);
            }
        }
        return target;
    }

    static void fillMap(int[][] target, int px, int py, int[][] source) {
        int height = source.length;
        int width = source[height-1].length;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int risk = source[y][x] + px + py;
                while(risk > 9) {
                    risk -= 9;
                }
                target[py*height + y][px*width+x] = risk;
            }
        }
    }
}