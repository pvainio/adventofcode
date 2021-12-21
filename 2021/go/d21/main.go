package main

import (
	"fmt"
)

type Player struct {
	pos, score int
}

type State struct {
	totalRolls, prevRoll, prevRoll2 int
	players                         [2]Player
}

// Advent of Code (AOC) 2021 Day 21 Dira Dice
func main() {
	state := State{players: [2]Player{{pos: 8}, {pos: 6}}}
	for !state.gameOver(1000) {
		state = state.rollPracticeDice()
	}

	losingScore := state.players[0].score
	if state.players[1].score < losingScore {
		losingScore = state.players[1].score
	}
	fmt.Printf("part 1: %v\n", losingScore*state.totalRolls)

	state = State{players: [2]Player{{pos: 8}, {pos: 6}}}
	cache := map[State][2]int64{}
	res := playRecursiveUniverses(state, cache)

	max := res[0]
	if res[1] > max {
		max = res[1]
	}

	fmt.Printf("part 2: %v\n", max)
}

// return number of wins as array player1, player2
func playRecursiveUniverses(state State, cache map[State][2]int64) [2]int64 {
	res, ok := cache[state]
	if !ok {
		for i := 1; i <= 3; i++ { // play Dira Dice all faces 1,2,3 and count winners together
			lr := playRound(i, state, cache)
			res[0] += lr[0]
			res[1] += lr[1]
		}
		cache[state] = res
	}
	return res
}

// return number of wins as array player1, player2
func playRound(currentRoll int, state State, cache map[State][2]int64) [2]int64 {
	state = state.roll(currentRoll)

	if state.gameOver(21) { // We have winner, return it as array 0,1 or 1,0
		currentPlayer := (state.totalRolls / 3) % 2
		if currentPlayer == 0 {
			return [2]int64{1, 0}
		} else {
			return [2]int64{0, 1}
		}
	}
	return playRecursiveUniverses(state, cache)
}

func (state State) gameOver(score int) bool {
	return state.players[0].score >= score || state.players[1].score >= score
}

func (state State) rollPracticeDice() State {
	currentRoll := (state.totalRolls)%100 + 1
	return state.roll(currentRoll)
}

func (state State) roll(currentRoll int) State {
	playerRollNumber := state.totalRolls % 3 // number of roll for current player
	currentPlayer := (state.totalRolls / 3) % 2
	state.totalRolls += 1

	if playerRollNumber == 2 { // third roll for the player, move it
		totalRollSum := currentRoll + state.prevRoll + state.prevRoll2 // rolls sum
		moved := state.players[currentPlayer].move(totalRollSum)
		state.players[currentPlayer] = moved
	}
	state.prevRoll, state.prevRoll2 = currentRoll, state.prevRoll
	return state
}

func (p Player) move(move int) Player {
	pos := (p.pos+move-1)%10 + 1
	score := pos + p.score
	return Player{pos, score}
}
