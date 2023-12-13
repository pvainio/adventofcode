import { readFileSync } from 'fs';

const patterns = readFileSync('../input/day13.txt').toString().split('\n\n');
const pattern = patterns.map(pattern => pattern.split('\n').map(line => line.split('')));

const isMirror = (line, mirrorPosition) => {
  for (let distance = 0; ; distance++) {
    const left = line[mirrorPosition - distance - 1];
    const right = line[mirrorPosition + distance];
    if (left === undefined || right === undefined) {
      return true;
    }
    if (left !== right) {
      return false;
    }
  }
}

const findMirrorVertical = (pattern, ignore) => {
  let options = [...Array(pattern[0].length-1).keys()].map(i => i + 1);
  if (ignore) {
    options = options.filter(o => o !== ignore);
  }
  for (let i = 0; i < pattern.length; i++) {
    const toCheck = [...options];
    options = options.filter(opt => isMirror(pattern[i], opt));
  }
  if (options.length === 1) {
    return options[0];
  }
  return undefined;
}

const transpose = (pattern) => {
  let res = Array(pattern[0].length).fill('b');
  res = res.map(l => Array(pattern.length).fill('-'));
  for (let i = 0; i < pattern.length; i++) {
    for (let j = 0; j < pattern[i].length; j++) {
      res[j][i] = pattern[i][j];
    }
  }
  return res;
}

const score = (pattern) => {
  const vert = findMirrorVertical(pattern);
  return vert ? vert : findMirrorVertical(transpose(pattern)) * 100;
};

const sum1 = pattern.map(p => score(p)).reduce((a, b) => a + b, 0);

console.log(`part1: ${sum1}`);

const scoreFixed = (pattern) => {
  const origVertical = findMirrorVertical(pattern);
  const origHorizontal = findMirrorVertical(transpose(pattern));
  for (let i = 0; i < pattern.length; i++) {
    for (let j = 0; j < pattern[i].length; j++) {
      const clone = JSON.parse(JSON.stringify(pattern));
      clone[i][j] = clone[i][j] === '.' ? '#' : '.';
      const m = findMirrorVertical(clone, origVertical);
      if (m) {
        return m;
      }
      const h = transpose(clone);
      const hm = findMirrorVertical(h, origHorizontal);
      if (hm) {
        return hm * 100;
      }
    }
  }
  return undefined;
};

const sum2 = pattern.map(p => scoreFixed(p)).reduce((a, b) => a + b, 0);

console.log(`part2: ${sum2}`);
