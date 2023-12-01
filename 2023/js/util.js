import { readFileSync } from 'fs';

export const asLines = (fname) => {
  return readFileSync(fname).toString()
    .split('\n')
    .filter((l) => l.length > 0);
}
