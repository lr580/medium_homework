<template>
    <div>
        <div class="container">
          <div>
            <center>
            <el-radio @click="loadGoodsPricesRange" class="grayFont" v-model="radio1" label="1" border>商品价格分布图</el-radio>
            <el-radio @click="loadCompaniesGeoRanges" class="grayFont" v-model="radio1" label="2" border>快递公司位置分布图</el-radio>
            </center>
          </div>
<!--            <div>当前图表：{{ nowTitle }}</div>-->
            <div id="echart"></div>
        </div>
    </div>
</template>
<script scoped>
import * as echarts from 'echarts';
import { getGoodsPricesRange, getCompaniesGeoRanges } from '@/api';
export default {
    data() {
        return {
          radio1: '1',
          nowTitle: '商品价格分布图',
          like: true,
          value1: 4154.564,
          value2: 2222,
          title: "今年的增长",
          input: "Hello Element UI!",
        };
    },
    mychart: null,
    // 页面初始化挂载dom
    mounted() {//this.getLoadEcharts();
        this.myChart = echarts.init(
            document.getElementById("echart")
        );
        this.loadGoodsPricesRange();
    },
    methods: {
        async loadGoodsPricesRange() {
            this.myChart.clear();
            this.nowTitle = '商品价格分布图';
            var option = {
                xAxis: {
                    data: ['1~10元', '11~100元', '101~1000元', '>=1001元']
                },
                yAxis: {},
                series: [
                    {
                        type: 'bar',
                        data: [0, 1, 0, 0]
                    }
                ]
            };
            var res = await getGoodsPricesRange();
            var rowData = res.data.data;
            option.series[0].data = rowData;
            this.myChart.setOption(option);
        },

        async loadCompaniesGeoRanges() {
            this.myChart.clear();
            this.nowTitle = '快递公司位置分布图';
            var option2 = {
                series: [{
                    type: 'pie',
                    data: [] //{value: , name: }
                }]
            };
            var res = await getCompaniesGeoRanges();
            var rowData = res.data.data;
            for (let i in rowData) {
                option2.series[0].data.push({
                    value: rowData[i],
                    name: i
                });
            }
            this.myChart.setOption(option2);
        }
    }
};
</script>
<style scoped>
.container {
    width: 500px;
    height: 400px;
    margin-left: 30px;
}

#echart {
    width: 100%;
    height: 100%;
}
.grayFont{
    color: grey;
}
</style>
