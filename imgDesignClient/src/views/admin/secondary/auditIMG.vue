<template>
  <section>
    <b-table
      :data="AdminSingleImageList"
      ref="table"
      paginated
      page-input
      page-input-position="is-input-right"
      pagination-simple
      per-page="10"
      detailed
      detail-key="id"
      show-detail-icon
      aria-next-label="Next page"
      aria-previous-label="Previous page"
      aria-page-label="Page"
      checkable
      :debounce-search="AdminSingleImageList.length"
      :checked-rows.sync="checkItems"
      checkbox-position="left"
      aria-current-label="Current page">

      <b-table-column centered field="cateName"
                      searchable
                      label="图片分类"
                      sortable
                      v-slot="props">
        {{ props.row.cateName }}
      </b-table-column>

      <b-table-column field="username"
                      searchable
                      label="上传用户"
                      sortable
                      v-slot="props">
        {{ props.row.username }}
      </b-table-column>

      <b-table-column field="createTime" label="上传时间"
                      sortable centered v-slot="props">
                  <span class="tag is-success">
                      {{ new Date(props.row.createTime).toLocaleDateString() }}
                  </span>
      </b-table-column>

      <b-table-column field="imgName"
                      searchable
                      label="图片名称" sortable centered v-slot="props">
        {{ props.row.imgName }}
      </b-table-column>


      <b-table-column centered field="isAudit" label="状态"

                      searchable
                      sortable v-slot="props">
        <template v-if="props.row.delFlag==1">
          <span class="tag is-danger">
                 已删除
          </span>
        </template>
        <template v-else>
                  <span class="tag" :class="[props.row.isAudit == 0 ? 'is-warning' : '' +
         'is-success']">
                  {{ props.row.isAudit == 0 ? "未审核" : "通过" }}
        </span>
        </template>
      </b-table-column>

      <template #detail="props">
        <article class="media">
          <figure class="media-left">
            <p class="image" style="width: 128px">
              <img :src="/img2d/+props.row.imgHostPath">
            </p>
          </figure>
          <div class="media-content">
            <div class="content">
              <p>
                <strong>图片唯一ID：{{ props.row.id }}</strong>
                <br>
                <b-field>
                  <b-taglist>
                    <b-tag v-for="tag in props.row.tagDTOList" :key="tag.id" type="is-link">
                      <b-icon
                        icon="tag"
                        size="is-small">
                      </b-icon>
                      {{ tag.tagName }}
                    </b-tag>
                  </b-taglist>
                </b-field>
                <span>感知哈希值：{{ props.row.perceptualHash }}</span><br>
                <b-button
                  expanded
                  size="is-small"
                  rounded
                  type="is-dark"
                  @click="openImageDetail(props.row.id)"
                  label="查看详情"/>
              </p>
            </div>
          </div>
        </article>
      </template>
      <template #bottom-left>
        <div class="field is-grouped">
          <p class="control">
            <b-button
              label="通过审核"
              @click="auditImageList(1)"
              class="button is-link"/>
          </p>
          <p class="control">
            <b-button
              label="吊销审核"
              @click="auditImageList(0)"
              class="button is-danger"/>
          </p>
          <b-modal
            destroy-on-hide
            :active="newInfoValue.changeTag"
            width="25rem"
          >
            <update-tags :checkItem="checkItems"
                         @success="updateSingleIMG"
                         @close="newInfoValue.changeTag=false"></update-tags>
          </b-modal>
          <p class="control">
            <b-button
              @click="newInfoValue.changeTag=!newInfoValue.changeTag"
              label="修改标签"
              :disabled="checkItems.length!=1"
              class="button is-info"/>
          </p>
          <b-modal
            destroy-on-hide
            :active="newInfoValue.changeCate"
            width="25rem"
          >
            <update-cate :checkItem="checkItems"
                         @success="updateSingleIMG"
                         @close="newInfoValue.changeCate=false"></update-cate>
          </b-modal>
          <p class="control">
            <b-button
              label="修改分类"
              @click="newInfoValue.changeCate=!newInfoValue.changeCate"
              :disabled="checkItems.length!=1"
              class="button is-info"/>
          </p>
          <b-modal
            destroy-on-hide
            :active="newInfoValue.changeImgName"
            width="25rem"
          >
            <update-name :checkItem="checkItems"
                         @success="updateSingleIMG"
                         @close="newInfoValue.changeImgName=false"></update-name>
          </b-modal>
          <p class="control">
            <b-button
              label="重命名"
              @click="newInfoValue.changeImgName=!newInfoValue.changeImgName"
              :disabled="checkItems.length!=1"
              class="button is-info"/>
          </p>
        </div>
      </template>
    </b-table>
  </section>
</template>

<script>
import {get, post} from "@/untils/request"
import updateTags from "@/components/adminAudit/img/updateImageTags"
import updateCate from "@/components/adminAudit/img/updateImageCate"
import updateName from "@/components/adminAudit/img/updateImageName"
import {successTips, warningTips} from "@/untils/tipsInfoUntil";
import {productSnowKey} from "@/untils/snowKey";

export default {
  name: "auditIMG",
  components: {
    updateTags,
    updateCate,
    updateName
  },
  data() {
    return {
      newInfoValue: {
        changeTag: false,
        changeCate: false,
        changeImgName: false
      },
      AdminSingleImage: {
        id: "",
        cateId: "",
        cateName: "",
        tagDTOList: [
          {
            id: "",
            tagName: ""
          }
        ]
      },
      AdminSingleImageList: [
        {
          id: "",
          imgName: "",
          imgHostPath: "",
          cateId: "",
          cateName: "",
          userId: "",
          username: "",
          likeCount: "",
          collectCount: "",
          createTime: "0",
          isAudit: 0,
          delFlag:0,
          perceptualHash:"",
          tagDTOList: [
            {
              id: "",
              tagName: ""
            }
          ]
        },
      ],
      checkItems: []
    }
  },
  mounted() {
    this.getLatestData()
  },
  methods: {
    // 审核图片
    auditImageList(state) {
      this.checkItems.forEach(item => {
        item.isAudit = state
      })
      post("/img2d/img/admin/auditImage/" + state, this.checkItems).then(({data}) => {
        if (data.flag) {
          successTips("操作成功～～～")
        } else {
          warningTips("操作失败(･_･;")
        }
      })
    },
    // 获取最新数据
    getLatestData() {
      get("/img2d/img/admin/getAllInfo").then(({data}) => {
        this.AdminSingleImageList = data.data
      })
    },
    // 对图片内容更新后，重新获取最新数据
    updateSingleIMG() {
      successTips("数据更新成功，正在获取最新数据")
      this.getLatestData()
      this.checkItems = []
      this.newInfoValue.changeTag = false
      this.newInfoValue.changeCate = false
      this.newInfoValue.changeImgName = false
    },
    openImageDetail(imgId){
      window.open("/imgDetail/"+productSnowKey(imgId),"_blank")
    }
  },
}
</script>

<style scoped>

</style>
