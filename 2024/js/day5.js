
import { readFileSync } from 'fs';

const day4 = readFileSync('../input/5.txt').toString();
const rules = day4.split('\n\n')[0].split('\n').map(l => l.split('|').map(s => Number(s)));
const updates = day4.split('\n\n')[1].split('\n').map(l => l.split(',').map(s => Number(s)));

const validPage = (page, to, pages) => {
  const test = pages.slice(0, to); // test pages before position
  return !test.some(testPage => rules.some(r => r[0] == page && r[1] == testPage));
};

const valid = (pages) => pages.some((p, i) => !validPage(p, i, pages)) == false;
const validUpdates = updates.filter(valid);
const middles = validUpdates.map(u => u[Math.floor(u.length/2)]);
const sum = middles.reduce((a, b) => a + b);

console.log(`sum of middle pages: ${sum}`);

const reorder = (pages) => {
  for (let pos = 0; pos < pages.length; pos++) {
    const test = pages.slice(0, pos);
    for (let testPos = 0; testPos < test.length; testPos++) {
      const failed = rules.some(r => r[0] == pages[pos] && r[1] == pages[testPos]);
      if (failed) {
        // testPage was before page but rule says otherwise, move page before testPage
        pages = pages.toSpliced(pos, 1).toSpliced(testPos, 0, pages[pos])
        return reorder(pages);
      }
    }
  }
  return pages;
}

const invalid = updates.filter(u => !valid(u));
const ordered = invalid.map(u => reorder(u));
const middles2 = ordered.map(u => u[Math.floor(u.length/2)]);
const sum2 = middles2.reduce((a, b) => a + b);
console.log(`sum of middle pages: ${sum2}`);