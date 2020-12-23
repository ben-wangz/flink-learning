module.exports = {
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