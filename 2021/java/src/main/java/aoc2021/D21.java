package aoc2021;

import java.util.HashMap;
import java.util.Map;

/**
 * Advent of Code (AOC) 2021 Day 21 Dirac Dice
 */
public class D21 {

    record Player(int pos, int score) {
        Player move(int m) {
            int newPos = (this.pos + m - 1) % 10 + 1; // calculate new position
            return new Player(newPos, score + newPos);
        }
    }

    record State(int totalRolls, int prevRoll, int prevRoll2, Player p1, Player p2) {
        boolean gameOver(int score) {
            return p1.score >= score || p2.score >= score;
        }
        int playerInTurn() {
            return (totalRolls / 3) % 2;
        }
        State roll(int currentRoll) {
            int playerRollNumber = totalRolls % 3; // number of roll for current player
            Player p1 = this.p1;
            Player p2 = this.p2;
            if (playerRollNumber == 2) { // third roll for the player, move it
                int totalRollSum = currentRoll + prevRoll + prevRoll2; // rolls sum
                if (playerInTurn() == 0) {
                    p1 = this.p1.move(totalRollSum);
                } else {
                    p2 = this.p2.move(totalRollSum);
                }
            }
            return new State(totalRolls+1, currentRoll, prevRoll, p1, p2);
        }
    }

    static State rollPracticeDice(State state) {
        int currentRoll = (state.totalRolls) %100 + 1;
        return state.roll(currentRoll);
    }

    public static void main(String... args)
    {
        State state = new State(0, 0, 0, new Player(8,0), new Player(6,0));
        while(!state.gameOver(1000)) { // until wins
            state = rollPracticeDice(state);
        }
        int losingScore = Math.min(state.p1.score, state.p2.score);
        System.out.printf("part 1: %s\n", losingScore * state.totalRolls);

        Map<State, long[]> cache = new HashMap<>();
        state = new State(0, 0, 0, new Player(8,0), new Player(6,0));
        var res = play(state, cache);
        System.out.printf("part 2: %s\n", Math.max(res[0], res[1]));
    }

    // play state, return array of two, player 1 wins and player 2 wins
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

    // play state with dice rolling given value, return array of two, player 1 wins and player 2 wins
    static long[] playRound(int currentRoll, State state, Map<State, long[]> cache) {
        state = state.roll(currentRoll);
        if (state.gameOver(21)) {
            return state.playerInTurn() == 0 ? new long[]{1,0} : new long[]{0,1};
        }
        return play(state, cache);
    }
}