import { asLines } from './util.js';

const layout = asLines('../input/day16.txt').map(line => line.split(''));

const traceLight = (layout, light, visited) => {
  const {x, y, dx, dy} = light;
  if (x < 0 || y < 0 || x >= layout[0].length || y >= layout.length) {
    return []; // light has left the building
  }
  if (!visited.has(`${x},${y}`)) visited.set(`${x},${y}`, new Set());
  if (visited.get(`${x},${y}`).has(`${dx},${dy}`)) return []; // light has looped
  visited.get(`${x},${y}`).add(`${dx},${dy}`);

  const current = layout[y][x];
  let next = [{dx, dy}];
  if (current === '/') next = [{dx: -dy, dy: -dx}];
  if (current === '\\') next = [{dx: dy, dy: dx}];
  if (current === '|' && dx !==0) next = [{dx: dy, dy: -1}, {dx: dy, dy: 1}];
  if (current === '-' && dy !==0) next = [{dx: -1, dy: dx}, {dx: 1, dy: dx}];
  return next.map(({dx, dy}) => ({x: x + dx, y: y + dy, dx, dy}));
}

const traceLights = (layout, lights) => {
  const visited = new Map();
  while (lights.length > 0) {
    lights = lights.flatMap(light => traceLight(layout, light, visited));
  }
  return visited.size;
}

const energized = traceLights(layout, [{x: 0, y: 0, dx: 1, dy: 0}]);
console.log(`part1: ${energized}`);

let energized2 = 0;
for (let y = 0; y < layout.length; y++) {
  for (let x = 0; x < layout[0].length; x++) {
    if (y === 0) {
      energized2 = Math.max(energized2, traceLights(layout, [{x, y, dx: 0, dy: 1}]));
    }
    if (x === 0) {
      energized2 = Math.max(energized2, traceLights(layout, [{x, y, dx: 1, dy: 0}]));
    }
    if (y === layout.length - 1) {
      energized2 = Math.max(energized2, traceLights(layout, [{x, y, dx: 0, dy: -1}]));
    }
    if (x === layout[0].length - 1) {
      energized2 = Math.max(energized2, traceLights(layout, [{x, y, dx: -1, dy: 0}]));
    }
  }
}

console.log(`part2: ${energized2}`);
