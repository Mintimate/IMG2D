<template>
  <div>
    <b-carousel
      pause-text="悬浮中"
      arrow
      indicator-mode="click"
      indicator-position="is-bottom"
      indicator-style="is-dots">
      <b-carousel-item v-for="(carousel, i) in carousels" :key="i">
        <section class="hero is-medium">
          <img :src="carousel.imgUrl" style="object-fit: cover;max-height: 42em"/>
          <a :href="carousel.url" target="_blank">
            <div class="carousel-caption">
              <h1 class=" is-size-5">
                <strong style="color: #ffffff">{{ carousel.title }}</strong>
              </h1>
              <p class="">{{ carousel.subtitle }}</p>
            </div>
          </a>
        </section>
      </b-carousel-item>
    </b-carousel>
  </div>
</template>

<script>
import {get} from "@/untils/request"

export default {
  name: "homeHeader",
  data() {
    return {
      testIMG: require("@/assets/indexBanner/index01.webp"),
      carousels: [
        {
          /**
           * 优先级别
           */
          topId: "",
          /**
           * 图片地址
           */
          imgUrl: "",
          /**
           * 点击跳转地址
           */
          url: "",
          /**
           * 主标题
           */
          title: "",
          /**
           * 副标题
           */
          subtitle: "",
        }
      ]
    }
  },
  mounted() {
    get("/img2d/systemctl/getBannerIndex").then(({data}) => {
      this.carousels = data.data
    }).catch(() => {
      this.carousels = [
        {
          topId: 3,
          title: 'Mintimate\'s Blog',
          subtitle: 'Mintimate的博客，只为与你分享。不定期分享干货。',
          imgUrl: require("@/assets/indexBanner/index01.webp"),
          url: "https://www.mintimate.cn"
        },
        {
          topId:2,
          title: 'Mintimate\'s Bilibili',
          subtitle: '本站官方Bilibili账号，分享一些使用教程和干货～',
          imgUrl: require("@/assets/indexBanner/index02.webp"),
          url: "https://space.bilibili.com/355567627"
        },
        {
          topId:1,
          title: 'Mintimate\'s Github',
          subtitle: 'Github……',
          imgUrl: require("@/assets/indexBanner/index03.webp"),
          url: "https://github.com/Mintimate"
        },
      ]
    })
  }
}
</script>
<style scoped>
.carousel-caption >>> {
  text-shadow: 0 0 0.2em #424242;
  position: absolute;
  right: 15%;
  bottom: 2rem;
  left: 15%;
  z-index: 10;
  padding-top: 20px;
  padding-bottom: 20px;
  color: #fff;
  text-align: center;
}
</style>
