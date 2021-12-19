package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 19
 */
public class D19 {

    record Scanner(Set<Vector> beacons, Vector location) {
        Scanner rotate(int[][] matrix) {
            return new Scanner(beacons.stream().map(b -> b.rotate(matrix)).collect(Collectors.toSet()), location);
        }
        Scanner move(Vector location) {
            return new Scanner(beacons.stream().map(b -> b.move(location)).collect(Collectors.toSet()), location);
        }
    }

    record Vector(int x, int y, int z) {
        Vector rotate(int[][] rm) {
            int rx = rm[0][0]*x + rm[0][1]*y + rm[0][2]*z;
            int ry = rm[1][0]*x + rm[1][1]*y + rm[1][2]*z;
            int rz = rm[2][0]*x + rm[2][1]*y + rm[2][2]*z;
            return new Vector(rx, ry, rz);
        }
        Vector move(Vector move) {
            return new Vector(x+move.x, y+move.y, z+move.z);
        }
        Vector vector(Vector b) {
            return new Vector(x-b.x, y-b.y, z-b.z);
        }
        int distance(Vector b) {
            return Math.abs((x-b.x)) + Math.abs((y-b.y)) + Math.abs((z-b.z));
        }
    }

    public static void main(String... args) throws IOException 
    {
        List<Scanner> scanners = Stream.of(Files.readString(Path.of("../input/19a_test.txt"))
                .split("--- scanner \\d+ ---")).filter(s -> !s.isBlank())
                .map(s -> parseScanner(s)).toList();

        List<Scanner> aligned = align(scanners);

        Set<Vector> beacons = aligned.stream().flatMap(s -> s.beacons.stream()).collect(Collectors.toSet());
        System.out.printf("part 1: %s\n", beacons.size());

        int maxDistance = aligned.stream().flatMapToInt(a -> aligned.stream()
            .mapToInt(b -> a.location.distance(b.location))).max().getAsInt();
        System.out.printf("part 2: %s\n", maxDistance);
    }

    static List<Scanner> align(List<Scanner> scanners) {
        List<Scanner> original = new ArrayList<>(scanners);
        List<Scanner> aligned = new ArrayList<>();
        aligned.add(original.remove(0));

        nextTry:
        while(!original.isEmpty()) { // Loop until all scanners aligned
            for (Scanner s : aligned) { // Use aligned scanners as source
                for (Scanner test: original) { // Test agains original list
                    for (Scanner orientation : orientations(test).toList()) { // test all orientations
                        Vector align = calculateAlignVector(s, orientation);
                        if (align != null) { // Found alignment
                            Scanner oa = orientation.move(align); // Move Scanner to origin
                            original.remove(test);
                            aligned.add(oa);
                            continue nextTry;
                        }
                    }
                }
            }            
        }
        return aligned;
    }

    // Calculate vector to align S2 with S1 and null if not possible
    static Vector calculateAlignVector(Scanner s1, Scanner s2) {
        List<Vector> betweenS1S2Beacons = s1.beacons.stream()
            .flatMap(b1 -> s2.beacons().stream().filter(b2 -> b1 != b2).map(b2 -> b1.vector(b2))).toList();

        Map<Integer,Long> x = betweenS1S2Beacons.stream().collect(Collectors.groupingBy(v -> v.x, Collectors.counting()));
        Map<Integer,Long> y = betweenS1S2Beacons.stream().collect(Collectors.groupingBy(v -> v.y, Collectors.counting()));
        Map<Integer,Long> z = betweenS1S2Beacons.stream().collect(Collectors.groupingBy(v -> v.z, Collectors.counting()));

        Map.Entry<Integer,Long> xMax = x.entrySet().stream().max((a,b) -> (int)(a.getValue()-b.getValue())).get();
        Map.Entry<Integer,Long> yMax = y.entrySet().stream().max((a,b) -> (int)(a.getValue()-b.getValue())).get();
        Map.Entry<Integer,Long> zMax = z.entrySet().stream().max((a,b) -> (int)(a.getValue()-b.getValue())).get();

        int minCount = Stream.of(xMax.getValue(), yMax.getValue(), zMax.getValue()).mapToInt(i -> i.intValue()).min().getAsInt();
        if (minCount >= 12) {
            return new Vector(xMax.getKey(), yMax.getKey(), zMax.getKey());
        } else {
            return null;
        }
    }

    // Produce all possible orientations of Scanner
    static Stream<Scanner> orientations(Scanner s) {
        Set<Scanner> res = new HashSet<>();
        for (int x = 0; x < 4; x++) {
            s = s.rotate(X);
            for (int y = 0; y < 4; y++) {
                s = s.rotate(Y);
                for (int z = 0; z < 4; z++) {
                    s = s.rotate(Z);
                    res.add(s);
                }
            }
        }
        return res.stream();
    }

    static Scanner parseScanner(String s) {
        return new Scanner(Stream.of(s.split("\n")).filter(b -> !b.isBlank())
            .map(b -> parseBeacon(b.split(","))).collect(Collectors.toSet()), new Vector(0,0,0));        
    }

    static Vector parseBeacon(String[] s) {
        return new Vector(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
    }

    // rotation matrixes
    static int[][] X = { { 1, 0, 0}, { 0, 0, -1}, { 0, 1, 0}, };
    static int[][] Y = { { 0, 0, 1}, { 0, 1, 0}, { -1, 0, 0}, };
    static int[][] Z = { { 0, -1, 0}, { 1, 0, 0}, { 0, 0, 1}, };
}