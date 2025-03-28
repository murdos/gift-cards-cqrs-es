<template>
  <div class="details-container">
    <ul class="details-list">
      <li v-for="detail in details.value" :key="detail">{{ detail }}</li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { AxiosGiftCardQueryRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardQueryRepository.ts';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';
import axios from 'axios';
import { ref } from 'vue';

const props = defineProps<{
  barcode: string;
}>();

const giftCardRepository = new AxiosGiftCardQueryRepository(new AxiosHttp(axios));

const details = ref<{ value: string[] }>({ value: [] });

giftCardRepository.findDetailsBy(props.barcode).then(result => {
  details.value = result;
});
</script>

<style scoped>
.details-container {
  padding: 20px;
  border: 1px solid #ccc;
}

.details-list {
  list-style-type: none;
  padding: 0;
}

.details-list li {
  padding: 10px;
  margin-bottom: 8px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.details-list li:hover {
  background-color: #f1f1f1;
}
</style>
