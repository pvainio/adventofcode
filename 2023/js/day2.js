
import { asLines } from './util.js';

const getGames = () => {
  return asLines('../input/day2_a.txt').map(line => toGame(line));
};

const toGame = (line) => {
  const [gameStr, setsStr] = line.split(':');
  const id = parseInt(gameStr.split(' ')[1]);
  const sets = setsStr.split(';').map(setStr => {
    const set = {};
    const colors = setStr.split(',');
    colors.forEach(color => {
      const [count, colorName] = color.trim().split(' ');
      set[colorName] = parseInt(count);
    });
    return set;
  });
  return { id, sets };
};

const isPossibleGame = (game, red, green, blue) => {
  return game.sets.filter(set => !isPossibleSet(set, red, green, blue)).length === 0;
};

const isPossibleSet = (set, red, green, blue) => {
  return (set.red || 0) <= red && (set.green || 0) <= green && (set.blue || 0) <= blue;
}

const minimumSet = (sets) => {
  const red = sets.reduce((min, set) => Math.max(min, set.red || 0), 0);
  const green = sets.reduce((min, set) => Math.max(min, set.green || 0), 0);
  const blue = sets.reduce((min, set) => Math.max(min, set.blue || 0), 0);
  return { red, green, blue };
};

const sum1 = getGames()
  .filter(game => isPossibleGame(game, 12, 13, 14))
  .map(game => game.id)
  .reduce((a,b) => a + b, 0);

console.log(`part 1: ${sum1}`);

const sum2 = getGames()
  .map(game => minimumSet(game.sets))
  .map(minSet => minSet.red * minSet.green * minSet.blue)
  .reduce((a,b) => a + b, 0);

console.log(`part 2: ${sum2}`);
