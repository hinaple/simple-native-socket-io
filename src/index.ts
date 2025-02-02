import { registerPlugin } from '@capacitor/core';

import type { SimpleNativeSocketioPlugin } from './definitions';

const SimpleNativeSocketio = registerPlugin<SimpleNativeSocketioPlugin>('SimpleNativeSocketio', {
  web: () => import('./web').then((m) => new m.SimpleNativeSocketioWeb()),
});

export * from './definitions';
export { SimpleNativeSocketio };
