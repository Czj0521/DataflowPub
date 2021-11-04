// src/store.ts
import { createStore } from 'ice';
import filter from './models/filter';

const store = createStore(
  {
    filter,
  },
  {
    // options
  },
);

export default store;
