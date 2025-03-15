<template>
  <svg :id="barcodeId" class="barcode"></svg>
</template>

<script lang="ts">
import JsBarcode from 'jsbarcode';
import { defineComponent, nextTick, onMounted, ref, watch } from 'vue';

export default defineComponent({
  name: 'VisualBarcode',
  props: {
    barcodeValue: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const barcodeId = ref(`barcode-${props.barcodeValue}`);

    const generateBarcode = () => {
      nextTick(() => {
        const element = document.getElementById(barcodeId.value);
        if (element) {
          JsBarcode(`#${barcodeId.value}`, props.barcodeValue, {
            format: 'CODE128',
            displayValue: false,
            width: 2,
            height: 50,
            margin: 10,
          });
        } else {
          console.error(`Element with id ${barcodeId.value} not found.`);
        }
      });
    };

    onMounted(generateBarcode);

    watch(
      () => props.barcodeValue,
      () => {
        barcodeId.value = `barcode-${props.barcodeValue}`;
        generateBarcode();
      },
    );

    return {
      barcodeId,
    };
  },
});
</script>

<style scoped>
.barcode {
  width: 100%;
  height: 100%;
}
</style>
