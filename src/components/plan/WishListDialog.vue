<template>
  <v-card flat class="wish-list-dialog">
    <v-toolbar color="primary" dark flat>
      <v-toolbar-title>찜 목록</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon @click="closeDialog">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>

    <v-card-text class="pa-4 wish-list-content">
      <v-row>
        <v-col 
          v-for="(spot, index) in planStore.wishList" 
          :key="index" 
          cols="12" 
          md="6"
        >
          <v-card variant="outlined" class="h-100">
            <div class="image-container">
              <v-img
                v-if="spot.firstImage1"
                :src="spot.firstImage1"
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
              <v-icon
                v-else
                size="150"
                color="grey-lighten-1"
                class="no-image-icon"
              >
                mdi-image-off
              </v-icon>
            </div>

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
  planStore.setSelectedSpot({
    ...spot,
    latitude: spot.latitude,
    longitude: spot.longitude
  });
  planStore.setShowWishList(false);
  planStore.setShowSchedulePopup(true);
};

onMounted(() => planStore.fetchWishList());
</script>

<style scoped>
.wish-list-dialog {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.wish-list-content {
  flex-grow: 1;
  overflow-y: auto;
  height: calc(100vh - 64px); /* 64px는 toolbar의 높이 */
}

.v-card {
  height: 100%;
  border-radius: 0;
}

.v-toolbar {
  border-radius: 0;
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

.image-container {
  width: 100%;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  overflow: hidden;
}

.no-image-icon {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
