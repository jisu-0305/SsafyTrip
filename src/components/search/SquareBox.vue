<script setup>
defineProps({
  title: String,
  firstImage1: String,
  views: Number,
  latitude: Number,
  longitude: Number,
  contentId: Number
});
</script>

<template>
  <div class="overlay-content">
    <div class="card">
      <div class="image-container">
        <img 
          :src="firstImage1" 
          :alt="title"
          class="attraction-image"
        >
      </div>
      
      <div class="card-body">
        <h3 class="title">{{ title }}</h3>
        
        <div class="info-section">
          <div class="views">
            <i class="fas fa-eye"></i>
            <span>{{ views }}</span>
          </div>
          <div class="location">
            <i class="fas fa-map-marker-alt"></i>
            <span>{{ latitude }}, {{ longitude }}</span>
          </div>
        </div>

        <a :href="`/attraction/${contentId}`" class="detail-button">
          상세보기
        </a>
      </div>
    </div>
    <div class="arrow"></div>
  </div>
</template>

<style scoped>
.overlay-content {
  position: relative;
  margin-bottom: 12px;
  transform: translateY(-100%);
  pointer-events: auto;
  z-index: 1000;
}

.card {
  width: 280px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  position: relative;
  z-index: 1000;
}

.image-container {
  width: 100%;
  height: 140px;
  background: #f5f5f5;
  overflow: hidden;
}

.attraction-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-body {
  padding: 12px;
}

.title {
  margin: 0 0 8px 0;
  font-size: 1rem;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info-section {
  margin-bottom: 12px;
  font-size: 0.875rem;
  color: #666;
}

.views, .location {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 4px;
}

.detail-button {
  display: block;
  width: 100%;
  padding: 8px;
  background-color: #1976d2;
  color: white;
  text-align: center;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.detail-button:hover {
  background-color: #1565c0;
}

.arrow {
  position: absolute;
  bottom: 4px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 10px solid white;
  filter: drop-shadow(0 2px 2px rgba(0,0,0,0.1));
}
</style>
