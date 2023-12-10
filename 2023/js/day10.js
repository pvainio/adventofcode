import { asLines } from './util.js';

const map = asLines('../input/day10.txt').map(line => line.split(''));

const findStart = (map) => {
  return map.flatMap((row, y) => {
    return row.flatMap((char, x) => char === 'S' ? [x, y] : []);
  });
}

const start = findStart(map);

const directions = [ [0, -1], [0, 1], [-1, 0], [1, 0] ];
const tileConnections = {
  '|': [ [0, -1], [0, 1] ],
  '-': [ [-1, 0], [1, 0] ],
  'L': [ [0, 1], [-1, 0] ],
  'J': [ [0, 1], [1, 0] ],
  '7': [ [0, -1], [1, 0] ],
  'F': [ [0, -1], [-1, 0] ],
};


const isPossibleFrom = (direction, tile) => {
  if (!tileConnections[tile]) {
    return false;
  }
  return tileConnections[tile].find(([dx, dy]) => dx === direction[0] && dy === direction[1]);
}

const isPossibleTo = (direction, tile) => {
  if (!tileConnections[tile]) {
    return false;
  }
  return tileConnections[tile].find(([dx, dy]) => dx === -direction[0] && dy === -direction[1]);
}

const isPossibleMove = (map, [x, y], [dx, dy]) => {
  const currentTile = map[y][x];
  const nextTile = map[y+dy][x+dx];
  return isPossibleTo([dx, dy], currentTile) && isPossibleFrom([dx, dy], nextTile);
}

const findNextLocation = (map, [x, y], visited) => {
  return directions
    .filter(([dx, dy]) => isPossibleMove(map, [x, y], [dx, dy])) // filter possible moves
    .map(([dx, dy]) => [x + dx, y + dy]) // map move to coordinates
    .find(([nx, ny]) => !visited.find(([x, y]) => x === nx && y === ny)); // accept not visisited
};

const buildPipe = (map, location) => {
  const pipe = [];
  while (location) {
    pipe.push(location);
    location = findNextLocation(map, location, pipe);
  }
  return pipe;
};

const fixStartTile = (map) => {
  const [x, y] = findStart(map);
  const tile = Object.entries(tileConnections).filter(([tile, connections]) => {
    return connections.filter(([dx, dy]) => { // filter possible connections
      const nextTile = map[y - dy][x - dx]; // get tile
      return isPossibleTo([-dx, -dy], tile) && isPossibleFrom([-dx, -dy], nextTile);
    }).length === 2; // accept only 2 connections
  }).map(([tile]) => tile).find(() => true); // get filtered tile
  map[y][x] = tile; // replace start tile
  return map;
}

const fixedMap = fixStartTile(map);
const pipe = buildPipe(fixedMap, start, []);

console.log(`part1: ${pipe.length/2}`);

const isPipe = (x, y, pipe) => !!pipe.find(([px, py]) => px === x && py === y);
const horizontalCorners = [ 'L', 'J', '7', 'F' ];

const raycast = (x, y, pipe) => {
  let crossings = 0;
  let firstCorner;;
  // from position to left, count pipe crossings
  for (let dx = x; dx >= 0; dx--) {
    if (!isPipe(dx, y, pipe)) continue;

    const tile = map[y][dx];
    
    if (tile === '|') { // vertical pipe is always crossing
      crossings++;
      continue;
    }

    if (horizontalCorners.includes(tile)) {
      // for corners need to check if pipe is going vertical
      if (!firstCorner) { // first corner
        firstCorner = tile;
      } else { // second corder
        if (tileConnections[firstCorner][0][1] !== tileConnections[tile][0][1]) {
          // vertical if start and end corner have different vertical connection direction
          crossings++;
        }
        firstCorner = null;
      }
    }
  }
  return crossings;
};

const enclosedArea = (map, pipe) => {
  let area = 0;
  // loop through all points
  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[0].length; x++) {
      if (isPipe(x, y, pipe)) {
        continue; // point is on the pipe, skip
      }
      // odd number of raycast crossings means inside the pipe
      area += raycast(x, y, pipe) % 2 ? 1 : 0;
    }
  }
  return area;
};

const area = enclosedArea(fixedMap, pipe);
console.log(`part2: ${area}`);
