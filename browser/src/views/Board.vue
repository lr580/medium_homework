<template>
    <div v-html="stateInfo"></div>
</template>
<script scoped>
import { getBoardState } from '@/api'
export default {
    data() {
        return {
            stateInfo: '请求中……',
        }
    },
    cycler: 0,
    async mounted() {
        if (this.cycler) {
            clearInterval(this.cycler);
            this.cycler = 0;
        }
        let that = this;
        var data = await getBoardState();
        that.stateInfo = data.data.data;
        this.cycler = setInterval(async function () {
            var data = await getBoardState();
            that.stateInfo = data.data.data;
        }, 998);//整秒容易延期误差，导致看起来两秒才更一次
    }
}
</script>