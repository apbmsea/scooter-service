import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import federation from "@originjs/vite-plugin-federation";

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: "hostApp",
      filename: "remoteEntry.js",
      remotes: {
        authMF: "http://localhost:5001/assets/remoteEntry.js",
        sharedMF: 'https://localhost:5002/assets/remoteEntry.js',
        userMF: "http://localhost:5003/assets/remoteEntry.js",
        headerMF: "http://localhost:5004/assets/remoteEntry.js",
      },
      shared: [
        "axios",
        "react",
        "react-dom",
        "react-router-dom",
        "react-redux",
        "@reduxjs/toolkit",
        "redux-saga",
        "typed-redux-saga",
      ],
    }),
  ],

  build: {
    modulePreload: false,
    target: "esnext",
    minify: false,
    cssCodeSplit: false,
  },
});
