const { getConfigs } = require('./getConfigs');
const envConfig = require('../configs/env.config');

function renderHTML(path) {
	const config = getConfigs(path);

	return `
        <!DOCTYPE html>
        <html lang="ru">
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                ${config.css}
                <title>${config.title || 'Самокат'}</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                        scroll-behavior: smooth;
                        font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
                        Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
                    }
                    html, body {
                        height: 100%;
                        width: 100%;
                    }
                    body {
                        display: flex;
                        flex-direction: column;
                    }
                    #app {
                        display: flex;
                        flex-direction: column;
                        flex: 1;
                        min-height: 0;
                    }
                    #app > div[id*="header"] {
                        flex-shrink: 0;
                    }
                    #app > div:not([id*="header"]) {
                        flex: 1;
                        min-height: 0;
                        display: flex;
                        flex-direction: column;
                    }
                    header {
                        flex-shrink: 0;
                    }
                </style>
            </head>
            <body>
                <div id="app">
                    ${config.rootDivs}
                </div>

                ${config.js}
                ${config.mountScripts}
                
                <script src="https://cdn.jsdelivr.net/npm/axios@1.13.2/dist/axios.min.js"></script>
                <script src="/axios-instance.js"></script>
                <script src="/mfe-manager.js"></script>
                <script src="/router.js"></script>
            </body>
        </html>
    `;
}

module.exports = {
	renderHTML
};
