<template>
  <div class="columns">
    <div class="column is-2">
      <aside class="sidebar">
        <p class="menu-label">
          管理员控制台
        </p>
        <ul class="menu-list">
          <li>
            <router-link to="/audit">
            监控台
          </router-link>
          </li>
          <li>
            <p class="menu-p">图片相关</p>
            <ul>
              <li>
                <router-link to="/audit/auditIMG">
                  图片审核
                </router-link>
              </li>
              <li>
                <router-link to="/audit/auditIMGClassify">
                  分类管理
                </router-link>
              </li>
            </ul>
          </li>
          <li>
            <p class="menu-p">文章相关</p>
            <ul>
              <li>
                <router-link to="/audit/auditPost">
                  文章审核
                </router-link>
              </li>
              <li>
                <router-link to="/audit/auditPostCategory">
                  分类管理
                </router-link>
              </li>
            </ul>
          </li>
          <li>
            <p class="menu-p">用户相关</p>
            <ul>
              <li>
                <router-link to="/audit/auditUserIdent">
                  认证审核
                </router-link>
              </li>
              <li>
                <router-link to="/audit/auditUser">
                  用户管理
                </router-link>
              </li>
            </ul>
          </li>
          <li><p class="menu-p">其他</p>
            <ul>
              <li>
                <router-link to="/audit/auditImageComment">
                图片评论
                </router-link>
              </li>
              <li>
                <router-link to="/audit/auditArticleComment">
                  文章评论
                </router-link>
              </li>
            </ul>
          </li>
        </ul>
        <a role="button" class="back-to-top">
          <span class="icon is-small">
            <i
              class="mdi mdi-arrow-expand-up">
            </i></span>
          <span>Back to top</span>
        </a>
      </aside>
    </div>
    <div class="column is-10">
      <router-view>
      </router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: "auditMessage",
}
</script>

<style scoped>
.menu-p {
  display: block;
  padding: 0.5em 0.75em;
}

.back-to-top {
  position: sticky;
  margin-top: 1.5rem;
  top: calc(100vh - 3rem);
  font-size: .8rem;
  color: #4a4a4a;
  font-weight: 700;
}
</style>
