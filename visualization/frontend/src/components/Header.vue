<template>
  <el-header>
    <el-row>
      <el-col :span="1" class="footer_column_size">
        <el-image
            style="width: 50px;"
            :src="require('../assets/logo.png')"
            fit="contain">
        </el-image>
      </el-col>
      <el-col :span="2" class="text_center footer_column_size">
        <a href="#">
          <p style="color: #42b983">About</p>
        </a>
      </el-col>
      <el-col :span="2" class="text_center footer_column_size">
        <a href="#">
          <p style="color: #42b983">Data Source</p>
        </a>
      </el-col>
      <el-col :span="2" class="text_center footer_column_size">
        <a href="#">
          <p style="color: #42b983">Help</p>
        </a>
      </el-col>
    </el-row>
  </el-header>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {};
  },
}
</script>

<style>
.text_center {
  text-align: center;
}

.footer_column_size {
  height: 50px;
}
</style>
