package aoc2021;

record Instruction(String dir, int amount) {
    static Instruction parse(String line) {
        String parts[] = line.split(" ");
        return new Instruction(parts[0], Integer.parseInt(parts[1]));
    }
}

