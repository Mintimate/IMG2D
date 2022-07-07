<template>
  <section class="is-vcentered is-centered">
    <div class="columns">
      <div class="column is-3">
        <div class="content is-small">
          <article class="block">
            <h5 class="heading">
              支持的图片类型 </h5>
            <div class="types block">
              <ul>
                <li>
                  支持：JPG，PNG，WEBP，BMP格式
                </li>
                <li class="">
                  支持：最大30MB的图片/照片（图床图片5MB）
                </li>
              </ul>
            </div>
          </article>
          <hr/>
          <article class="block">
            <h5 class="heading">
              关于审核 </h5>
            <ul>
              <li>
                图床图片：图床图片为私有图片，不会在分类、首页等公共板块展示。<strong>普通用户空间为500MB,图片合法性由系统自动审核。</strong>
              </li>
              <li class="">
                公开图片：公开图片会在分类和首页等板块展示，需要有一定的类别和标签。<strong>不限空间，图片合法性由管理员审核。</strong>
              </li>
            </ul>
          </article>
          <hr/>
          <article class="block">
            <h5 class="heading">
              分类和标签 </h5>
            <ul>
              <li>
                图片分类：<strong>一个图片只能所属于一个分类下</strong>；如果分类归属错误、没有自己需要的分类，可以联系管理员协助嗷。
              </li>
              <li class="">
                图片标签：<strong>一个图片可以打上多个标签（当前最多7个）。</strong>标签可以自定义嗷。
              </li>
            </ul>
          </article>
        </div>
      </div>

      <div class="column is-9">
        <b-loading v-model="isLoading" :can-cancel="false"></b-loading>
        <div class="columns">
          <choseIMG v-bind:dropFiles.sync="dropFiles" class="column"/>
        </div>

        <div class="columns">
          <b-field class="column" label="选择模式">
            <b-select
              v-model="userMode"
              placeholder="选择存储模式"
              rounded
              expanded
              size="is-small"
            >
              <option selected value="public">图库（公开）</option>
<!--              <option value="private">图床（私有）</option>-->
            </b-select>
          </b-field>
          <template v-if="userMode=='private'">
            <b-field class="column" label="个人图库空间">
              <b-progress :value="75" size="is-large" type="is-primary" show-value>
                75 / 100
              </b-progress>
            </b-field>
          </template>
          <template v-else>
            <b-field class="column" label="选择分类">
              <b-select
                v-model="imgCategory"
                placeholder="选择图片分类"
                rounded
                expanded
                size="is-small"
              >
                <option v-for="item in imgCategoryList" :key="item.id" :value="item.id">{{ item.cateName }}</option>
              </b-select>
            </b-field>
          </template>
        </div>
        <infoIMG v-bind:dropFiles.sync="dropFiles"
                 v-bind:img-tags="returnImgTags"
                 v-bind:imgFriendName.sync="imgFriendName"/>
        <div class="columns">
          <div class="column">
            <div class="buttons">
              <b-button :disabled="uploadButtonReady" @click="handleSubmit" type="is-primary" rounded expanded>上传
              </b-button>
            </div>
          </div>
        </div>
      </div>
    </div>

  </section>

</template>

<script>
import {get, post} from "../untils/request";
import choseIMG from "../components/imgTool/choseIMG"
import infoIMG from "../components/imgTool/infoIMG"
import {successTips, warningTips} from "@/untils/tipsInfoUntil";

export default {
  name: "userUploadIMG",
  components: {choseIMG, infoIMG},
  data() {
    return {
      returnImgTags: [],
      isLoading: false,
      dropFiles: null,
      imgFriendName: "",
      userMode: "public",
      uploadButtonReady: true,
      imgCategory: "",
      imgCategoryList: [
        {
          id: "",
          cateName: ""
        }
      ],
    }
  },
  mounted() {
    get("/img2d/imgCate/getAllCategory")
      .then(({data}) => {
        this.imgCategory = data.data[0].id
        this.imgCategoryList = data.data
      })
  },
  watch: {
    dropFiles(After) {
      console.log(After);
      After != null ? this.uploadButtonReady = false : this.uploadButtonReady = true;
    }
  },
  methods: {
    handleSubmit() {
      this.isLoading = true
      const formData = new FormData();
      formData.append("IMG_Raw", this.dropFiles);
      formData.append("IMG_Name", this.imgFriendName + "." + this.dropFiles.name.split('.').pop().toLowerCase())
      formData.append("CateId", this.imgCategory)
      post("/img2d/img/upload", formData)
        .then(({data}) => {
          if (data.flag){
            successTips("上传成功，请等待审核")
            console.log(data.data)
            this.returnImgTags = data.data.tagDTOList
          }
          else{
            warningTips("处理失败，系统内已经有此图片")
          }
          console.log(data)

        })
        .finally(() => {
          this.isLoading = false
        })
    },
    handleSubmitIMG() {
      this.isLoading = true
      const formData = new FormData();
      formData.append("imgRaw", this.dropFiles);
      formData.append("imgName", this.imgFriendName + "." + this.dropFiles.name.split('.').pop().toLowerCase())
      formData.append("cateId", this.imgCategory)
      post("/img2d/img/newUpload", formData).then(({data}) => {
        console.log(data)
      }).finally(this.isLoading = false
      )
    }
  },
}
</script>

<style scoped>

</style>
