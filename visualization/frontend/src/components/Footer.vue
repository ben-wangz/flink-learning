<template>
  <el-footer style="border-radius: 5px">
    <el-row>
      <el-col :span="2" class="footer_column_size">
        <a class="text_center" style="color: #42b983" href="#">Business</a>
      </el-col>
      <el-col :span="20" class="footer_column_size">
        <a class="text_center" style="color: #42b983" href="#">Best Practise</a>
      </el-col>
      <el-col :span="2" class="footer_column_size">
        <a class="text_center" style="color: #42b983" href="#">Settings</a>
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
