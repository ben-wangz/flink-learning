<template>
  <el-footer>
    <el-row>
      <el-col :span="2" class="text_center footer_column_size">
        <a href="#">
          <p style="color: #42b983">Business</p>
        </a>
      </el-col>
      <el-col :span="20" class="text_center footer_column_size">
        <a href="#">
          <p style="color: #42b983">Best Practise</p>
        </a>
      </el-col>
      <el-col :span="2" class="text_center footer_column_size">
        <a href="#">
          <p style="color: #42b983">Settings</p>
        </a>
      </el-col>
    </el-row>
  </el-footer>
</template>
<script>
export default {
  name: "Footer",
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
