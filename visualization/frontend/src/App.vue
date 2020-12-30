<template>
  <el-container id="app" direction="vertical">
    <Header></Header>
    <el-main>
      <router-view></router-view>
    </el-main>
    <Footer></Footer>
  </el-container>
</template>

<script>
import Header from "./components/Header.vue";
import Footer from "./components/Footer.vue";

export default {
  name: "App",
  components: {Header, Footer},
  data() {
    return {};
  },
}
</script>

<style>
#app {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif
}

.dev-bordered {
  background-color: aquamarine;
}
</style>
