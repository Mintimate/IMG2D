module.exports = {
  devServer: {
    proxy: {
      "/img2d": {
        target: "http://localhost:8088",
        changeOrigin: true,
        pathRewrite: {
          "^/img2d": ""
        }
      }
    },
    disableHostCheck: true
  },
}
