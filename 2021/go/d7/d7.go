package d7

func CalculateMinCost(pos int, crab []int, costForDistance func(int) int) int {
	cost := CalculateCost(pos, crab, costForDistance)

	for CalculateCost(pos-1, crab, costForDistance) < cost {
		pos -= 1
		cost = CalculateCost(pos, crab, costForDistance)
	}

	for CalculateCost(pos+1, crab, costForDistance) < cost {
		pos += 1
		cost = CalculateCost(pos, crab, costForDistance)
	}

	return cost
}

func CalculateCost(pos int, crab []int, costForDistance func(int) int) int {
	fuel := 0
	for _, c := range crab {
		dist := pos - c
		if dist < 0 {
			dist = -dist
		}
		fuel += costForDistance(dist)
	}
	return fuel
}
