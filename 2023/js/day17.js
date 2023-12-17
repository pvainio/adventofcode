import { asLines } from './util.js';

const map = asLines('../input/day17.txt').map(line => line.split('').map(c => parseInt(c)));

const next = { R: [1, 0], L: [-1, 0], U: [0, -1], D: [0, 1], '' : [0, 0] };
const nextXY = (x, y, dir) => [x + next[dir][0], y + next[dir][1]];

const opposite = { R: 'L', L: 'R', U: 'D', D: 'U' };
const getPossibleDirs = (dir) => ['R', 'D', 'U', 'L'].filter(d => d !== opposite[dir]);

const shortestPath = (map, minSteps, maxSteps) => {
  const visited = new Map();
  const queue = [{x: 0, y: 0, dir: '', dirSteps: minSteps, temp: -map[0][0]}];
  let min = 999999;
  while (queue.length > 0) {
    const {x: ox, y: oy, dir, dirSteps, temp: prevTemp} = queue.shift();
    const [x, y] = nextXY(ox, oy, dir);

    if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) continue; // out of bounds
    if (dirSteps > maxSteps) continue; // too many steps in same direction
    const temp = prevTemp + map[y][x]; // add to temp
    if (temp >= min) continue; // already too big
    const prev = visited.get(`${x},${y},${dir},${dirSteps}`);
    if (prev && prev <= temp) continue; // already visited with same or better temp
    visited.set(`${x},${y},${dir},${dirSteps}`, temp);

    if (dirSteps >= minSteps && x === map[0].length - 1 && y === map.length - 1) {
      min = Math.min(min, temp); // reach goal, update min
    } else {
      const possibleDirs = dirSteps < minSteps ? [dir] : getPossibleDirs(dir);
      const nextSteps = possibleDirs.map(n => ({x, y, dir: n, dirSteps: dir === n ? dirSteps + 1 : 1, temp}));
      queue.push(...nextSteps);
      queue.sort((a, b) => a.temp - b.temp);  
    }
  }
  return min;
}

const shortestPath1 = shortestPath(map, 0, 3);
console.log(`part1: ${shortestPath1}`);

const shortestPath2 = shortestPath(map, 4, 10);
console.log(`part2: ${shortestPath2}`);
