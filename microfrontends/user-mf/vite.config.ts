import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';
import path from 'path';

export default defineConfig({
	plugins: [
		react(),
		federation({
			name: 'user-mf',
			filename: 'remoteEntry.js',
			remotes: {
				sharedMF: 'https://localhost:5002/assets/remoteEntry.js'
			},
			exposes: {
				'./App': './src/app/App'
			},
			shared: [
				'react-router-dom',
				'axios',
				'react',
				'react-dom',
				'react-redux',
				'@reduxjs/toolkit',
				'redux-saga',
				'typed-redux-saga'
			]
		})
	],
	build: {
		modulePreload: false,
		target: 'esnext',
		minify: false,
		cssCodeSplit: false
	},
	resolve: {
		alias: {
			'@': path.resolve(__dirname, './src'),
			'@app': path.resolve(__dirname, 'src/app'),
			'@entities': path.resolve(__dirname, 'src/entities'),
			'@features': path.resolve(__dirname, 'src/features'),
			'@pages': path.resolve(__dirname, 'src/pages'),
			'@shared': path.resolve(__dirname, 'src/shared'),
			'@widgets': path.resolve(__dirname, 'src/widgets')
		}
	}
});
