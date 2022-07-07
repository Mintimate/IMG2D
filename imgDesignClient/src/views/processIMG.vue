<template>
  <section class="is-vcentered is-centered">
    <b-loading v-model="isLoading" :can-cancel="false"></b-loading>
    <b-field>
      <b-upload
        v-model="dropFiles"
        drag-drop
        expanded
        accept=".png, .jepg, .jpg, .webp, .bmp"
      >
        <section class="section">
          <div class="content has-text-centered">
            <p>
              <b-icon icon="upload" size="is-large"></b-icon>
            </p>
            <p>将图片拖到到此处，或点击此处选择图片</p>
            <p>.jpg、.png、.webp、.bmp</p>
            <div class="tags" style="text-align: center;display: inherit">
              <p v-if="dropFiles!=null" class="tag is-primary is-large">
                <i class="iconfont icon-photo  mr-2"></i>
                {{ dropFiles.name }}
                <button class="delete is-small" type="button" @click="deleteDropFile()"></button>
              </p>
            </div>
          </div>
        </section>
      </b-upload>
    </b-field>
    <div class="columns">
      <b-field class="column" label="转码格式">
        <b-select
          v-model="targetIMG.targetFormat"
          placeholder="选择输出的格式"
          rounded
          expanded
        >
          <option value="png">PNG</option>
          <option value="bmp">BMP</option>
          <option value="jpg">JPG</option>
          <option value="webp">WEBP</option>
        </b-select>
      </b-field>
      <b-field class="column" label="尺寸模式">
        <b-select
          v-model="targetIMG.targetWorkMode"
          placeholder="选择工作模式"
          rounded
          expanded>
          <option value="ratio">等比例缩小</option>
          <option value="square">方形缩略图</option>
        </b-select>
      </b-field>
      <div class="column">
        <b-field label="图片质量">
          <b-slider v-model="targetIMG.quality" indicator :tooltip="false" size="is-medium" :min=20 :max=120
                    aria-label="图片质量">
            <b-slider-tick :value=20>极低</b-slider-tick>
            <b-slider-tick :value=60>不错</b-slider-tick>
            <b-slider-tick :value=100>极佳</b-slider-tick>
            <b-slider-tick :value=120>爆表</b-slider-tick>
          </b-slider>
        </b-field>
      </div>
      <div class="column">
        <b-field :label="targetIMG.targetWorkMode=='ratio'?'等比缩小':'中心比例'">
          <b-slider v-model="targetIMG.proportion" indicator :tooltip="false" size="is-medium" :min=20 :max=100
                    aria-label="图片尺寸">
            <b-slider-tick :value=20>微小</b-slider-tick>
            <b-slider-tick :value=60>较小</b-slider-tick>
            <b-slider-tick :value=80>较大</b-slider-tick>
            <b-slider-tick :value=100>最大</b-slider-tick>
          </b-slider>
        </b-field>
      </div>

    </div>
    <div class="box">
      <div class="columns">
        <div class="column">
          <div class="buttons">
            <b-button :disabled="uploadButtonReady||finishedProcess"
                      @click="handleSubmit"
                      type="is-primary" expanded>上传
            </b-button>
          </div>
        </div>
      </div>
      <div v-if="imgPreview.isWork" class="columns">
        <div class="column is-4">
          <img @load="getInfo()" ref="opus" class="preview-img" :src="imgPreview.TempSrc"/>
        </div>
        <div class="column">
          <b-notification @close="deleteDropFile()" aria-close-label="Close notification">
            <span class="is-size-6 mr-3">{{ imgPreview.Name }}</span>
            <span class="is-size-7">{{ (imgPreview.Size / 1024 / 1024).toFixed(2) }}MB</span>
          </b-notification>
          <div>
            <div style="padding:15px 20px;background:#f6f5fa">
              <table style="white-space:nowrap;width: 50%" class="is-centered">
                <tbody>
                <tr>
                  <th>图片类型</th>
                  <td>{{ imgPreview.Type }}</td>
                </tr>
                <tr>
                  <th>图片分辨率</th>
                  <td>
                    <span> {{ imgPreview.Width }}</span>
                    x
                    <span v-text="imgPreview.Height"></span>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div v-show="finishedProcess" class="columns mt-2">
            <div class="column">
              <b-field label="AI识别的标签">
                <b-taglist>
                  <b-tag v-for="tag in outPutIMG.imgType" :key="tag" type="is-link">
                    <b-icon
                      icon="tag"
                      size="is-small">
                    </b-icon>
                    {{ tag }}
                  </b-tag>
                </b-taglist>
              </b-field>
            </div>
          </div>

          <div v-show="finishedProcess" class="columns mt-3">
            <div class="column">
              <a class="button is-danger is-fullwidth is-rounded"
                 :href="'/img2d/SystemData/ProcessOLD/Output/'+outPutIMG.outPutPathName"
                 :download="outPutIMG.imageName">立即下载</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import {post} from "@/untils/request";
