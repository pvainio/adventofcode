package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 18
 */
public class D18 {
    record Number(Number left, Number right, int regular) {
        Number(Number left, Number right) {
            this(left, right, -1);
        }
        Number(int regular) {
            this(null, null, regular);
        }

        boolean pair() {
            return left != null && right != null;
        }
        Stream<Number> regularStream() { // Stream or regular numbers left to right order
            return pair() ? Stream.concat(left.regularStream(), right.regularStream()) : Stream.of(this);
        }
        int magnitude() {
            return pair() ? 3*left.magnitude() + 2*right.magnitude() : regular;
        }
    }

    public static void main(String... args) throws IOException 
    {
        List<Number> nrs = Files.lines(Path.of("../input/18a.txt")).map(l -> parse(l)).toList();
        Number part1 = nrs.stream().reduce((a,b) -> add(a, b)).get();

        System.out.printf("part 1: %d\n", part1.magnitude());

        var part2 = IntStream.range(0, nrs.size())
            .flatMap(i -> IntStream.range(0, nrs.size())
                .map(j -> add(nrs.get(i), nrs.get(j)).magnitude())).max().getAsInt();

        System.out.printf("part 2: %d\n", part2);
    }


    static Number add(Number a, Number b) {
        return reduce(new Number(a, b));
    }

    record Location(Number n, int depth) {}

    static Number explode(Number n) {
        // Traverse the number tree with depth to find out first item to explode and prev/next regulars
        Deque<Location> stack = new LinkedList<>();
        Location current = new Location(n, 0);

        Number previousRegular = null;
        Number toExplode = null;
        Number nextRegular = null;

        while(!stack.isEmpty() || current.n != null) {
            while(current.n != null) {
                stack.push(current);
                current = new Location(current.n.left, current.depth+1);
            }
            current = stack.pop();

            if (toExplode == null && !current.n.pair() && current.depth <= 4) {
                previousRegular = current.n; // update prev unless target to explode is found
            } else if (toExplode == null && current.n.pair() && current.depth == 4) {
                toExplode = current.n; // found target to explode
            } else if (nextRegular == null && !current.n.pair() && toExplode != null && current.n != toExplode.right) {
                nextRegular = current.n; // update next regular after target to explode is found
            }

            current = new Location(current.n.right, current.depth+1);
        }

        if (toExplode == null) {
            return n;
        }

        // Since using records, have to rebuild the whole tree
        return rebuild(n, previousRegular, toExplode, nextRegular, n);
    }

    static Number rebuild(Number n, Number prevRegular, Number toExplode, Number nextRegular, Number root) {
        if (n == toExplode) {
            return new Number(0);
        } else if (n == prevRegular) {
            return new Number(n.regular + toExplode.left.regular);
        } else if (n == nextRegular) {
            return new Number(n.regular + toExplode.right.regular);
        } else if (n.pair()) {
            return new Number(rebuild(n.left, prevRegular, toExplode, nextRegular, root), rebuild(n.right, prevRegular, toExplode, nextRegular, root));
        } else {
            return n;
        }
    }

    static Number split(Number n) {
        Number toSplit = n.regularStream().filter(r -> r.regular > 9).findFirst().orElse(null); // find first to split
        return split(n, toSplit);
    }

    static Number split(Number n, Number toSplit) {
        if (n.pair()) {
            return new Number(split(n.left, toSplit), split(n.right, toSplit));
        } else if (n == toSplit) {
            return new Number(new Number((int)Math.floor(n.regular/2.0)), new Number((int)Math.ceil(n.regular/2.0)));
        } else {
            return n;
        }
    }

    static Number reduce(Number original) {
        while(true) { // loop while no movement
            Number n = explode(original);
            if (!n.equals(original)) {
                original = n;
                continue; // so exploding while changes
            }
            n = split(n);
            if (n.equals(original)) {
                return n; // no change when splitting, ready
            }
            original = n;
        }
    }

    static Number parse(String s) {
        if (s.startsWith("[")) {
            s = s.substring(1, s.length()-1);
            int c=0;
            int pos=0;
            while(s.charAt(pos) != ',' || c > 0) {
                if (s.charAt(pos) == '[') {
                    c++;
                } else if (s.charAt(pos) == ']') {
                    c--;
                }
                pos++;
            }
            String l = s.substring(0, pos);
            String r = s.substring(pos+1, s.length());
            return new Number(parse(l), parse(r));
        } else {
            return new Number(Integer.parseInt(s));
        }
    }
}