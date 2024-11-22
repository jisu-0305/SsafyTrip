<!-- components/search/AttractionList.vue -->
<script setup>
const props = defineProps({
  attractions: {
    type: Array,
    required: true
  },
  currentPage: {
    type: Number,
    required: true
  },
  totalPages: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['update:page']);

const handlePageChange = (page) => {
  emit('update:page', page);
};
</script>

<template>
  <v-card>
    <v-list lines="two">
      <v-list-item
        v-for="attraction in attractions"
        :key="attraction.title"
        :title="attraction.title"
        :subtitle="attraction.loc"
      >
        <template v-slot:prepend>
          <v-avatar size="100">
            <v-img :src="attraction.img" cover></v-img>
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