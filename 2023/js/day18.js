import { asLines } from './util.js';

const plan = asLines('../input/day18.txt').map(line => line.split(' '));

const dirs = { R: [1, 0], L: [-1, 0], U: [0, -1], D: [0, 1] };

const digCorners = (plan) => {
  let pos = [0, 0];
  const holes = [ ];
  plan.forEach(([dir, meters]) => {
    const [dx, dy] = dirs[dir];
    pos = [pos[0] + dx * meters, pos[1] + dy * meters];
    holes.push(pos);
  });
  return holes;
}

const shoelaceFormula = (coords) => {
  let sum = 0;
  let trenchLen = 0;
  for (let i = 0; i < coords.length; i++) {
    const next = coords[i + 1] ? coords[i + 1] : coords[0];
    const x = coords[i][0];
    const ny = next[1];
    const y = coords[i][1];
    const nx = next[0];
    sum += x * ny - y * nx;
    trenchLen += Math.abs(coords[i][0] - next[0]) + Math.abs(coords[i][1] - next[1]);
  }

  const shoelace = Math.abs(sum / 2);
  // fix error caused by square units
  return shoelace + trenchLen / 2 + 1;
}

const corners = digCorners(plan);
const area1 = shoelaceFormula(corners);
console.log(`part1: ${area1}`);

const fixPlan = (plan) => plan.map(([,, color]) => {
  const dirMap = [ 'R', 'D', 'L', 'U' ];
  const meters = parseInt(color.substring(2, 7), 16);
  const dir = dirMap[parseInt(color.substring(7, 8))];
  return [dir, meters, color];
});

const corners2 = digCorners(fixPlan(plan));
const part2 = shoelaceFormula(corners2);
console.log(`part2: ${part2}`);
