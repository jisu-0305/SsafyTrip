<script setup>
import { storeToRefs } from 'pinia';
import { useAttractionStore } from '@/stores/attractionStore';

const attractionStore = useAttractionStore();
const { attractions, currentPage, totalPages, totalCount } = storeToRefs(attractionStore);

const emit = defineEmits(['update:page']);

const handlePageChange = (page) => {
  emit('update:page', page);
};

const props = defineProps({
  hoveredMarkerId: Number
});
</script>

<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center px-4">
      <span>검색 결과</span>
      <span class="text-subtitle-2">총 {{ totalCount }}건</span>
    </v-card-title>

    <v-divider></v-divider>

    <v-list lines="two">
      <v-list-item
        v-for="attraction in attractions"
        :key="attraction.no"
        :title="attraction.title"
        :subtitle="`조회수: ${attraction.views}`"
        :class="{ 'highlighted': attraction.contentId === hoveredMarkerId }"
        @click="$router.push(`/attraction/${attraction.no}`)"
        style="cursor: pointer"
      >
        <template v-slot:prepend>
          <v-avatar size="100">
            <v-img 
              :src="attraction.firstImage1" 
              :alt="attraction.title"
              cover
            >
              <template v-slot:placeholder>
                <v-avatar
                  color="grey-lighten-2"
                  size="100"
                >
                  <v-icon 
                    icon="mdi-image-off"
                    color="grey-darken-2"
                    size="32"
                  ></v-icon>
                </v-avatar>
              </template>
            </v-img>
          </v-avatar>
        </template>
      </v-list-item>
    </v-list>

    <v-card-actions class="justify-center">
      <v-pagination
        v-model="currentPage"
        :length="totalPages"
        :total-visible="7"
        @update:model-value="handlePageChange"
      ></v-pagination>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.highlighted {
  position: relative;
  background-color: rgba(var(--v-theme-primary), 0.1);
  transition: all 0.3s ease;
}

.highlighted::after {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 4px;
  height: 100%;
  background-color: rgb(var(--v-theme-primary));
}

.v-list-item {
  transition: background-color 0.3s ease;
}
</style>