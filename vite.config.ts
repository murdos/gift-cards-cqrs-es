import vue from '@vitejs/plugin-vue';
import { defineConfig } from 'vite';
import tsconfigPaths from 'vite-tsconfig-paths';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), tsconfigPaths()],
  build: {
    outDir: '../../../target/classes/static',
  },
  root: 'src/main/webapp',
  server: {
    port: 9000,
  },
});
