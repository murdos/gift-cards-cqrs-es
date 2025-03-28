<template>
  <div class="gift-card">
    <div class="gift-card-header">
      <div class="gift-card-logo">
        <img :src="`../../../../content/images/${giftCard.shoppingStore.value}.png`" alt="Gift Card Logo" />
      </div>
      <div class="gift-card-header-content">
        <div class="gift-card-header-content-amount">
          {{ giftCard.remainingAmount.value }}
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
      <button class="gift-card-button" @click="openModal">Payer</button>
    </div>
    <GiftCardModal v-model:is-open="isModalOpen" title="Payer" @close="closeModal">
      <GiftCardPayment :default-amount="giftCard.remainingAmount.value" @submit="submitPayment" />
    </GiftCardModal>
  </div>
</template>

<script setup lang="ts">
import type { GiftCard } from '@/giftcard/domain/GiftCard';
import GiftCardPayment from '@/giftcard/infrastructure/primary/GiftCardPayment.vue';
import { AxiosGiftCardCommandRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardCommandRepository';
import VisualBarcode from '@/shared/barcode/infrastructure/primary/VisualBarcode.vue';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import GiftCardModal from '@/shared/modal/infrastructure/primary/GiftCardModal.vue';
import axios from 'axios';
import { ref } from 'vue';

const props = defineProps<{
  giftCard: GiftCard;
}>();

const emit = defineEmits(['giftCardUpdated']);

const isModalOpen = ref(false);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const submitPayment = async (paymentAmount: number) => {
  const giftCardCommandRepository = new AxiosGiftCardCommandRepository(new AxiosHttp(axios));
  const payment = { amount: paymentAmount };
  try {
    await giftCardCommandRepository.pay(props.giftCard.barcode.value, payment);
    console.log('Payment successful!');
    closeModal();
    emit('giftCardUpdated');
  } catch (error) {
    console.error('Payment failed:', error);
    // Add logic to show an error message
  }
};
</script>

<style scoped>
/* ... (Your existing styles) ... */
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
  margin-left: 10px;
}

.gift-card-button:hover {
  background-color: #0056b3;
}
</style>
