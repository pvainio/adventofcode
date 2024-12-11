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

const blink = (stones) => stones.flatMap(s => blinkStone(s));

let afterBlink = stones;
for (let i = 0; i < 25; i++) {
  afterBlink = blink(afterBlink);
}
console.log(afterBlink.length);
