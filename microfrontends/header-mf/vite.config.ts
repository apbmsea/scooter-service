import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';

export default defineConfig({
	plugins: [
		react(),
		federation({
			name: 'header-mf',
			filename: 'remoteEntry.js',
			remotes: {
				sharedMF: 'https://localhost:5002/assets/remoteEntry.js',
        authMF: 'http://localhost:5001/assets/remoteEntry.js',
			},
			exposes: {
				'./App': './src/app/App'
			},
			shared: [
				'react-router-dom',
        'axios',
				'react',
				'react-dom',
			]
		})
	],
	build: {
		modulePreload: false,
		target: 'esnext',
		minify: false,
		cssCodeSplit: false
	},
});
