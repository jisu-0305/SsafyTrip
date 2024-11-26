<script setup>
import { ref, onMounted } from "vue";
import AttractKakaoMap from "@/components/search/AttractKakaoMap.vue";
import AttractSearchFilter from "@/components/search/AttractSearchFilter.vue";
import AttractList from "@/components/search/AttractList.vue";
import PageHeader from "@/components/common/PageHeader.vue";
import { useAttractionStore } from "@/stores/attractionStore";
import { storeToRefs } from "pinia";

const attractionStore = useAttractionStore();
const { loading } = storeToRefs(attractionStore);

const handleSearch = async (params) => {
  await attractionStore.fetchAttractions(params);
};

const handlePageChange = async (page) => {
  await attractionStore.fetchAttractions({ page });
};

onMounted(async () => {
  await attractionStore.fetchInitialAttractions();
});

const hoveredMarkerId = ref(null);

const handleMarkerHover = (contentId) => {
  hoveredMarkerId.value = contentId;
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="관광지 검색" icon="mdi-map-search" />

          <div class="content-area">
            <AttractSearchFilter @search="handleSearch" class="mb-4" />

            <v-row>
              <v-col cols="12" md="6">
                <v-card height="600" class="map-card">
                  <AttractKakaoMap
                    @hover-marker="handleMarkerHover"
                  />
                </v-card>
              </v-col>

              <v-col cols="12" md="6">
                <v-progress-circular
                  v-if="loading"
                  indeterminate
                  color="primary"
                  class="d-flex mx-auto my-4"
                />
                <keep-alive>
                  <AttractList
                    :hoveredMarkerId="hoveredMarkerId"
                    @update:page="handlePageChange"
                  />
                </keep-alive>
              </v-col>
            </v-row>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.map-card {
  overflow: visible !important;
  position: relative;
}
</style>