import {failTips, successTips, warningTips} from "@/untils/tipsInfoUntil";

export default {
  name: "processIMG",
  data() {
    return {
      isLoading: false,
      imgTags: [
        "小姐姐"
      ],
      dropFiles: null,
      targetIMG: {
        targetFormat: undefined,
        targetWorkMode: undefined,
        quality: 80,
        proportion: 80
      },
      finishedProcess: false,
      outPutIMG: {
        imageName: "",
        imgType: [],
        outPutPathName: ""
      },
      imgPreview: {
        Name: "",
        Size: "",
        Type: "",
        Width: "",
        Height: "",
        TempSrc: "",
        isWork: false
      },
      imgUrl: "",
    }
  },
  computed: {
    uploadButtonReady: {
      get() {
        if (this.targetIMG.targetFormat != undefined && this.targetIMG.targetWorkMode != undefined && this.dropFiles != null) {
          return false
        } else {
          return true
        }
      },
    },
  },
  watch: {
    dropFiles(After) {
      this.finishedProcess = false
      if (After != null) {
        this.getInfo()
      } else {
        this.imgPreview.isWork = false
      }
    }
  },
  methods: {
    deleteDropFile() {
      this.dropFiles = null;
    },
    getInfo() {
      this.imgPreview.Name = this.dropFiles.name
      this.imgPreview.Size = this.dropFiles.size
      this.imgPreview.Type = this.dropFiles.type
      let _this = this;
      const reader = new FileReader();
      reader.readAsDataURL(this.dropFiles);
      reader.onload = function (ev) {
        let replaceSrc = ev.target.result;
        //加载图片获取图片真实宽度和高度
        let image = new Image();
        image.src = replaceSrc
        let url = "data:image/png;base64," + this.result.substring(this.result.indexOf(",") + 1)
        image.onload = (() => {
          _this.imgPreview.isWork = true
          _this.imgPreview.TempSrc = url
          _this.imgPreview.Width = image.width
          _this.imgPreview.Height = image.height
        })
      }
    },
    handleSubmit() {
      this.isLoading = true;
      let data = new FormData();
      data.append("targetFormat", this.targetIMG.targetFormat,)
      data.append("targetWorkMode", this.targetIMG.targetWorkMode,)
      data.append("quality", this.targetIMG.quality / 100,)
      data.append("proportion", this.targetIMG.proportion / 100,)
      data.append("file", this.dropFiles,)
      // const uploadData = {
      //   targetFormat: this.targetIMG.targetFormat,
      //   targetWorkMode: this.targetIMG.targetWorkMode,
      //   quality: this.targetIMG.quality,
      //   proportion: this.targetIMG.proportion,
      //   file: this.dropFiles
      // };
      post("/img2d/imgOLP/uploadImage", data).then(({data}) => {
        if (data.flag) {
          successTips("处理完成，快去下载吧～")
          this.outPutIMG = data.data
          this.finishedProcess = true
          console.log(data.data)
        }
        else {
          warningTips(data.message)
          this.finishedProcess = true
        }
      }).catch(e => {
        failTips("芜湖，居然出错了。你这张图片太强了")
        console.log(e);
      }).finally(() => {
        this.isLoading = false
      })
    },
    downloadIMG(){
      window.open("/img2d/"+this.outPutIMG.outPutPathName,"_blank")
    }
  }
}
</script>

<style scoped>
@import "//at.alicdn.com/t/font_2867504_emfyjqa8xtp.css";

.preview-img {
  border-radius: 10px;
}
</style>
