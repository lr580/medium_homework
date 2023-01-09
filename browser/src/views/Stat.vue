<template>
    <div>
        <div class="container">
            <div>商品价格分布图</div>
            <div id="echart"></div>
        </div>
    </div>
</template>
<script scoped>
import * as echarts from 'echarts';
import { getGoodsPricesRange } from '@/api';
export default {
    data() {
        return {};
    },
    // 页面初始化挂载dom
    mounted() {
        this.getLoadEcharts();
    },
    methods: {
        async getLoadEcharts() {
            var myChart = echarts.init(
                document.getElementById("echart")
            );
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
            myChart.setOption(option);
        }
    }
};
</script>
<style scoped>
.container {
    width: 500px;
    height: 300px;
    margin-left: 30px;
}

#echart {
    width: 100%;
    height: 100%;
}
</style>

<!-- <template>
    <div id="cv_1" style="width: 600px;height:400px;"></div>
</template>

<script>
import * as echarts from 'echarts'
var chart1 = echarts.init(document.getElementById('cv_1'));
// console.log(chart1);
var opt1 = {
    title: {
        text: '商品价格分布图',
    },
    tooltip: {},
    legend: {
        data: ['销量']
    },
    xAxis: {
        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
    },
    yAxis: {},
    series: [
        {
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
        }
    ]
};
// chart1.setOption(opt1);
</script> -->