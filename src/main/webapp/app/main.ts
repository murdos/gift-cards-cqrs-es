import { focus } from '@/shared/directive/focus.ts';
import axios from 'axios';
import { createApp } from 'vue';
import AppVue from './AppVue.vue';
import router from './router';
// jhipster-needle-main-ts-import

// Configure Axios base URL
axios.defaults.baseURL = 'http://localhost:8080';

const app = createApp(AppVue);
app.use(router);
app.directive('focus', focus);
// jhipster-needle-main-ts-provider
app.mount('#app');
