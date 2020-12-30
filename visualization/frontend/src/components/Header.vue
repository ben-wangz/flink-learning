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
      <el-col :span="2" class="footer_column_size">
        <a class="text_center" style="color: #42b983" href="#">About</a>
      </el-col>
      <el-col :span="2" class="footer_column_size">
        <a class="text_center" style="color: #42b983" href="#">Data Source</a>
      </el-col>
      <el-col :span="2" class="footer_column_size">
        <a class="text_center" style="color: #42b983" href="#">Help</a>
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
  display: block;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0;
  margin-inline-end: 0;
  text-align: center;
}

.footer_column_size {
  height: 50px;
}
</style>
