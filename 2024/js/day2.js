
import { asLines } from './util.js';

const day2a = asLines('../input/2.txt').map((l) => l.split(/\s+/).map((v) => Number(v)));

const badCount = (rest, expectedSign) => {
  if (rest.length < 2) return 0;
  const diff = rest[0] - rest[1];
  const bad = diff > 3 || diff < -3 || diff === 0 || Math.sign(diff) !== expectedSign ? 1 : 0;
  return bad + badCount(rest.slice(1), expectedSign);
}
const safe = (line) => badCount(line, Math.sign(line[0] - line[1])) === 0;
const s = day2a.filter((l) => safe(l));
console.log(`day1 part a: ${s.length}`);

const safe2 = (line) => {
  for (let i = 0; i < line.length; i++) {
    const oneRemoved = line.toSpliced(i, 1);
    if (safe(oneRemoved)) return true;
  }
  return false;
};
const s2 = day2a.filter((l) => safe2(l));
console.log(`day2 part a: ${s2.length}`);
