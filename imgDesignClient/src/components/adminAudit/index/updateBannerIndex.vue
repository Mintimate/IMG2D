<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">修改头图</p>
    </header>
    <section class="modal-card-body">
      <b-field label="优先级">
        <b-select
          v-model="newBannerIndex.topId"
          placeholder="选择优先级（越高越优先）">
          <option
            v-for="item in topIdList"
            :value="item"
            :key="item">
            {{ item }}
          </option>
        </b-select>
      </b-field>
      <b-field label="图片地址">
        <b-input
          v-model="newBannerIndex.imgUrl"
          placeholder="输入图片地址"/>
      </b-field>
      <b-field
        label="主标题"
        label-position="on-border">
        <b-input
          v-model="newBannerIndex.title"/>
      </b-field>
      <b-field
        label="副标题"
        label-position="on-border">
        <b-input
          v-model="newBannerIndex.subtitle"/>
      </b-field>
      <b-field
        label="跳转链接"
        label-position="on-border">
        <b-input
          v-model="newBannerIndex.url"/>
      </b-field>
    </section>

    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('close')"
      />
      <b-button
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
  name: "updateBannerIndex",
  props: ['sourceList', 'checkItem'],
  data() {
    return {
      newBannerIndex: {
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
      },
      bakSourceList:[],
      sourceCheckItem: {},
      checkButton: true,
      topIdList: [1, 2, 3, 4, 5]
    }
  },
  mounted() {
    this.bakSourceList=this.sourceList
    this.sourceCheckItem = this.checkItem[0]
    this.newBannerIndex = this.checkItem[0]
  },
  methods: {
    updateFinish() {
      let newList=[]
      this.bakSourceList.forEach(item=>{
        if (item==this.checkItem){
          newList.push(this.newBannerIndex)
        }
        else {
          newList.push(item)
        }
      })
      post("/img2d/systemctl/saveBannerIndex",newList).then(({data})=>{
        if (data.flag) {
          successTips("更新成功，获取最新数据")
        } else {
          warningTips("尴尬，更新失败：数据异常")
        }
        this.$emit("success")
      })
    }
  }
}
</script>

<style scoped>

</style>
