<template>
  <div class="gift-card-list">
    <h2>Mes Cartes Cadeaux</h2>
    <ul v-if="giftCards.length > 0 && !error" class="gift-cards">
      <li v-for="giftCard in giftCards" :key="giftCard.barcode.value">
        <OneGiftCard :gift-card="giftCard" @gift-card-updated="refresh" />
      </li>
    </ul>
    <div v-if="loading" class="loading">Chargement...</div>
    <div v-else-if="error" class="error">Erreur: {{ error }}</div>
  </div>
</template>

<script setup lang="ts">
import type { GiftCard } from '@/giftcard/domain/GiftCard';
import { AxiosGiftCardQueryRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardQueryRepository.ts';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';
import axios from 'axios';
import { ref } from 'vue';
import OneGiftCard from './OneGiftCard.vue';

const giftCards = ref<GiftCard[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const fetchGiftCards = async () => {
  const giftCardRepository = new AxiosGiftCardQueryRepository(new AxiosHttp(axios));
  console.log('Fetching gift cards...');
  loading.value = true;
  error.value = null;
  try {
    giftCards.value = await giftCardRepository.findAll();
  } catch (err: any) {
    error.value = err.message || 'An error occurred';
  } finally {
    loading.value = false;
  }
};

const refresh = () => {
  fetchGiftCards();
};

fetchGiftCards();

defineExpose({ refresh });
</script>

<style scoped>
.gift-card-list {
  font-family: 'Arial', sans-serif;
  padding: 20px;
}

.gift-cards {
  list-style: none;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px; /* Add spacing between cards horizontally and vertically */
  row-gap: 40px; /* Add more vertical spacing between rows */
}

.loading {
  text-align: center;
  font-style: italic;
}

.error {
  color: red;
  text-align: center;
}
</style>
