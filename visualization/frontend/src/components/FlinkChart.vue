<template>
  <div class="flink-chart">
    <chart ref="barChart" :options="chartOptions" :auto-resize="true"></chart>
  </div>
</template>

<script>
import axios from 'axios'
const URL = '/queryDataWithPrefix?namePrefix=MeteorologicalDataStream_';
const baseOption = {
    title: {
        text: 'Flink动态数据'
    },
    tooltip: {},
    legend: {
        data:['时间']
    },
    xAxis: {
      type: 'category',
      data: []
    },
    yAxis: {},
    series: [{
        name: '值',
        type: 'line',
        data: []
    }]
};
let __interval = 2000;
let __timer;

function preProcess(res) {
  let x = [];
  let y = [];
  Object.keys(res.data)
    .sort((a,b) => +a - +b)
    .forEach(key => {
      x.push(key);
      y.push(res.data[key]);
    })
  return {x, y};
}
export default {
  name: 'FlinkChart',
  data() {
    return { chartOptions: baseOption }
  },
  mounted() {
    this.init();
  },
  unmounted() {
    clearInterval(__timer);
  },
  methods: {
    updateChart({x, y}) {
      baseOption.xAxis.data = x;
      baseOption.series[0].data = y;
      this.chartOptions = baseOption;
    },
    fetchData(limit = 100) {
      return axios.get(URL + `&limit=${limit}`).then(res => res.data)
    },
    init() {
      this.$refs.barChart.showLoading();
      this.fetchData().then(res => {
        if (Object.keys(res.data).length) {
          this.$refs.barChart.hideLoading();
          this.updateChart(preProcess(res));
          Object.keys(res.data).length && this.doTimer();
        } else {
          window.alert('无数据');
        }
      })
    },
    doTimer() {
      __timer = setInterval(() => {
        this.fetchData().then(res => {
          if (Object.keys(res.data).length) {
            this.updateChart(preProcess(res));
          }
        })
      }, __interval);
    }
  }
}
</script>
<style scoped>
.flink-chart {
  text-align: center;
}
.echarts {
  margin: 0 auto;
  width: 800px;
  height: 600px;
}

</style>