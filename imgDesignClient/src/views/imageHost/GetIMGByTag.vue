<template>
  <section class="box mt-5">
    <div class="columns">
      <div class="column">
        <div class="columns">
          <div class="column">
            <b-field>
              <b-taglist>
                <a v-for="(item,index) in TagNames" :key="index"
                   @click="chooseTag(item)"
                   :class="getRandomClass(index)"
                   class="tag  is-rounded">
                      <span class="icon">
                      <i class="fas fa-tag mr-1" style="font-size: 0.5rem"></i>
                      </span>
                  {{ item }}
                </a>
              </b-taglist>
            </b-field>
            <br/>
            <b-field>
              <b-taginput
                v-model="Tags"
                autocomplete
                allow-new
                field="user.first_name"
                icon="label"
                maxtags="3"
                placeholder="添加查询的标签">
              </b-taginput>
            </b-field>
            <b-field>
              <button
                @click="searchImagesByTags()"
                :disabled="Tags.length==0"
                class="button is-primary is-fullwidth is-rounded">搜索</button>
            </b-field>
          </div>
        </div>
      </div>
    </div>
    <div class="columns">
      <div class="column">
        <image-water-fall-display :images_info="IMG_LIST"/>
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
    </div>
  </section>
</template>

<script>
import imageWaterFallDisplay from "@/components/displayIMG/imageWaterFallDisplay";
import {get, post} from "@/untils/request"
import {warningTips} from "@/untils/tipsInfoUntil";
import {productSnowKey} from "@/untils/snowKey";

export default {
  name: "GetIMGByTag",
  components: {imageWaterFallDisplay},
  data() {
    return {
      TagNames: [],
      Tags: [],
      IMG_LIST:[],
      total:0,
      current:1
    }
  },
  mounted() {
    let searchTagName=this.$route.query.tagName;
    if (searchTagName!=undefined){
      this.Tags.push(searchTagName)
    }
    this.getTagNames()
  },
  watch: {
    'current':function(newValue){
      this.searchImagesByTags()
      console.log(newValue)
    },
  },
  methods: {
    getRandomClass(seed) {
      let randomClass = ["is-info", "is-success", "is-light", "is-warning", "is-danger", "is-link"]
      let index = Math.floor(('0.' + Math.sin(seed).toString().slice(6)) * randomClass.length);
      return randomClass[index]
    },
    getTagNames() {
      get("/img2d/imgTag/getAllTagNames").then(({data}) => {
        if (data.flag) {
          this.TagNames = data.data
        }
      })
    },
    chooseTag(tag) {
      if (this.Tags.indexOf(tag) != -1) {
        this.Tags = this.Tags.filter(item => item != tag)
      } else if (this.Tags.length >= 3) {
        warningTips("最多同时查询三个标签哦～")
      } else {
        this.Tags.push(tag)
      }
    },
    searchImagesByTags(){
      post("/img2d/imgTag/getImagesByTagNames/"+this.current,this.Tags).then(({data})=>{
        let list=data.data.recordList
        this.total=data.data.count
        console.log(data.data);
        let IMG_LIST=[]
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
        this.IMG_LIST=IMG_LIST
      })
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
  }
}
</script>

<style scoped>

</style>
