<template>
  <div class="columns">
    <div class="column">
      <vue-waterfall-easy
          class="myPosition"
          :imgsArr="imgsArr"
          @scrollReachBottom="getData"
          :gap="5"
          :mobileGap="20"
          :maxCols="3"
          :imgWidth=500
      >
        <div class="img-info">
          <p class="some-info">第n张图片</p>
          <p class="some-info">aaa</p>
        </div>
      </vue-waterfall-easy>
    </div>
  </div>
</template>

<script>
import vueWaterfallEasy from 'vue-waterfall-easy'

export default {
  name: "WatefallIndex",
  components: {
    vueWaterfallEasy
  },
  data(){
    return {
      waterFallContentWidth:document.body.clientWidth/5*4,
      TempWater:"500rem",
      imgsArr: [
        {id:1,src:"/TestIMG/blue.jpg"},
        {id:2,src:"/TestIMG/football01.jpg"},
        {id:3,src:"/TestIMG/breakfast.jpg"},
        {id:4,src:"/TestIMG/football02.jpg"},
        {id:5,src:"/TestIMG/rear.jpg"},
        {id:6,src:"/TestIMG/yellow-cups.jpg"},

      ],
    }
  },
  watch:{
    waterFallContentWidth(val){
      if(!this.timer){
        // 一旦监听到的screenWidth值改变，就将其重新赋给data里的screenWidth
        this.waterFallContentWidth = val/5*4
        this.timer = true
      }
    }
  },
  methods:{
    getData(){
      console.log("heihei")
    }
  }
}
</script>
<style scoped>
.vue-waterfall-easy-container[data-v-ded6b974] {
  position: absolute;
}
.vue-waterfall-easy-container .vue-waterfall-easy-scroll[data-v-ded6b974]{
  position: static!important;
}
</style>