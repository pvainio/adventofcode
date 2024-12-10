import { asLines } from './util.js';

const data = asLines('../input/10.txt').map(l => l.split("").map(h => Number(h)));

const trailHeads = data.flatMap((r, y) => r.map((h, x) => [h, x, y]).filter(([h]) => h == 0));

const moves = [[0, 1], [0, -1], [1, 0], [-1, 0]];

const score = ([_, x, y], type = 1) => {
  const visited = new Set();
  let score = 0;
  const queue = [[x, y]];
  while(queue.length > 0) {
    const [x, y] = queue.shift();
    if (visited.has(`${x}.${y}`) && type == 1) continue;
    visited.add(`${x}.${y}`);
    const height = data[y][x];
    if (height >= 9) {
      score++;
    } else {
      const nextPositions = moves.map(([dx, dy]) => [x + dx, y + dy])
        .filter(([x, y]) => x >= 0 && y >= 0 && x < data[0].length && y < data.length) // inside map
        .filter(([x, y]) => data[y][x] == height + 1) // one higher
      queue.push(...nextPositions);
    }
  }
  return score;
};

const scores = trailHeads.map(tr => score(tr));
const sum = scores.reduce((a,b) => a + b);

console.log(`part1: ${sum}`);

const scores2 = trailHeads.map(tr => score(tr, 2));
const sum2 = scores2.reduce((a,b) => a + b);

console.log(`part2: ${sum2}`);
