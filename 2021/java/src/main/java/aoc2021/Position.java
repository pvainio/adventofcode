package aoc2021;

record Position(int horizontal, int depth, int aim) {

    Position(int horizontal, int depth) {
       this(0, 0, 0);
    }
}

