<template>
  <div>
    <div class="card mt-3">
      <header class="card-header">
        <p class="is-centered card-header-title is-size-5">
          {{ label }}详情
        </p>
      </header>
      <div class="card-content">
        <waterfallSlim :images_info="imgList"/>
      </div>
    </div>
    <div class="box mt-3">
      <b-pagination
        :total="total"
        v-model="current"
        @change="changeCurrent"
        order="is-centered"
        per-page=8
        aria-next-label="Next page"
        aria-previous-label="Previous page"
        aria-page-label="Page"
        aria-current-label="Current page">
      </b-pagination>
    </div>
  </div>
</template>

<script>
import waterfallSlim from "@/components/displayIMG/waterfallSlim"
import {backToTop} from "@/untils/backToTop";
import {get} from "@/untils/request"
import {productSnowKey} from "@/untils/snowKey";
export default {
  name: "spaceImageDetailCard",
  props: ["label", "type"],
  components:{
    waterfallSlim
  },
  data(){
    return{
      current:1,
      total:8,
      imgList:[]
    }
  },
  created() {
    if (this.type=="Collect"&&this.$route.params.userId==this.$store.state.loggedUserId){
      this.getCollectImages()
    }
  },
  methods:{
    changeCurrent(){
      backToTop()
      this.getCollectImages()
    },
    getCollectImages(){
      get("/img2d/img/userSpace/"+this.type+"/"+this.current).then(({data})=>{
        if (data.flag) {
          this.total=data.data.count
          const tempList = []
          const list = data.data.recordList
          list.forEach(item => {
            tempList.push(
              {
                id: productSnowKey(item.id),
                src: this.$store.state.apiHost + item.imgThumbnailPath,
                title: item.imgName,
                author: item.username,
                authorAvatarPath: item.userAvatarPath
              }
            )
          })
          this.imgList = tempList
        }
      })
    },
  },
}
</script>

<style scoped>

</style>
