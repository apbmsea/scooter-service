import { defineConfig } from "vite";
import federation from "@originjs/vite-plugin-federation";
import fs from "node:fs";
import path from "node:path";

export default defineConfig({
  server: {
    port: 5002,
    https: {
      cert: fs.readFileSync(path.resolve(__dirname, "certs/cert.pem")),
      key: fs.readFileSync(path.resolve(__dirname, "certs/key.pem")),
    },
  },
  preview: {
    https: {
      cert: fs.readFileSync(path.resolve(__dirname, "certs/cert.pem")),
      key: fs.readFileSync(path.resolve(__dirname, "certs/key.pem")),
    },
    host: true,
    port: 5002,
  },
  plugins: [
    federation({
      name: "sharedMF",
      filename: "remoteEntry.js",
      remotes: {
        authMF: "http://localhost:5001/assets/remoteEntry.js",
      },
      exposes: {
        "./event": "./src/event/index.ts",
        "./api": "./src/api/index.ts",
        "./types": "./src/types/index.ts",
        "./utils": "./src/utils/index.ts",
      },
      shared: ["axios"],
    }),
  ],
  build: {
    lib: {
      entry: "src/index.ts",
      formats: ["es"],
      fileName: () => "shared.js",
    },
    modulePreload: false,
    target: "esnext",
    cssCodeSplit: false,
  },
});
