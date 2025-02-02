export interface SimpleNativeSocketioPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
