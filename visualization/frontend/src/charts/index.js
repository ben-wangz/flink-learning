import Vue from 'vue'
import ECharts from 'vue-echarts'
// 按需引用所需图表组件
import 'echarts/lib/chart/line'
import 'echarts/lib/chart/bar'

Vue.component('chart', ECharts)