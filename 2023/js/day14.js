import { asLines } from './util.js';

const platform = asLines('../input/day14.txt').map(line => line.split(''));

const move = (platform, x, y, nx, ny) => {
  const c = platform[y][x];
  const nc = platform[ny] ? platform[ny][nx] : undefined;
  if (c === 'O' && nc === '.') {
    platform[ny][nx] = c;
    platform[y][x] = '.';
    return 1;
  }
  return 0;
}

const NORTH = { x: 0, y: -1, start: _ => 0, next: v => v + 1 };
const WEST = { x: -1, y: 0, start: _ => 0, next: v => v + 1 };
const SOUTH = { x: 0, y: 1, start: p => p.length - 1, next: v => v - 1 };
const EAST = { x: 1, y: 0, start: p => p.length - 1, next: v => v - 1 };

const tilt = (platform, dir) => {
  let moves = 1;
  while (moves) {
    moves = 0;
    for (let y = dir.start(platform); y >= 0 && y < platform.length; y = dir.next(y)) {
      for (let x = dir.start(platform); x >= 0 && x < platform[y].length; x = dir.next(x)) {
        moves += move(platform, x, y, x + dir.x, y + dir.y);
      }
    }
  };
};

tilt(platform, NORTH);

const load = (platform) => 
  platform.map((line, y) => line.map(c => c !== 'O' ? 0 : platform.length - y) // calculate distance from bottom
      .reduce((a, b) => a + b, 0) // sum all distances for line
    ).reduce((a, b) => a + b, 0); // sum all lines

console.log(`part1: ${load(platform)}`);

const platform2 = asLines('../input/day14.txt').map(line => line.split(''));

const cycle = (platform) => {
  const maps = new Map(); // store maps and their index to detect cycles
  for(let i = 0; i < 1000_000_000; i++) {
    tilt(platform, NORTH);
    tilt(platform, WEST);
    tilt(platform, SOUTH);
    tilt(platform, EAST);
    const mapId = platform.map(line => line.join('')).join('\n');
    if (maps.get(mapId)) {
      // found cycle, skip to end
      const cycleLength = i - maps.get(mapId);
      const remaining = 1000_000_000 - i;
      i += Math.floor(remaining / cycleLength) * cycleLength;
      maps.clear();
    }
    maps.set(mapId, i);
  }
}

cycle(platform2);

console.log(`part2: ${load(platform2)}`);
