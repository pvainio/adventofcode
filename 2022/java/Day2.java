import java.nio.file.Files;
import java.nio.file.Path;

class Day2 {

    static int[] toWin = { 1, 2, 0 };  //for  0/Rock -> 1/Paper,    1/Paper -> 2/Scissors, 2/Scissors -> 0/Rock
    static int[] toLose = { 2, 0, 1 }; //for  0/Rock -> 2/Scissors, 1/Paper -> 0/Rock    , 2/Scissors -> 1/Paper

    public static void main(String ... args) throws Exception {
        var input = Files.readString(Path.of("../input/day2.txt"));
        var score = input.lines()
            .map(line -> game1(line))
            .mapToInt(game -> score(game)).sum();
        System.out.printf("part 1: %d\n", score);

        var score2 = input.lines()
            .map(line -> game2(line))
            .mapToInt(game -> score(game)).sum();
        System.out.printf("part 2: %d\n", score2);
    }

    static int[] game1(String line) {
        return new int[] { line.charAt(0) - 'A', line.charAt(2)-'X' };
    }

    static int[] game2(String line) {
        int shape1 = line.charAt(0)-'A';
        int shape2 = switch (line.charAt(2)) {
            case 'X' -> toLose[shape1];
            case 'Z' -> toWin[shape1];
            default -> shape1;
        };
        return new int[] { shape1, shape2 };
    }

    static int score(int[] game) {
        var shapeScore = game[1] + 1;
        return shapeScore + gameScore(game[0], game[1]);
    }

    static int gameScore(int shape1, int shape2) {
        if (toWin[shape1] == shape2) return 6;
        if (toLose[shape1] == shape2) return 0;
        else return 3;
    }
}

    