<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  articles: {
    type: Array,
    required: true,
  },
  columns: {
    type: Array,
    required: true,
  },
  onRowClick: {
    type: Function,
    required: true
  }
});

const formatValue = (item, column) => {
  if (column.formatter) {
    const formattedValue = column.formatter(item[column.key]);
    if (typeof formattedValue === 'object') {
      return formattedValue;
    }
    return { text: formattedValue };
  }
  
  if (column.key === 'createdAt') {
    return { text: new Date(item[column.key]).toLocaleDateString() };
  }
  
  return { text: item[column.key] };
};
</script>

<template>
  <v-table hover>
    <thead>
      <tr>
        <th 
          v-for="column in columns" 
          :key="column.key"
          :class="column.class"
          :style="column.style"
        >
          {{ column.label }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr 
        v-for="article in articles" 
        :key="article.id" 
        @click="onRowClick(article)"
        style="cursor: pointer"
      >
        <td 
          v-for="column in columns" 
          :key="column.key"
          :class="column.class"
          :style="column.style"
        >
          <v-chip
            v-if="column.key === 'status'"
            :color="formatValue(article, column).color"
            size="small"
          >
            {{ formatValue(article, column).text }}
          </v-chip>
          <template v-else>
            {{ formatValue(article, column).text }}
          </template>
        </td>
      </tr>
    </tbody>
  </v-table>
</template>

<style scoped>
.v-table {
  cursor: pointer;
}
tr:hover {
  background-color: #f5f5f5;
}
</style>
