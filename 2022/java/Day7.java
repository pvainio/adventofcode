import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day7 {
    public static void main(String ... args) throws Exception {
        var root = parseTerminal(Files.readString(Path.of("../input/day7.txt")));

        var part1 = root.dirs()
            .filter(n -> n.size() <= 100000)
            .mapToInt(n -> n.size()).sum();
        System.out.printf("part 1: %s\n", part1);

        var needed = 30000000 - (70000000 - root.size());
        var part2 = root.dirs()
            .filter(n -> n.size() >= needed)
            .mapToInt(n -> n.size()).sorted().findFirst().getAsInt();
        System.out.printf("part 2: %s\n", part2);
    }

    static Node parseTerminal(String input) {
        Node root = new Node(0, null, new TreeMap<>());
        input.lines().reduce(root, (n, l) -> n.apply(l), (a,b) -> {return null;});
        return root;
    }
}

record Node(int size, Node parent, Map<String,Node> nodes) {
    static Pattern filePattern = Pattern.compile("(\\d+) ([\\w.]+)");

    Node apply(String line) {
        if (line.startsWith("$ cd ")) {
            return cd(line.substring(5));
        }
        var fileMatcher = filePattern.matcher(line);
        if (fileMatcher.matches()) {
            return add(fileMatcher.group(2), fileMatcher.group(1));
        }
        return this;
    }

    Node cd(String name) {
        return switch (name) {
            case "/" -> parent == null ? this : parent.cd("/");
            case ".." -> parent;
            default -> nodes.computeIfAbsent(name, k -> new Node(0, this, new TreeMap<>()));
        };
    }

    Node add(String name, String size) {
        nodes.put(name, new Node(Integer.parseInt(size), this, new TreeMap<>()));
        return this;
    }

    Stream<Node> dirs() {
        return Stream.concat(Stream.of(this), nodes.values().stream().flatMap(n -> n.dirs())
            .filter(n -> n.size == 0));
    }

    public int size() {
        return size > 0 ? size : nodes.values().stream().mapToInt(n -> n.size()).sum();
    }
};