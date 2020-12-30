module.exports = {
    "pages": {
        "index": {
            "entry": "src/app.js",
            "template": "public/index.html"
        }
    },
    outputDir: "build/dist",
    devServer: {
        port: 5000,
        proxy: {
            '/queryDataWithPrefix': {
                target: 'http://localhost:8080',
                changeOrigin: true
            }
        }
    }
}