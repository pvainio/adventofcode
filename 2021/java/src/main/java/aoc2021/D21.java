package aoc2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Advent of Code (AOC) 2021 Day 21 Dirac Dice
 */
public class D21 {

    record Player(int pos, int score) {
        Player move(int m) {
            int newPos = (this.pos + m - 1) % 10 + 1;
            return new Player(newPos, score + newPos);
        }
    }

    record State(int totalRolls, int prev, int prev2, Player p1, Player p2) {}

    static int rollPracticeDice(int rolls) {
        return (rolls + 1) % 100 - 1;
    }

    public static void main(String... args)
    {
        Player[] players = {new Player(8,0), new Player(6,0)};
        int totalRolls = 0;
        while(players[0].score < 1000 && players[1].score < 1000) { // until wins
            int player = (totalRolls / 3) % 2; // player in turn, index to players table
            int roll = 0;
            roll += rollPracticeDice(++totalRolls); // roll 1
            roll += rollPracticeDice(++totalRolls); // roll 2
            roll += rollPracticeDice(++totalRolls); // roll 3
            players[player] = players[player].move(roll); // move player and update players table
        }
        int losingScore = Arrays.stream(players).mapToInt(p -> p.score).min().getAsInt();
        System.out.printf("part 1: %s\n", losingScore * totalRolls);

        Map<State, long[]> cache = new HashMap<>();
        State s = new State(0, 0, 0,new Player(8,0), new Player(6,0));
        var res = play(s, cache);
        System.out.printf("part 2: %s\n", Math.max(res[0], res[1]));
    }

    // play state, return table of two, player 1 wins and player 2 wins
    static long[] play(State state, Map<State, long[]> cache) {
        long[] cached = cache.get(state);
        if (cached == null) {
            cached = new long[] {0,0};
            for (int i = 1; i <= 3; i++) {
                var lr = playRound(i, state, cache);
                cached[0] = cached[0]+lr[0];
                cached[1] = cached[1]+lr[1];
            }
            cache.put(state, cached);
        }
        return cached;
    }

    // play state with dice rolling given value, return table of two, player 1 wins and player 2 wins
    static long[] playRound(int currentRoll, State state, Map<State, long[]> cache) {
        boolean firstPlayer = (state.totalRolls / 3) % 2 == 0; // get the player based on dice rolls
        int playerRollNumber = state.totalRolls % 3; // number of rolls for current player

        Player p1 = state.p1;
        Player p2 = state.p2;
        if (playerRollNumber == 2) {
            // third roll for the player
            int totalRollSum = currentRoll + state.prev + state.prev2; // rolls sum
            Player moved = (firstPlayer ? state.p1 : state.p2).move(totalRollSum);

            if (moved.score >= 21) {
                return firstPlayer ? new long[]{1, 0} : new long[]{ 0, 1};
            }

            if (firstPlayer) {
                p1 = moved;
            } else {
                p2 = moved;
            } 
        }
        var newState = new State(state.totalRolls+1, currentRoll, state.prev, p1, p2);
        return play(newState, cache);
    }
}