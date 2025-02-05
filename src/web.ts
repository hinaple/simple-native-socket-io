import { ListenerCallback, PluginListenerHandle, WebPlugin } from '@capacitor/core';
import type { SimpleNativeSocketioPlugin } from './definitions';

export class SimpleNativeSocketioWeb extends WebPlugin implements SimpleNativeSocketioPlugin {
  async startSocketConnection(options: { url: string }): Promise<void> {
    console.warn('startSocketConnection is not implemented on web', options);
    return;
  }

  async addSocketListener(options: { event: string }): Promise<void> {
    console.warn('addSocketListener is not implemented on web', options);
    return;
  }

  async emit(options: { event: string; data: any }): Promise<void> {
    console.warn('emit is not implemented on web', options);
    return;
  }

  async onSocketEvent(options?: {}): Promise<void> {
    console.warn('onSocketEvent is not implemented on web', options);
    return;
  }
}

const SimpleNativeSocketio = new SimpleNativeSocketioWeb();

export { SimpleNativeSocketio };
