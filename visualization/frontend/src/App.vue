<template>
  <el-container id="app">
    <el-header class="dev-bordered">
      <!--TODO 这里需要调整一下样式-->
      <img alt="Vue logo" src="./assets/logo.png" style="height: 50px; margin-left: -500px">
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
    <el-footer class="dev-bordered">
    </el-footer>
  </el-container>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #409EFF;
  margin-top: 60px;
}

.dev-bordered {
  background-color: aquamarine;
}
</style>
