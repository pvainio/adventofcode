import { asLines } from './util.js';
const garden = asLines('../input/12.txt').map(l => l.split(""));

const inSameRegion = ([x,y], region) => region[`${x}.${y}`] != undefined;
const inAnyRegion = ([x, y], regions) => regions.some(r => inSameRegion([x, y], r));

const buildRegion = ([x, y], plant, region = {}) => {
  region[`${x}.${y}`] = 4; // initialize perimeter to 4
  const moves = [[0, 1], [0, -1], [1, 0], [-1, 0]];
  for (const [dx, dy] of moves) {
    const [nx, ny] = [x + dx, y + dy];
    if (nx < 0 || nx >= garden[0].length || ny < 0 || ny >= garden.length) continue; // outside
    if (garden[ny][nx] != plant) continue; // different plant
    region[`${x}.${y}`]--; // same plant, no perimeter needed
    if (inSameRegion([nx, ny], region)) continue; // already calculated
    buildRegion([nx, ny], plant, region);
  }
  return region;
};

const compute = (garden) => {
  const regions = [];
  garden.forEach((_, y) => garden[y].forEach((_, x) => {
    if (inAnyRegion([x,y], regions)) return; // alredy in region
    const plant = garden[y][x];
    const region = buildRegion([x, y], plant);
    regions.push(region);
  }));
  return regions;
};

const regions = compute(garden);
const area = (region) => Object.entries(region).length;
const perimeter = (region) => Object.values(region).reduce((a, b) => a + b);
const price = regions.map(r => area(r) * perimeter(r)).reduce((a, b) => a + b);
console.log(price);
