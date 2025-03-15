<template>
  <div class="gift-card-creation">
    <h2>Créer une Carte Cadeau</h2>
    <form @submit.prevent="createGiftCard">
      <div class="form-group">
        <label for="barcode">Code-barres:</label>
        <input id="barcode" v-model="barcode" type="text" required />
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

<script lang="ts">
import type { GiftCardDeclaration } from '@/giftcard/domain/GiftCardDeclaration';
import { AxiosGiftCardCommandRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardCommandRepository.ts';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';
import axios from 'axios';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'GiftCardDeclaration',
  emits: ['giftCardDeclared'],
  setup(props, { emit }) {
    const barcode = ref('');
    const amount = ref(0);
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
        };
        await giftCardCommandRepository.declare(giftCardDeclaration);
        successMessage.value = 'Carte cadeau déclarée avec succès!';
        barcode.value = '';
        amount.value = 0;
        emit('giftCardDeclared');
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

    return {
      barcode,
      amount,
      successMessage,
      errorMessage,
      createGiftCard,
      isSubmitting,
    };
  },
});
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
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
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
