import { asLines } from './util.js';

const data = asLines('../input/9.txt')[0].split('').map(c => Number(c));

const buildBlocks = (data) => {
  let idCounter = 0;
  return data.flatMap((_, i) => new Array(data[i]).fill(i % 2 ? '.' : idCounter++));
}

const compact = (blocks) => {
  let p1 = 0;
  let p2 = blocks.length - 1;
  
  while(p1 < p2) {
    while (blocks[p1] != '.' && p1 < p2 && p1 < blocks.length - 1) p1++;
    while (blocks[p2] == '.' && p1 < p2 && p2 > 0) p2--;
    if (p1 >= p2 || blocks[p1] != '.' || blocks[p2] == '.') break;
    blocks[p1] = blocks[p2];
    blocks[p2] = '.';
  }
  return blocks; 
};

const compacted = compact(buildBlocks(data));

const checksum = (blocks) => blocks.reduce((a, b, i) => a + (b == '.' ? 0 : (i  * b)));

console.log(`day9a: ${checksum(compacted)}`);

const findFile = (blocks, p) => {
  if (p < 0) return [-1, -1, -1];
  while(p > 0 && blocks[p] == '.') p--;
  const end = p;
  const id = blocks[end];
  while(p > 0 && blocks[p-1] == id) p--;
  const len = end - p + 1;
  return [id, p, len];
}

const findFirstFit = (blocks, len, max) => {
  let p = 0;
  let found = 0;
  while (p < max) {
    if (blocks[p] == '.') found++;
    else found = 0;
    if (found == len) return p - found + 1;
    p++;
  }
  return -1;
}

const compact2 = (blocks) => {
  let p = blocks.length;
  while(true) {
    const [id, fileStart, len] = findFile(blocks, p - 1);
    if (id == -1) break;
    const fit = findFirstFit(blocks, len, fileStart);
    if (fit != -1) {
      for (let i = 0; i < len; i++) {
        blocks[fit + i] = blocks[fileStart + i];
        blocks[fileStart + i] = '.';
      }
    }
    p = fileStart;
  }
  return blocks; 
};

const compacted2 = compact2(buildBlocks(data));
console.log(`day9b: ${checksum(compacted2)}`);
