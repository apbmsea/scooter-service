import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  base: "/admin-mfe-assets/",
  build: {
    outDir: "dist",
    emptyOutDir: true,
    rollupOptions: {
      input: "/src/app/main.tsx",
      output: {
        entryFileNames: "assets/[name].js",
        chunkFileNames: "assets/[name].js",
        assetFileNames: "assets/[name][extname]",
      },
    },
  },
  server: {
    port: 3004,
    cors: true,
  },
});
