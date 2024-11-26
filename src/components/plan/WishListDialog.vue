<template>
  <v-card flat>
    <v-toolbar color="primary" dark flat>
      <v-toolbar-title>찜 목록</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon @click="closeDialog">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>

    <v-card-text class="pa-4">
      <v-row>
        <v-col 
          v-for="(spot, index) in planStore.wishList" 
          :key="index" 
          cols="12" 
          md="6"
        >
          <v-card variant="outlined" class="h-100">
            <v-img
              :src="spot.firstImage1 || 'https://via.placeholder.com/150'"
              height="150"
              cover
              class="bg-grey-lighten-2"
            >
              <template v-slot:placeholder>
                <v-row class="fill-height ma-0" align="center" justify="center">
                  <v-progress-circular indeterminate color="grey-lighten-5"></v-progress-circular>
                </v-row>
              </template>
            </v-img>

            <v-card-title class="text-subtitle-1">{{ spot.title }}</v-card-title>
            <v-card-subtitle>{{ spot.address }}</v-card-subtitle>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="primary"
                variant="tonal"
                @click="selectSpot(spot)"
              >
                선택
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { onMounted } from 'vue';
import { usePlanStore } from '@/stores/planStores';

const planStore = usePlanStore();

const closeDialog = () => {
  planStore.setShowWishList(false);
};

const selectSpot = (spot) => {
  planStore.setSelectedSpot(spot);
  planStore.setShowWishList(false);
  planStore.setShowSchedulePopup(true);
};

onMounted(() => planStore.fetchWishList());
</script>

<style scoped>
.v-card {
  height: 100%;
  border-radius: 0;
}

.v-toolbar {
  border-radius: 0;
}

.v-card {
  transition: transform 0.2s;
}

.v-card:hover {
  transform: translateY(-4px);
}

:deep(.v-card-title) {
  font-size: 1rem;
  line-height: 1.4;
  word-break: keep-all;
}

:deep(.v-card-subtitle) {
  font-size: 0.875rem;
  line-height: 1.4;
  opacity: 0.8;
}
</style>
