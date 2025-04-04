<template>
  <div v-show="weeklyStatistics" class="chart-section">
    <canvas ref="barChartRef"></canvas>
  </div>
</template>

<script setup lang="ts">
import type { Days, WeeklyStatistics } from '@/giftcard/domain/WeeklyStatistics.ts';
import { AxiosGiftCardQueryRepository } from '@/giftcard/infrastructure/secondary/AxiosGiftCardQueryRepository.ts';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';
import axios from 'axios';
import { Chart, registerables } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';
import { onMounted, ref, watch } from 'vue';

Chart.register(...registerables);
const giftCardRepository = new AxiosGiftCardQueryRepository(new AxiosHttp(axios));

const weeklyStatistics = ref<WeeklyStatistics | null>();
const loading = ref(false);
const error = ref<string | null>(null);
let chartInstance: Chart | null = null;
const barChartRef = ref<HTMLCanvasElement | null>(null);

const daysOrder: Days[] = [
  'MONDAY',
  'TUESDAY',
  'WEDNESDAY',
  'THURSDAY',
  'FRIDAY',
  'SATURDAY',
  'SUNDAY',
];

const daysLabels: Record<Days, string> = {
  MONDAY: 'Lundi',
  TUESDAY: 'Mardi',
  WEDNESDAY: 'Mercredi',
  THURSDAY: 'Jeudi',
  FRIDAY: 'Vendredi',
  SATURDAY: 'Samedi',
  SUNDAY: 'Dimanche',
};

const createChart = () => {
  if (barChartRef.value && weeklyStatistics.value) {
    chartInstance = new Chart(barChartRef.value, {
      type: 'bar',
      data: {
        labels: daysOrder.map(day => daysLabels[day]),
        datasets: [
          {
            label: 'Nombre de paiements',
            data: daysOrder.map(day => weeklyStatistics.value!!.values[day]),
            backgroundColor: 'rgba(0, 123, 255, 0.7)',
            borderColor: 'rgba(0, 123, 255, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            display: false,
            grid: {
              color: 'rgba(0, 0, 0, 0.1)',
            },
          },
          x: {
            ticks: {
              color: '#333',
              font: {
                family: 'Arial',
                size: 14,
              },
            },
            grid: {
              color: '#ffffff',
            },
          },
        },
        plugins: {
          legend: {
            labels: {
              color: '#007bff',
              font: {
                family: 'Arial',
                size: 16,
              },
            },
          },
          datalabels: {
            anchor: 'end',
            align: 'start',
            color: '#ffffff',
            font: {
              family: 'Arial',
              size: 14,
              weight: 'bold',
            },
            display: context => {
              return context.dataset.data[context.dataIndex] !== 0;
            },
          },
        },
      },
      plugins: [ChartDataLabels],
    });
  }
};

async function refresh() {
  loading.value = true;
  error.value = null;
  try {
    weeklyStatistics.value = await giftCardRepository.getWeeklyStatistics();
  } catch (err: any) {
    error.value = err.message || 'An error occurred';
  } finally {
    loading.value = false;
  }
  createChart();
}

onMounted(() => {
  refresh();
});

watch(
  () => weeklyStatistics.value,
  newData => {
    if (chartInstance && newData) {
      chartInstance.data.labels = daysOrder.map(day => daysLabels[day]);
      chartInstance.data.datasets[0].data = daysOrder.map(day => newData!!.values[day]);
      chartInstance.update();
    }
  },
);

defineExpose({ refresh });
</script>

<style scoped>
.chart-section {
  margin: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

canvas {
  max-width: 100%;
  margin: 20px;
}
</style>
