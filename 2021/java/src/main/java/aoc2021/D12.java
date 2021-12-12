package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * Advent of Code (AOC) 2021 Day 12
 */
public class D12 {

    public static void main(String ... args) throws IOException {

        Map<String,List<String>> caveToCaves = Files.lines(Path.of("../input/12a.txt"))
            .map(line -> line.split("-")) // split to cave a and b
            .flatMap(l -> Stream.of(l, new String[] {l[1], l[0]})) // build both directions a -> b and b -> a
            .collect(groupingBy(l -> l[0], mapping(l -> l[1], toList()))); // to map from cave to caves
        
        var paths1 = findPaths(List.of("start"), Map.of(), 1, caveToCaves).toList();

        System.out.printf("part 1: %s\n", paths1.size());

        var paths2 = findPaths(List.of("start"), Map.of(), 2, caveToCaves).toList();

        System.out.printf("part 2: %s\n", paths2.size());
    }

    static Stream<List<String>> findPaths(List<String> currentPath, Map<String,Integer> prevVisited, int maxSingleSmallVisits, Map<String,List<String>> caveToCaves) {
        String currentCave = currentPath.get(currentPath.size()-1);
        
        if (currentCave.equals("end")) {
            return Stream.of(currentPath);
        }

        var visited = new TreeMap<>(prevVisited);
        visited.merge(currentCave, 1, (k,v) -> v+1);

        return caveToCaves.get(currentCave).stream() // next caves
            .filter(next -> !alreadyMaxVisits(next, visited, maxSingleSmallVisits)) // filter out ones cannot be visited anymore
            .map(next -> appendToList(currentPath, next)) // build next path = current path + next cave
            .flatMap(path -> findPaths(path, Map.copyOf(visited), maxSingleSmallVisits, caveToCaves)); // solve next paths forward
    }

    static List<String> appendToList(List<String> path, String cave) {
        return Stream.concat(path.stream(), Stream.of(cave)).toList();
    }

    static boolean alreadyMaxVisits(String cave, Map<String,Integer> visited, int maxSingleSmallVisits) {
        if (isBigCave(cave) || !visited.containsKey(cave)) {
            return false; // big cave or never visited
        } else if (cave.equals("start") || cave.equals("end") || maxSingleSmallVisits < 2) {
            return true; // start or end can be visisted only once
        } else {
            // do we have any small cave already visited max times
            return visited.entrySet().stream()
                .anyMatch(e -> !isBigCave(e.getKey()) && e.getValue() >= maxSingleSmallVisits);
        }
    }

    static boolean isBigCave(String cave) {
        return Character.isUpperCase(cave.charAt(0));
    }
}