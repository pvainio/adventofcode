
import { readFileSync } from 'fs';

const input = readFileSync('../input/day5.txt').toString().split('\n\n');
const seeds = input.shift().split(':')[1].trim().split(' ').map((n) => parseInt(n));

// format [ { from: 'seed', to: 'location', ranges: [ { rstart: 0, rend: 0, diff: 0 } ] } ]
const buildMap = (input) => {
  return input.map((map) => {
    const [mapping, rangesStr] = map.split(' map:\n');
    const [from, to] = mapping.split('-to-');
    const ranges = rangesStr.split('\n').map((range) => {
      const [to, from, len] = range.trim().split(' ').map((n) => parseInt(n));
      return { rstart: from, rend: from + len - 1, diff: to - from };
    }).sort((a, b) => a.rstart - b.rstart);
    return { from, to, ranges };
  });
};

const maps = buildMap(input);

const mapRangeToLocation = (from, [start, end]) => {
  if (from === 'location') {
    return [start, end];
  }
  const targetMap = maps.filter((m) => m.from === from)[0];
  const targetRanges = targetMap.ranges.filter((r) => r.rstart <= end && r.rend >= start);
  const nextRanges = [];
  for (let i = start; i <= end;) { // loop through the range
    if (targetRanges.length === 0) {
      nextRanges.push([i, end]); // no more target ranges, push the rest of the range
      break;
    }
    const match = targetRanges.find((r) => r.rstart <= i && r.rend >= i); 
    if (match) { // there is a match in target ranges
      const { rend, diff } = targetRanges.shift();
      const matchEnd = Math.min(end, rend);
      const nextRange = [i + diff, matchEnd + diff];
      nextRanges.push(nextRange);
      i = matchEnd + 1;
    } else {
      const { rstart } = targetRanges[0];
      const matchEnd = Math.min(end, rstart);
      const nextRange = [i, matchEnd];
      nextRanges.push(nextRange);
      i = matchEnd + 1;
    }
  }
  return nextRanges.flatMap(([start, end]) => {
      return mapRangeToLocation(targetMap.to, [start, end]);
  });
}

const lowestLocation = () => {
  return seeds.map((seed) => {
    const locationsForSeed = mapRangeToLocation('seed', [seed, seed]);
    return locationsForSeed.reduce((a, b) => Math.min(a, b), Number.MAX_SAFE_INTEGER);
  }).reduce((a, b) => Math.min(a, b), Number.MAX_SAFE_INTEGER);
}

console.log(`part 1: ${lowestLocation()}`);

const lowestLocation2 = () => {
  const res = [];
  for (let i = 0; i < seeds.length; i += 2) {
    const seedRange = [seeds[i], seeds[i] + seeds[i+1] - 1];
    const locations = mapRangeToLocation('seed', seedRange);
    const minLocation = locations.reduce((a, b) => Math.min(a, b), Number.MAX_SAFE_INTEGER);
    res.push(minLocation);
  }
  return res.reduce((a, b) => Math.min(a, b), Number.MAX_SAFE_INTEGER);
}

console.log(`part 2: ${lowestLocation2()}`);
