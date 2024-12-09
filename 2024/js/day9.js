import { asLines } from './util.js';

const data = asLines('../input/9.txt')[0].split('').map(c => Number(c));

let idCounter = 0;
const blocks = data.flatMap((_, i) => new Array(data[i]).fill(i % 2 ? '.' : idCounter++))

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

const compacted = compact(blocks);

const checksum = (blocks) => blocks.reduce((a, b, i) => a + (b == '.' ? 0 : (i  * b)));

console.log(`day9a: ${checksum(compacted)}`);
