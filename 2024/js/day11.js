const stones = '572556 22 0 528 4679021 1 10725 2790'.split(' ').map(s => Number(s));

const blinkStone = (stone) => {
  if (stone == 0) return [1];
  const str = `${stone}`;
  if (str.length % 2 == 0) {
    const middle = str.length / 2;
    const [left, right] = [str.substring(0, middle), str.substring(middle)];
    return [Number(left), Number(right)];
  }
  return [2024 * stone];
};

const cache = {};
const numberAfterBlinks = (stone, blinks) => {
  const key = `${stone}.${blinks}`;
  if (cache[key]) return cache[key];
  if (blinks == 0) return 1;
  const stones = blinkStone(stone);
  const ret = stones.map(s => numberAfterBlinks(s, blinks-1)).reduce((a, b) => a + b);
  cache[key] = ret;
  return ret;
}

const stonesAfterBlinks = (stones, blinks) => {
  return stones.map(s => numberAfterBlinks(s, blinks)).reduce((a, b) => a + b);
}

console.log(`part1: ${stonesAfterBlinks(stones, 25)}`);
console.log(`part2: ${stonesAfterBlinks(stones, 75)}`);
