package d9

type Point struct {
	X, Y int
}

func LowPoints(hmap []string) []Point {
	lowPoints := []Point{}
	for y, row := range hmap {
		for x := range row {
			adjacentMin := min(Height(x-1, y, hmap), Height(x+1, y, hmap), Height(x, y-1, hmap), Height(x, y+1, hmap))
			if row[x] < adjacentMin {
				lowPoints = append(lowPoints, Point{X: x, Y: y})
			}
		}
	}
	return lowPoints
}

func min(h ...byte) byte {
	var m byte = 128
	for _, v := range h {
		if v < m {
			m = v
		}
	}
	return m
}

func Height(x int, y int, hmap []string) byte {
	if x < 0 || y < 0 || y >= len(hmap) || x >= len(hmap[y]) {
		return '9'
	}
	return hmap[y][x]
}
