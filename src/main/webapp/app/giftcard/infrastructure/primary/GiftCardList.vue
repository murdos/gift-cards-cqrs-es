<template>
  <div class="gift-card-list">
    <h2>Mes Cartes Cadeaux</h2>
    <div v-if="loading" class="loading">Chargement...</div>
    <div v-else-if="error" class="error">Erreur: {{ error }}</div>
    <ul v-else class="gift-cards">
      <li v-for="giftCard in giftCards" :key="giftCard.barcode.value" class="gift-card">
        <div class="gift-card-header">
          <div class="gift-card-logo">
            <img src="../../../../content/images/JHipster-Lite-neon-green.png" alt="Gift Card Logo" />
          </div>
          <div class="gift-card-header-content">
            <div class="gift-card-header-content-amount">
              {{ giftCard.remaingAmount.value }}
              <span class="currency">€</span>
            </div>
            <div class="gift-card-description">Montant restant</div>
          </div>
        </div>
        <div class="gift-card-body">
          <div class="gift-card-barcode-container">
            <visual-barcode :barcode-value="giftCard.barcode.value" />
          </div>
        </div>
        <div class="gift-card-footer">
          <button class="gift-card-button">Voir les détails</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { AxiosGiftCardQueryRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardQueryRepository.ts';
import VisualBarcode from '@/shared/barcode/infrastructure/primary/VisualBarcode.vue';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';
import axios from 'axios';
import { defineComponent, onMounted, ref } from 'vue';

interface GiftCard {
  barcode: { value: string };
  remaingAmount: { value: number };
}

export default defineComponent({
  name: 'GiftCardList',
  components: { VisualBarcode },
  setup() {
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

    onMounted(fetchGiftCards);

    return {
      giftCards,
      loading,
      error,
    };
  },
});
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
  gap: 20px;
}

.gift-card {
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 200px;
}

.gift-card-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.gift-card-logo {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.gift-card-logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.gift-card-header-content {
  display: flex;
  flex-direction: column;
  margin-left: auto;
}

.gift-card-header-content-amount {
  font-size: 2em;
  font-weight: bold;
  color: #333;
  display: flex;
  align-items: center;
}

.currency {
  font-size: 0.8em;
  margin-left: 5px;
}

.gift-card-description {
  font-size: 0.8em;
  color: #666;
}

.gift-card-body {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.gift-card-barcode-container {
  height: 150px;
  width: 100%;
}

.gift-card-footer {
  text-align: right;
}

.gift-card-button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 15px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.gift-card-button:hover {
  background-color: #0056b3;
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
