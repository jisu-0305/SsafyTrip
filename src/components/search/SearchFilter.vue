<!-- components/search/SearchFilter.vue -->
<script setup>
import { ref } from 'vue';

const props = defineProps({
  cities: {
    type: Array,
    required: true
  },
  types: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['search']);

const selectedCity = ref(null);
const selectedDistrict = ref(null);
const selectedType = ref(null);
const searchKeyword = ref("");

const handleSearch = () => {
  emit('search', {
    city: selectedCity.value,
    district: selectedDistrict.value,
    type: selectedType.value,
    keyword: searchKeyword.value
  });
};
</script>

<template>
  <v-card class="mb-4">
    <v-card-text>
      <v-row>
        <v-col cols="12" md="3">
          <v-select
            v-model="selectedCity"
            :items="cities"
            item-title="title"
            item-value="value"
            label="시도"
            variant="outlined"
            density="comfortable"
          ></v-select>
        </v-col>

        <v-col cols="12" md="3">
          <v-select
            v-model="selectedDistrict"
            :items="[]"
            label="구군"
            variant="outlined"
            density="comfortable"
          ></v-select>
        </v-col>

        <v-col cols="12" md="3">
          <v-select
            v-model="selectedType"
            :items="types"
            item-title="title"
            item-value="value"
            label="관광지 유형"
            variant="outlined"
            density="comfortable"
          ></v-select>
        </v-col>

        <v-col cols="12" md="2">
          <v-text-field
            v-model="searchKeyword"
            label="검색어"
            variant="outlined"
            density="comfortable"
          ></v-text-field>
        </v-col>

        <v-col cols="12" md="1">
          <v-btn 
            color="primary" 
            block 
            @click="handleSearch"
            height="56"
          >
            검색
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>