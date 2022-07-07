<template>
  <div class="modal-card" style="width: auto">
    <header class="modal-card-head">
      <p class="modal-card-title">用户验证</p>
    </header>
    <section class="modal-card-body">
      <form>
        <b-field label="选择认证的身份"
                 class="is-fullwidth"
                 label-position="on-border">
          <b-select
            v-model="identWhat"
            placeholder="选择认证的身份">
            <option value="摄影师">摄影师</option>
            <option value="艺术家">艺术家</option>
            <option value="灵魂画师">灵魂画师</option>
          </b-select>
        </b-field>
        <b-field label="请选择证明材料">
          <chooseIMG v-bind:dropFiles.sync="identImage"/>
        </b-field>
      </form>
    </section>
    <footer class="modal-card-foot">
      <b-button
        label="取消"
        @click="$emit('updateFinish')"
      />
      <b-button
        :disabled="submitButtonDisabled"
        @click="handleSubmit"
        label="提交"
        type="is-primary"/>
    </footer>
  </div>
</template>

<script>
import chooseIMG from "@/components/imgTool/choseIMG"
import {uploadFile} from "@/untils/tencentCOS";
import {successTips, warningTips} from "@/untils/tipsInfoUntil";
import {isEmpty} from "@/untils/valueUntil";
import {post} from "@/untils/request";

export default {
  name: "ident.vue",
  components: {chooseIMG},
  data() {
    return {
      identWhat: null,
      identImage: null,
    }
  },
  computed: {
    submitButtonDisabled() {
      if (this.identWhat == undefined || this.identImage == undefined) {
        return true
      }
      return false
    },
  },
  methods: {
    handleSubmit() {
      if (isEmpty(this.identImage)) {
        warningTips("验证图片图怎么能少呢？")
        return;
      }
      const file = this.identImage
      const filenameWithTime = new Date().getTime() + file.name
      const url = uploadFile(file, filenameWithTime)
      const identVO = {
        identWhat: this.identWhat,
        identURL: url
      }
      post("/img2d/userIdent/addIdent", identVO).then(({data}) => {
        if (data.flag) {
          successTips("上传成功，请等待管理员审核")
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
