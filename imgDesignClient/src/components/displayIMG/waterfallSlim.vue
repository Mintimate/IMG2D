<template>
  <div>
    <div v-if="listIMG.length!=0">
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
                <img :src="item.src" @load="onLoad(index)" style="object-fit: cover;max-height:8em"/>
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
    </div>
    <div class="message is-white" style="text-align: center" v-else>
      <p class="media-content is-center">
        空空如也(･_･;
      </p>
    </div>
  </div>

</template>

<script>
import {backToTop} from "@/untils/backToTop";

export default {
  name: "waterfallSlim",
  props: ["images_info"],
  data() {
    return {
      isLoading: true,
      listIMG:[
        // {
        //   id: "",
        //   src: "",
        //   title: "",
        //   author: "",
        //   authorAvatarPath: ""
        // }
      ],
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
  watch: {
      images_info: {
        handler(newVal) {
          console.log(newVal);
          if (newVal.length>0){
            this.listIMG = newVal
          }
        },
        deep:true,
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
    width: calc(100% / 4 - 32px);
    margin: 15px;
  }
}

@media screen and (min-width: 1440px) {
  .item {
    width: calc(100% / 5 - 32px);
  }
}

@media screen and (max-width: 768px) {
  .media-content {
    overflow-x: inherit;
  }
}

</style>
