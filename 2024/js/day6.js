import { asLines } from './util.js';

const map = asLines('../input/6.txt').map(l => l.split(""));

const dirs = { '^': [0, -1, '>'], 'v': [0, 1, '<'], '<': [-1, 0, '^'], '>': [1, 0, 'v']};
const findPosAndDir = (map) => map.flatMap((r, y) => r.flatMap((c, x) => dirs[c] ? [x, y, c] : null).filter(p => p !== null));
const outOfBounds = (map, x, y) => x < 0 || x > map[0].length - 1 || y < 0 || y > map.length - 1;

const patrol = (map, x, y, dir) => {
  const route = [];
  while (true) {
    const posAndDir = `${x},${y} ${dir}`;
    if (route.includes(posAndDir)) return null;
    route.push(posAndDir);
    const [dx, dy, rotate] = dirs[dir];
    const [nx, ny] = [x + dx, y + dy];
    if (outOfBounds(map, nx, ny)) return route;
    if (map[ny][nx] == '#') dir = rotate;
    else [x, y] = [nx, ny];
  }
}

const [startx, starty, startdir] = findPosAndDir(map);
const route = patrol(map, startx, starty, startdir);
const positions = route.map(posAndDir => posAndDir.split(' ')[0]);
const uniqPositions = [...new Set(positions)];
console.log(`day6a: ${uniqPositions.length}`);

// try adding obstacle to visited position on route and check for loop
const loop = uniqPositions.filter(pos => {
  const [x, y] = pos.split(',').map(Number);
  if (x == startx && y == starty) return false; // not allowed to add obstacle to start
  const nmap = JSON.parse(JSON.stringify(map)); // deep clone
  nmap[y][x] = '#'; // add new obstacle
  return patrol(nmap, startx, starty, startdir) == null; //
});
const uniqLoop = new Set(loop);
console.log(`day6b: ${uniqLoop.size}`);