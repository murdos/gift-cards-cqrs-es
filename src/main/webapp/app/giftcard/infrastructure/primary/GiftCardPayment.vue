<template>
  <div class="gift-card-payment">
    <form @submit.prevent="submitPayment">
      <div class="form-group">
        <label for="paymentAmount">Montant à payer:</label>
        <input
          id="paymentAmount"
          v-model.number="paymentAmount"
          v-focus
          type="number"
          min="0"
          required
        />
      </div>
      <div class="form-group">
        <label for="paymentDate">Date de paiement:</label>
        <input id="paymentDate" v-model="paymentDate" type="date" required />
      </div>
      <button type="submit" class="payment-button">Confirmer le paiement</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const paymentAmount = ref(0);
const paymentDate = ref('');

const props = defineProps<{
  defaultAmount: number;
}>();

const emit = defineEmits(['submit']);

paymentAmount.value = props.defaultAmount;

const submitPayment = () => {
  emit('submit', { amount: paymentAmount.value, date: paymentDate.value });
};
</script>

<style scoped>
.gift-card-payment {
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

input[type='number'],
input[type='date'] {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.payment-button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 15px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.payment-button:hover {
  background-color: #0056b3;
}
</style>
