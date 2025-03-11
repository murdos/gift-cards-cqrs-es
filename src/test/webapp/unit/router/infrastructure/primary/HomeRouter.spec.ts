import HomepageVue from '@/home/infrastructure/primary/HomepageVue.vue';
import { routes } from '@/router';
import { mount, type VueWrapper } from '@vue/test-utils';
import { beforeEach, describe, expect, it } from 'vitest';
import { createRouter, createWebHistory, type Router } from 'vue-router';

let router: Router;
beforeEach(async () => {
  router = createRouter({
    history: createWebHistory(),
    routes,
  });
});

const wrap = (): VueWrapper => {
  return mount(HomepageVue, {
    global: {
      plugins: [router],
    },
  });
};

describe('Router', () => {
  describe.for([['/'], ['/home']])('Navigation on HomepageVue', url => {
    it(`should navigate on HomepageVue when the URL is ${url}`, async () => {
      router.push(url);

      await router.isReady();

      const wrapper = wrap();

      expect(wrapper.html()).toContain('Vue 3 + TypeScript + Vite');
    });
  });
});
