<template>
  <div>
    <b-loading v-model="isLoading" :is-full-page="false" :can-cancel="false"></b-loading>
    <div
      v-masonry="'containerId'"
      transition-duration="1.5s"
      item-selector=".item"
      class="masonryWrap">
      <div
        v-for="(item, index) in listIMG"
        :key="index"
        v-masonry-tile
        class="masonryWrap_item item"
      >
        <router-link
          :to="`/imgDetail/${(item.id)}`">
          <div class="card" @click="backToTop">
            <div class="card-image">
              <img :src="item.src" @load="onLoad(index)" style="object-fit: cover;max-height:18em"/>
            </div>
            <div class="card-content">
              <div class="media">
                <div class="media-left is-bottom">
                  <figure class="image is-32x32">
                    <img class="is-rounded" :src="'/img2d/'+item.authorAvatarPath" alt="Placeholder image">
                  </figure>
                </div>
                <div class="media-content">
                  <p class="title is-6">{{ item.title }}</p>
                  <p class="subtitle is-7">{{ item.author }}</p>
                </div>
              </div>
            </div>
          </div>
        </router-link>

      </div>
    </div>
    <!--    <div class="card">-->
    <!--      <p class="card-header">开发者调试按钮</p>-->
    <!--      <b-button-->
    <!--          @click="addIMG"-->
    <!--          type="is-primary is-light">点击添加图片-->
    <!--      </b-button>-->
    <!--      <b-button-->
    <!--          @click="onLoad"-->
    <!--          type="is-primary is-dark">点击刷新图片-->
    <!--      </b-button>-->
    <!--    </div>-->

  </div>

</template>

<script>
import {get} from "@/untils/request"
import {productSnowKey} from "@/untils/snowKey";
import {backToTop} from "@/untils/backToTop";

export default {
  name: "imageWaterFallDisplay",
  props: ["images_info"],
  data() {
    return {
      isLoading: true,
      listIMG:[],
    }
  },
  methods: {
    onLoad(item) {
      this.$redrawVueMasonry('containerId')
      if (item>=this.listIMG.length-1) {
        this.$nextTick(()=>{
          this.isLoading=false
        })
      }
    },
    backToTop
  },
  mounted() {
    if (this.images_info==null||this.images_info==undefined){
      get("/img2d/img/getRandomList/12")
      .then(({data})=>{
        const tempList=[]
        const list = data.data
        list.forEach(item=>{
          tempList.push(
            {
              id: productSnowKey(item.id),
              src: this.$store.state.apiHost + item.imgThumbnailPath,
              title: item.imgName,
              author: item.username,
              authorAvatarPath:item.userAvatarPath
            }
          )
        })
        this.listIMG=tempList
      })
    }
  },
  watch: {
      images_info: {
        handler(newVal) {
          if (newVal==undefined||newVal.length==0){
            this.isLoading=false
          }
          this.listIMG = newVal
        },
        deep:true,
        immediate:true
      }
  }
}
</script>

<style scoped>
/*分割线*/
.masonryWrap {
  margin: auto;
  min-height: auto;
  width: 100%;
}

.item {
  width: 100%;
  margin: 0 0 8px 0;
}

.item img {
  display: block;
  width: 100%;
}

@media screen and (min-width: 768px) {
  .item {
    width: calc(100% / 3 - 32px);
    margin: 15px;
  }
}

@media screen and (min-width: 1440px) {
  .item {
    width: calc(100% / 4 - 32px);
  }
}

@media screen and (max-width: 768px) {
  .media-content {
    overflow-x: inherit;
  }
}

</style>
