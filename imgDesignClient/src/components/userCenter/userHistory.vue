<template>
  <div class="column" style="min-height: 60vh">
    <b-table
      :data="SingleImageList"
      ref="table"
      paginated
      per-page="10"
      detailed
      detail-key="id"
      show-detail-icon
      aria-next-label="Next page"
      aria-previous-label="Previous page"
      aria-page-label="Page"
      aria-current-label="Current page">

      <b-table-column centered field="cateName" label="图片分类" sortable v-slot="props">
        {{ props.row.cateName }}
      </b-table-column>

      <b-table-column field="createTime" label="上传时间" sortable centered v-slot="props">
                  <span class="tag is-success">
                      {{ new Date(props.row.createTime).toLocaleDateString() }}
                  </span>
      </b-table-column>

      <b-table-column field="imgName" label="图片名称" sortable centered v-slot="props">
        {{ props.row.imgName }}
      </b-table-column>
      <b-table-column centered field="isAudit" label="状态" sortable v-slot="props">
        <span class="tag" :class="[props.row.isAudit == 0 ? 'is-danger' : '' +
         'is-success']">
                  {{ props.row.isAudit == 0 ? "未审核" : "通过" }}
        </span>
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
                <!-- 图片标签-->
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
              </p>
            </div>
          </div>
        </article>
        <!-- 用户自助删除-->
        <b-field class="mt-2">
          <b-button
            expanded
            rounded
            size="is-small"
            type="is-danger"
            label="!!删除!!◡ ヽ(`Д´)ﾉ ┻━┻"
            @click="confirmDelete(props.row.id)"
          />
        </b-field>
      </template>
    </b-table>
  </div>

</template>

<script>
import {get, post} from "@/untils/request"
import {failTips, successTips} from "@/untils/tipsInfoUntil";

export default {
  name: "userHistory",
  data() {
    return {
      SingleImageList: [
        {
          id: "",
          imgName: "",
          imgHostPath: "",
          cateId: "",
          cateName: "",
          likeCount: "",
          collectCount: "",
          createTime: "0",
          isAudit: 0,
          tagDTOList: [
            {
              id: "",
              tagName: ""
            }
          ]
        },
      ],
    }
  },
  mounted() {
    this.getLatestData()
  },
  methods: {
    getLatestData() {
      get("/img2d/img/admin/getBaseInfo").then(({data}) => {
        console.log(data.data)
        this.SingleImageList = data.data
      })
    },
    confirmDelete(imgId) {
      this.$buefy.dialog.confirm({
        title: '确定删除图片',
        message: '是否确认删除，注意<b>该操作不可复原！！！</b>',
        confirmText: '删除该图片',
        cancelText:"手滑啦(･_･;",
        type: 'is-danger',
        hasIcon: true,
        onConfirm: () => {
          post("/img2d/img/deleteIMG/"+imgId).then(({data})=>{
            if(!data.flag){
              failTips("删除失败(･_･;")
            }
            else{
              successTips("删除成功:-)")
              this.getLatestData()
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
