<template>
  <div class="sidebar-page card mt-3">
    <section class="sidebar-layout">
      <b-sidebar
        position="static"
        fullheight
        type="is-light"
        open
      >
        <div class="p-1">
          <div class="block">
            <img
                :src="logoIMG" width="100%"
            />
          </div>
          <b-menu class="is-custom-mobile">
            <b-menu-list label="分类">
              <b-menu-item icon="information-outline"
                           @click="changeCate('All')"
                           :active="initClassifyId==undefined||initClassifyId==''" label="所有图片"></b-menu-item>
              <b-menu-item v-for="item in cateList"
                           :key="item.id"
                           :label="item.cateName"
                           @click="changeCate(item.id)"
                           :active="item.id==initClassifyId">
              </b-menu-item>
            </b-menu-list>
          </b-menu>
        </div>
      </b-sidebar>

      <div class="m-2" style="width: 100%">
        <imageWaterFallDisplay :images_info="IMG_LIST"/>
        <b-pagination
          :total="total"
          v-model="current"
          @change="backToTop"
          order="is-centered"
          per-page=9
          aria-next-label="Next page"
          aria-previous-label="Previous page"
          aria-page-label="Page"
          aria-current-label="Current page">
        </b-pagination>
      </div>
    </section>
  </div>
</template>

<script>
import imageWaterFallDisplay from "@/components/displayIMG/imageWaterFallDisplay";
import {get} from "@/untils/request";
import {productSnowKey} from "@/untils/snowKey";

export default {
  name: "userDiscuss",
  components: {imageWaterFallDisplay},
  data() {
    return {
      logoIMG:require("@/assets/logo/classifyLogo.png"),
      initClassifyId:"",
      cateList: [
        {
          id: "",
          cateName: ""
        }
      ],
      // 以下为分页插件
      total: 10,
      perPage: 10,
      current: 1,
    }
  },
  created() {
    get("/img2d/imgCate/getAllCategory").then(({data})=>{
      this.cateList=data.data
    })
    if (this.$route.params.classifyId!=undefined){
      this.initClassifyId=this.$route.params.classifyId
    }
  },
  methods: {
    changeCate(cateId){
      this.initClassifyId=cateId
      this.current=1
    },
    getIMGS(currentPage) {
      let initClassifyId=this.initClassifyId==''?"All":this.initClassifyId
      let IMG_LIST = []
      get("/img2d/img/getImgByPage/"+ initClassifyId +"/"+ currentPage)
        .then(({data}) => {
          this.total = data.data.count
          const list = data.data.recordList
          for (const k in list) {
            IMG_LIST.push(
              {
                id: productSnowKey(list[k].id),
                src: this.$store.state.apiHost + list[k].imgThumbnailPath,
                title: list[k].imgName,
                author: list[k].username,
                authorAvatarPath:list[k].userAvatarPath
              }
            )
          }
        })
      return IMG_LIST
    },
    backToTop() {
      let timer = setInterval(function () {
        let osTop = document.documentElement.scrollTop || document.body.scrollTop;
        let isSpeed = Math.floor(-osTop / 5);
        const isPosition = osTop + isSpeed;
        document.documentElement.scrollTop = document.body.scrollTop = osTop + isSpeed;
        if (osTop === 0 || (document.documentElement.scrollTop) > isPosition || document.body.scrollTop > isPosition) {
          clearInterval(timer);
        }
      }, 50)
    },
  },
  computed: {
    IMG_LIST: function () {
      let list = this.getIMGS(this.current)
      return list
    }
  },
};
</script>

<style scoped>
.p-1 {
  padding: 1em;
}

.sidebar-page {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100%;
}

.sidebar-page .sidebar-layout {
  display: flex;
  flex-direction: row;
  min-height: 100%;
}

.is-mini-expand .menu-list a {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

</style>
