<template>
  <div v-if="originalIMG!=null" class="columns">
    <div class="column is-4">
      <img width="100%" @load="getInfo()" ref="opus" class="preview-img" :src="imgPreview.TempSrc"/>
    </div>
    <div class="column">
      <div class="columns">
        <div class="column">
          <b-field
            message="建议设置一个好听的标题，方便通过审核嗷"
            type="is-info"
            label="设置图片标题">
            <b-input autofocus size="is-small" v-model="friendName"></b-input>
          </b-field>
        </div>
      </div>

      <div class="columns">
        <div class="column">
          <b-notification @close="deleteDropFile" aria-close-label="Close notification">
            <span class="is-size-6 mr-3">{{ imgPreview.Name }}</span>
            <span class="is-size-7">{{ (imgPreview.Size / 1024 / 1024).toFixed(2) }}MB</span>
          </b-notification>
        </div>
      </div>

      <div class="columns">
        <div class="column">
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
          <div class="columns mt-2" v-show="imgTags.length>0">
            <div class="column">
              <b-field label="AI识别的标签">
                <b-taglist>
                  <b-tag v-for="tag in imgTags" :key="tag" type="is-link">
                    <b-icon
                      icon="tag"
                      size="is-small">
                    </b-icon>
                    {{ tag.tagName }}
                  </b-tag>
                </b-taglist>
              </b-field>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "infoIMG",
  props: ["dropFiles", "imgFriendName","imgTags"],
  data() {
    return {
      imgPreview: {
        Name: "",
        Size: "",
        Type: "",
        Width: "",
        Height: "",
        TempSrc: "",
      },
    }
  },
  methods: {
    getInfo() {
      this.imgPreview.Name = this.originalIMG.name
      this.imgPreview.Size = this.originalIMG.size
      this.imgPreview.Type = this.originalIMG.type
      let _this = this;
      const reader = new FileReader();
      reader.readAsDataURL(this.originalIMG);
      reader.onload = function (ev) {
        let replaceSrc = ev.target.result;
        //加载图片获取图片真实宽度和高度
        let image = new Image();
        image.src = replaceSrc
        let url = "data:image/png;base64," + this.result.substring(this.result.indexOf(",") + 1)
        image.onload = (() => {
          _this.imgPreview.TempSrc = url
          _this.imgPreview.Width = image.width
          _this.imgPreview.Height = image.height
        })
      }
    },
    deleteDropFile() {
      this.originalIMG = null;
      this.$emit("update:dropFiles", null)
    },
  },
  watch: {
    originalIMG(After) {
      if (After != null) {
        this.getInfo()
      }
    },
  },
  computed: {
    originalIMG: {
      get() {
        return this.dropFiles
      },
      set(newValue) {
        return newValue
      }
    },
    friendName: {
      get() {
        const Name = this.originalIMG != null ?
          this.originalIMG.name.substring(0, this.originalIMG.name.lastIndexOf(".")) : null;
        this.$emit("update:imgFriendName", Name)
        return Name;
      },
      set(newValue) {
        this.$emit("update:imgFriendName", newValue)
      }
    }
  }
}
</script>

<style scoped>

</style>
