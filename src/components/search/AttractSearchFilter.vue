<script setup>
import { ref, computed, onMounted } from 'vue';
import { getGugunList } from '@/api/attractApi';
import { useAttractionStore } from '@/stores/attractionStore';
import { storeToRefs } from 'pinia';

const attractionStore = useAttractionStore();
const { sidoList: apiSidoList, contentTypeList: apiContentTypeList, searchParams } = storeToRefs(attractionStore);
const emit = defineEmits(['search']);

const sidoList = computed(() => [
  { sidoCode: null, sidoName: '전국' },
  ...apiSidoList.value
]);

const contentTypes = computed(() => [
  { contentTypeId: null, contentTypeName: '전체' },
  ...apiContentTypeList.value
]);

const gugunList = ref([]);
const selectedSido = ref(null);
const selectedGugun = ref(null);
const selectedType = ref(null);
const selectedSort = ref('name');
const keyword = ref('');

const sortOptions = [
  { title: '이름순', value: 'name' },
  { title: '좋아요순', value: 'likes' },
  { title: '조회수순', value: 'views' },
];

const handleSidoChange = async () => {
  selectedGugun.value = null;
  gugunList.value = [{ gugunCode: null, gugunName: '전체' }];
  
  if (!selectedSido.value) return;
  
  try {
    const response = await getGugunList(selectedSido.value);
    gugunList.value = [
      { gugunCode: null, gugunName: '전체' },
      ...response.data
    ];
  } catch (error) {
    console.error('구군 목록 조회 실패:', error);
  }
};

const handleSearch = () => {
  const params = {
    sidoCode: selectedSido.value,
    gugunCode: selectedGugun.value,
    contentTypeId: selectedType.value,
    keyword: keyword.value,
    sortBy: selectedSort.value,
    page: 1
  };
  
  attractionStore.fetchAttractions(params, true);
  emit('search', params);
};

// 컴포넌트 마운트 시 검색 조건 복원
onMounted(() => {
  if (selectedSido.value) {
    handleSidoChange();
  }
});
</script>

<template>
  <v-card class="mb-4">
    <v-card-text>
      <v-row>
        <v-col cols="12" md="2">
          <v-select
            v-model="selectedSido"
            :items="sidoList"
            item-title="sidoName"
            item-value="sidoCode"
            label="시도"
            variant="outlined"
            density="comfortable"
            @update:model-value="handleSidoChange"
          ></v-select>
        </v-col>

        <v-col cols="12" md="2">
          <v-select
            v-model="selectedGugun"
            :items="gugunList"
            item-title="gugunName"
            item-value="gugunCode"
            label="구군"
            variant="outlined"
            density="comfortable"
            :disabled="!selectedSido"
          ></v-select>
        </v-col>

        <v-col cols="12" md="2">
          <v-select
            v-model="selectedType"
            :items="contentTypes"
            item-title="contentTypeName"
            item-value="contentTypeId"
            label="관광지 유형"
            variant="outlined"
            density="comfortable"
          ></v-select>
        </v-col>

        <v-col cols="12" md="2">
          <v-select
            v-model="selectedSort"
            :items="sortOptions"
            item-title="title"
            item-value="value"
            label="정렬 기준"
            variant="outlined"
            density="comfortable"
          ></v-select>
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field
            v-model="keyword"
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
            height="48"
          >
            검색
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>