import { WebPlugin } from '@capacitor/core';

import type { SimpleNativeSocketioPlugin } from './definitions';

export class SimpleNativeSocketioWeb extends WebPlugin implements SimpleNativeSocketioPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
