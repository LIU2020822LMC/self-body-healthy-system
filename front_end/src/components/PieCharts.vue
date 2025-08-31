<template>
  <div class="line-main" :style="{ backgroundColor: bag }">
    <div ref="chart" :style="{ width: width, height: height }"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'PieChart',
  props: {
    types: {
      type: Array,
      default: () => [],
    },
    values: {
      type: Array,
      default: () => [],
    },
    width: {
      type: String,
      default: '100%',
    },
    tag: {
      type: String,
      default: '',
    },
    height: {
      type: String,
      default: '253px',
    },
    bag: {
      type: String,
      default: '#fff',
    },
    fontColor: {
      type: String,
      default: '#333',
    },
  },
  data() {
    return {
      chart: null,
    };
  },
  watch: {
    types() {
      this.initChart();
    },
    values() {
      this.initChart();
    },
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      if (this.chart) {
        this.chart.dispose();
      }
      this.chart = echarts.init(this.$refs.chart);

      const option = {
        title: {
          text: this.tag,
          subtext: '',
          left: 'center',
          textStyle: {
            color: this.fontColor,
          },
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          show: true,
          formatter: (name) => {
            const value = this.values[this.types.indexOf(name)];
            return `${name}: ${value}`;
          },
          textStyle: {
            color: this.fontColor,
          },
        },
        series: [
          {
            name: this.tag,
            type: 'pie',
            radius: ['40%', '70%'], // 调整内径和外径以适应更多数据
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'outside', // 将标签放置在外部以防止重叠
              formatter: '{b}: {d}%', // 显示名称和百分比
              color: this.fontColor,
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: true,
            },
            data: this.values.map((value, index) => ({
              name: this.types[index],
              value: value,
            })),
            itemStyle: {
              color: function (params) {
                const colorList = [
                  '#409EFF',
                  '#67C23A',
                  '#E6A23C',
                  '#F56C6C',
                  '#909399',
                  '#E4E7ED',
                  '#F2F6FC',
                ];
                return colorList[params.dataIndex % colorList.length];
              },
            },
          },
        ],
      };

      this.chart.setOption(option);
    },
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  },
};
</script>

<style scoped lang="scss">
.line-main {
  padding-top: 10px;
  margin-bottom: 5px;
  border-radius: 3px;
  background-color: v-bind(bag); /* 使用 v-bind 动态绑定背景颜色 */

  .tag {
    font-size: 14px;
    text-align: center;
    padding: 15px 6px;
    display: block;
  }
}
</style>