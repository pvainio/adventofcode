import { asLines } from './util.js';

const map = asLines('../input/8.txt').map(l => l.split(""));

const antennaByFreq = {};
map.forEach((r, y) => r.forEach((freq, x) => {
  if (freq == '.') return;
  if (!antennaByFreq[freq]) antennaByFreq[freq] = [];
  antennaByFreq[freq].push([x, y]);
}));

const valid = ([x, y]) => x >= 0 && x < map[0].length && y >= 0 && y < map.length;

const anti = ([x, y], [dx, dy], distanceLimit) => {
  const anti = new Set();
  for (const an = [x, y]; valid(an); an[0] += dx, an[1] += dy) {
    if (distanceLimit && an[0] - dx != x && an[1] - dy != y) continue;
    anti.add(JSON.stringify(an));
  }
  return anti;
};

const antinodes = (distanceLimit) => {
  const antinodes = new Set();
  Object.entries(antennaByFreq).forEach(([_, coords]) => {
    for (let i = 0; i < coords.length; i++) {
      for (let j = 0; j < coords.length; j++) {
        if (i == j) continue;
        const [x1, y1] = coords[i];
        const [x2, y2] = coords[j];
        const dx = x2 - x1;
        const dy = y2 - y1;
        anti([x1, y1], [-dx, -dy], distanceLimit).forEach(a => antinodes.add(a));
        anti([x2, y2], [dx, dy], distanceLimit).forEach(a => antinodes.add(a));
      }
    }
  });
  return antinodes;
}

console.log(`day8a ${antinodes(true).size}`);
console.log(`day8b ${antinodes(false).size}`);
