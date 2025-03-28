<template>
  <div class="gift-card-creation">
    <form @submit.prevent="createGiftCard">
      <div class="form-group">
        <label for="barcode">Code-barres:</label>
        <input id="barcode" v-model="barcode" v-focus type="text" required />
      </div>
      <div class="form-group">
        <label for="shoppingStore">Magasin:</label>
        <select id="shoppingStore" v-model="shoppingStore" class="styled-select">
          <option v-for="store in shoppingStores" :key="store.id" :value="store">
            {{ store.name }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="amount">Montant:</label>
        <input id="amount" v-model="amount" type="number" required min="1" />
      </div>
      <button type="submit" :disabled="isSubmitting">
        <span v-if="!isSubmitting">Créer</span>
        <span v-else>Création...</span>
      </button>
    </form>
    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { GiftCardDeclaration } from '@/giftcard/domain/GiftCardDeclaration';
import { shoppingStores } from '@/giftcard/domain/ShoppingStore.ts';
import { AxiosGiftCardCommandRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardCommandRepository.ts';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';
import axios from 'axios';
import { ref } from 'vue';

const emits = defineEmits(['giftCardDeclared']);

const barcode = ref('');
const amount = ref(0);
const shoppingStore = ref(shoppingStores[0]);
const successMessage = ref('');
const errorMessage = ref('');
const isSubmitting = ref(false);
const giftCardCommandRepository = new AxiosGiftCardCommandRepository(new AxiosHttp(axios));

const createGiftCard = async () => {
  successMessage.value = '';
  errorMessage.value = '';
  isSubmitting.value = true;

  try {
    const giftCardDeclaration: GiftCardDeclaration = {
      barcode: { value: barcode.value },
      amount: { value: amount.value },
      shoppingStore: { value: shoppingStore.value.id },
    };
    await giftCardCommandRepository.declare(giftCardDeclaration);
    successMessage.value = 'Carte cadeau déclarée avec succès!';
    barcode.value = '';
    amount.value = 0;
    emits('giftCardDeclared');
  } catch (error: any) {
    if (error.response && error.response.status === 400) {
      errorMessage.value = 'Données invalides. Veuillez vérifier les champs.';
    } else {
      errorMessage.value = 'Erreur lors de la déclaration de la carte cadeau.';
    }
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.gift-card-creation {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input[type='text'],
input[type='number'] {
  width: calc(100% - 16px);
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.styled-select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
  background-color: #fff;
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-image: url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="gray" class="bi bi-chevron-down" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z"/></svg>');
  background-repeat: no-repeat;
  background-position: right 10px center;
}

button {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

button:disabled {
  background-color: #ccc;
  cursor: default;
}

.success-message {
  color: green;
  margin-top: 10px;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
