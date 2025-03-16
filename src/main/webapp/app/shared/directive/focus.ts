import type { Directive } from 'vue';

export const focus: Directive = {
  mounted(el) {
    el.focus();
  },
};
