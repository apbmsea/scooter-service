const { execSync } = require('child_process');

const publicTsFiles = [
    'src/public/mfe-manager.ts',
    'src/public/axios-instance.ts', 
    'src/public/router.ts'
];

publicTsFiles.forEach(file => {
    try {
        execSync(`npx tsc ${file} --target ES2020 --module none --outDir src/public --skipLibCheck`, { stdio: 'inherit' });
    } catch (error) {
        console.error(`Ошибка компиляции публичных файлов ${file}:`, error.message);
    }
});