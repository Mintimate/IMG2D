<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改推荐文章</p>
    </header>
    <section class="modal-card-body">
      <b-field label="优先级">
        <b-select
          v-model="newRecommendArticleInfo.topId"
          placeholder="选择优先级（越高越优先）">
          <option
            v-for="item in topIdList"
            :value="item"
            :key="item">
            {{ item }}
          </option>
        </b-select>
      </b-field>
      <b-field label="文章标题">
        <b-input
          @input="changeTittle()"
          v-model="newRecommendArticleInfo.articleTitle"
          placeholder="输入文章标题"/>
      </b-field>
      <b-field>
        <b-button
          expanded
          :disabled="checkButton"
          rounded
          @click="checkTitle"
          size="is-small"
          type="is-primary"
          label="校验数据"
        />
      </b-field>
        <b-field
          label="文章悬浮描述"
          label-position="on-border">
          <b-input
            :disabled="newRecommendArticleInfo.articleDesc.length==0"
            v-model="newRecommendArticleInfo.articleDesc"
            maxlength="150" type="textarea"/>
        </b-field>
    </section>

    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
        :disabled="!checkButton||newRecommendArticleInfo.articleDesc.length==0"
        @click="updateFinish"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>

</template>

<script>
import {post} from "@/untils/request"
import {successTips, warningTips} from "@/untils/tipsInfoUntil";
export default {
  name: "setRecommendPost",
  props:['choseItemId','choseItem'],
  data() {
    return {
      newRecommendArticleInfo: {
        topId: "",
        id: "",
        username: "",
        articleTittle: "",
        articleDesc: ""
      },
      checkButton:true,
      topIdList: [1, 2, 3, 4, 5]
    }
  },
  mounted() {
    this.newRecommendArticleInfo=this.choseItem
  },
  methods:{
    changeTittle(){
      this.checkButton=false
    },
    checkTitle(){
      post("/img2d/article/admin/getArticleBaseByTitle",this.newRecommendArticleInfo).then(({data})=>{
        this.checkButton=true
        if(data.flag){
          this.newRecommendArticleInfo.articleDesc=data.data.articleDesc
          successTips("校验成功，可以自定义'悬浮描述'")
        }
        else {
          this.newRecommendArticleInfo.articleDesc=""
          warningTips("校验失败，并没有这篇文章")
        }
      })
    },
    updateFinish(){
      this.$emit("success",this.newRecommendArticleInfo,this.choseItemId)
    }
  }
}
</script>

<style scoped>

</style>
