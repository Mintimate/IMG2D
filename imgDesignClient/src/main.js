import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// buety样式
import buety from 'buefy'
import 'buefy/dist/buefy.css';
// 瀑布流插件
import {VueMasonryPlugin} from 'vue-masonry';

import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

// highlightjs
import hljs from 'highlight.js';

// codemirror 编辑器的相关资源
import Codemirror from 'codemirror';
// mode
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
// edit
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
// placeholder
import 'codemirror/addon/display/placeholder';
// active-line
import 'codemirror/addon/selection/active-line';
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';
// style
import 'codemirror/lib/codemirror.css';
// 社交分享
import Share from 'vue-social-share'
import 'social-share.js/dist/css/share.min.css'

import "@mdi/font/css/materialdesignicons.min.css"
import "@fortawesome/fontawesome-free/css/all.min.css"
VMdEditor.Codemirror = Codemirror;

VMdEditor.use(githubTheme, {
  Hljs: hljs,
});

// 头像上传组件
import AvatarCropper from 'vue-avatar-cropper'
Vue.use(VueMasonryPlugin)
Vue.use(VMdEditor)
Vue.use(AvatarCropper);
Vue.config.productionTip = false
Vue.use(buety)
Vue.use(Share)
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
