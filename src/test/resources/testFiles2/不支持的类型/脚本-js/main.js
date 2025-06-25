import App from './App'
import store from './store'
import uniToast from './utils/uniToastLoading.js'
import './components/C-scroll/mixin.js'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import publicFution from "@/utils/public.js";
Vue.prototype.$publicFution = publicFution;
Vue.config.productionTip = false
Vue.prototype.$store = store
Vue.prototype.uniToast = uniToast
App.mpType = 'app'
const app = new Vue({
  store,
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.use(store)
  return {
    app
  }
}
// #endif