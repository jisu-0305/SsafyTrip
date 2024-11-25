<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/authStores';
import { getInitials, getProfileColor } from '@/utils/profileUtils';

const props = defineProps({
  currentFlag: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['updateFlag']);
const authStore = useAuthStore();

const userEmail = computed(() => authStore.user?.email || '');
const userInitials = computed(() => getInitials(userEmail.value));
const avatarColor = computed(() => getProfileColor(userEmail.value));

const menuItems = [
  { id: 1, title: '나의 활동', icon: 'mdi-account-circle' },
  { id: 2, title: '회원정보 수정', icon: 'mdi-account-edit' },
  { id: 3, title: '회원탈퇴', icon: 'mdi-account-remove' }
];
</script>

<template>
  <div class="d-flex flex-column align-center pa-8">
    <v-avatar 
      size="120" 
      class="mb-6"
      :color="avatarColor"
    >
      <span class="text-h3 font-weight-bold white--text">
        {{ userInitials }}
      </span>
    </v-avatar>
    
    <div class="text-h5 text-white font-weight-bold mb-8">
      {{ userEmail }}
    </div>
    
    <v-list 
      nav 
      width="100%" 
      bg-color="transparent"
      class="navigation-list"
    >
      <template v-for="(item, index) in menuItems" :key="item.id">
        <v-divider v-if="index !== 0" color="white" class="my-2"></v-divider>
        <v-list-item
          :active="currentFlag === item.id"
          active-color="white"
          class="mb-2"
          @click="emit('updateFlag', item.id)"
        >
          <template v-slot:prepend>
            <v-icon color="white">{{ item.icon }}</v-icon>
          </template>
          <v-list-item-title class="text-white">{{ item.title }}</v-list-item-title>
        </v-list-item>
      </template>
    </v-list>
  </div>
</template>

<style scoped>
.navigation-list {
  background-color: transparent !important;
}

:deep(.v-list-item--active) {
  background: rgba(255, 255, 255, 0.1);
}

:deep(.v-list-item) {
  background: transparent !important;
}
</style>