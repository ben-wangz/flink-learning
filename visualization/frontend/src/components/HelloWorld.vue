<template>
  <div>
    <el-upload
        class="upload-demo"
        action="https://jsonplaceholder.typicode.com/posts/"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :before-remove="beforeRemove"
        multiple :limit="1"
        :on-exceed="handleExceed"
        :file-list="fileList">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
    </el-upload>
    <el-input placeholder="请输入内容" v-model="imageUri"></el-input>
    <el-image :src="imageUri" fit="contain">
    </el-image>
    <router-link to="/flink-chart">查看图表</router-link>
    <div>API RETURN: {{ apiContent }}</div>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String,
  },
  data() {
    return {
      apiContent: '',
      fileList: [],
      imageUri: "https://ci.apache.org/projects/flink/flink-docs-release-1.12/page/img/navbar-brand-logo.jpg",
    }
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
      let reader = new FileReader();
      reader.onload = function (e) {
        this.imageUri = e.target.result
      }
      reader.readAsDataURL(file);
      console.log(file)
    },
    handleExceed(files) {
      console.log(files[0])
    },
    beforeRemove(file, fileList) {
      console.log(fileList)
      return this.$confirm(`确定移除 ${file.name}？`);
    }
  },
  created() {
    const self = this
    fetch('/api/greeting?username=' + randomString(8))
        .then(function (response) {
          return response.text();
        }).then(function (res) {
      console.log(res)
      self.apiContent = res
    }).catch((e) => {
      console.log(e)
    })
  }
}

function randomString(length) {
  var result = '';
  var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  var charactersLength = characters.length;
  for (var i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
a {
  color: #42b983;
}
</style>
