<template>
  <div class="homepage">
    <header class="homepage-header">
      <div class="container">
        <h1 class="title">Bienvenue sur Gift Cards !</h1>
        <p class="subtitle">Gérez vos cartes cadeaux facilement et en toute sécurité.</p>
      </div>
    </header>

    <main class="homepage-main">
      <div class="container">
        <section class="gift-card-creation-section">
          <button class="primary-button" @click="openGiftCardCreationModal">
            Déclarer une carte cadeau
          </button>
        </section>
        <section class="gift-card-list-section">
          <GiftCardList ref="giftCardListRef" />
        </section>
      </div>
    </main>

    <GiftCardModal
      :is-open="isGiftCardDeclarationModalOpen"
      title="Déclarer une carte cadeau"
      @close="closeGiftCardDeclarationModal"
    >
      <GiftCardDeclaration @gift-card-declared="handleGiftCardDeclared" />
    </GiftCardModal>

    <footer class="homepage-footer">
      <div class="container">
        <p>&copy; {{ currentYear }} Gift Cards. Tous droits réservés.</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import GiftCardList from '@/giftcard/infrastructure/primary/GiftCardList.vue';
import GiftCardModal from '@/shared/modal/infrastructure/primary/GiftCardModal.vue';
import { ref } from 'vue';
import GiftCardDeclaration from '../../../giftcard/infrastructure/primary/GiftCardDeclaration.vue';

const currentYear = ref(new Date().getFullYear());
const isGiftCardDeclarationModalOpen = ref(false);

// store the instance of gifcardList to be able to call `refresh` on it
const giftCardListRef = ref<InstanceType<typeof GiftCardList>>();

const openGiftCardCreationModal = () => {
  isGiftCardDeclarationModalOpen.value = true;
};

const closeGiftCardDeclarationModal = () => {
  isGiftCardDeclarationModalOpen.value = false;
};

const handleGiftCardDeclared = () => {
  closeGiftCardDeclarationModal();
  refreshGiftCardList();
};

const refreshGiftCardList = () => {
  if (giftCardListRef.value) {
    giftCardListRef.value.refresh();
  }
};
</script>

<style scoped>
.homepage {
  font-family: 'Arial', sans-serif;
  color: #333;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.homepage-header {
  background-color: #f0f8ff; /* Light blue */
  padding: 40px 0;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo-container {
  margin-bottom: 20px;
}

.logo {
  width: 100px;
  height: auto;
}

.title {
  font-size: 2.5em;
  color: #007bff; /* Blue */
  margin-bottom: 10px;
}

.subtitle {
  font-size: 1.2em;
  color: #666;
}

.homepage-main {
  flex-grow: 1;
  padding: 40px 0;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.feature {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: center;
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin-bottom: 10px;
}

.feature h3 {
  color: #007bff;
  margin-bottom: 10px;
}

.gift-card-list-section {
  margin-bottom: 40px;
}

.homepage-footer {
  background-color: #f8f8f8;
  padding: 20px 0;
  text-align: center;
  border-top: 1px solid #ddd;
}

.primary-button {
  background-color: #007bff; /* Blue */
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.primary-button:hover {
  background-color: #0056b3; /* Darker blue on hover */
}

.primary-button:active {
  background-color: #004080; /* Even darker blue on click */
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}
</style>
