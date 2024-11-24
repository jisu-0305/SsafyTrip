<script setup>
import { defineProps } from "vue";
import { useRouter } from "vue-router";

const props = defineProps({
  articles: {
    type: Array,
    required: true,
  },
  columns: {
    type: Array,
    required: true,
    // [{ key: 'noticeId', label: '번호' }, { key: 'title', label: '제목' }, ...]
  },
  type: {
    type: String,
    required: true,
    validator: (value) => ['notice', 'qna', 'review'].includes(value)
  }
});

const router = useRouter();

const moveToDetail = (id) => {
  router.push({ name: `${props.type}-detail`, params: { id } });
};

const formatValue = (item, column) => {
  if (column.formatter) {
    return column.formatter(item[column.key]);
  }
  
  if (column.key === 'createdAt') {
    return new Date(item[column.key]).toLocaleDateString();
  }
  
  return item[column.key];
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
        :key="article.id || article.noticeId" 
        @click="moveToDetail(article.id || article.noticeId)"
        style="cursor: pointer"
      >
        <td 
          v-for="column in columns" 
          :key="column.key"
          :class="column.class"
          :style="column.style"
        >
          {{ formatValue(article, column) }}
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
