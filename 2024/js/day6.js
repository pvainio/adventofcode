import { asLines } from './util.js';

const map = asLines('../input/6.txt').map(l => l.split(""));

const dirs = { '^': [0, -1, '>'], 'v': [0, 1, '<'], '<': [-1, 0, '^'], '>': [1, 0, 'v']};
const findPosAndDir = (map) => map.flatMap((r, y) => r.flatMap((c, x) => dirs[c] ? [x, y, c] : null).filter(p => p !== null));
const outOfBounds = (map, x, y) => x < 0 || x > map[0].length - 1 || y < 0 || y > map.length - 1;

const patrol = (map, obs) => {
  let [x, y, dir] = findPosAndDir(map);
  const route = [];
  while (true) {
    const posAndDir = `${x},${y} ${dir}`;
    if (route.includes(posAndDir)) return null;
    route.push(posAndDir);
    const [dx, dy, rotate] = dirs[dir];
    const [nx, ny] = [x + dx, y + dy];
    if (outOfBounds(map, nx, ny)) return route;
    if (map[ny][nx] == '#' || (obs?.x == nx && obs?.y == ny)) dir = rotate;
    else [x, y] = [nx, ny];
  }
}

const route = patrol(map);
const positions = route.map(posAndDir => posAndDir.split(' ')[0]);
const uniqPositions = [...new Set(positions)];
console.log(`day6a: ${uniqPositions.length}`);

const firstPosition = positions[0];
// try adding obstacle to visited position on route and check for loop
const loop = uniqPositions.filter(pos => {
  if (pos == firstPosition) return false; // not allowed to add obstacle to start
  const [x, y] = pos.split(',').map(Number);
  return patrol(map, {x, y}) == null; // patrol returns null in case of loop
});
const uniqLoop = new Set(loop);
console.log(`day6b: ${uniqLoop.size}`);