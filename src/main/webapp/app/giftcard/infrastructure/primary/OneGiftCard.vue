<template>
  <div class="gift-card">
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
  </div>
</template>

<script lang="ts">
import type { GiftCard } from '@/giftcard/domain/GiftCard';
import VisualBarcode from '@/shared/barcode/infrastructure/primary/VisualBarcode.vue';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'OneGiftCard',
  components: { VisualBarcode },
  props: {
    giftCard: {
      type: Object as () => GiftCard,
      required: true,
    },
  },
});
</script>

<style scoped>
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
</style>
