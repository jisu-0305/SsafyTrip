<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import placeholderImage from "@/assets/logo.png";

// Constants
const ITEMS_PER_PAGE = 5; // 한 페이지에 보여줄 항목 수
const PAGE_GROUP_SIZE = 5; // 페이지 그룹 크기

// Data
const breadcrumbs = [
  { text: "Home", href: "/" },
  { text: "Mypage", href: "/form" },
  { text: "Advanced Form", href: "/form/advanced" },
];

const search = ref(""); // 검색어
const attractionList = ref([]); // 전체 데이터
const filteredItems = ref([]); // 현재 페이지 데이터
const page = ref(1); // 현재 페이지
const pages = ref(1); // 전체 페이지 수

// Fetch Data
const fetchAttractions = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/favorite?page=${page.value}&size=4&word=${search.value}`
    );
    attractionList.value = response.data.attractionList || [];
    pages.value = response.data.totalPages || 1; // 전체 페이지 수 설정
    filteredItems.value = attractionList.value; // 현재 페이지 항목
  } catch (error) {
    console.error("Error fetching attractions:", error);
  }
};

const onSearch = () => {
  fetchAttractions();
};

const deleteAttraction = async (attractId) => {
  try {
    await axios.delete(`http://localhost:8080/api/favorite/${attractId}`);
    console.log(`Deleted attraction with id: ${attractId}`);
    fetchAttractions();
  } catch (error) {
    console.error("Error deleting attraction:", error);
  }
};

const heartClick = (no) => {
  console.log("하트 클릭");

  console.log(no);
  deleteAttraction(no);
};

// Handle Page Change
const onPageChange = (newPage) => {
  page.value = newPage; // 페이지 업데이트
  console.log("페이지 클릭");
  console.log("새로운 페이지 번호:", newPage);

  fetchAttractions(); // 새로운 데이터 로드
};

// Initial Data Load
onMounted(() => {
  fetchAttractions();
});
</script>

<template>
  <v-container>
    <v-row>
      <!-- Breadcrumbs -->
      <v-breadcrumbs :items="breadcrumbs" class="mb-4">
        <template v-slot:item="{ item }">
          <v-breadcrumbs-item :href="item.href">{{
            item.text
          }}</v-breadcrumbs-item>
        </template>
      </v-breadcrumbs>

      <!-- Search and Sort -->
      <v-col cols="12" class="mb-4 d-flex align-center justify-space-between">
        <div class="d-flex align-center" style="width: 50%">
          <v-text-field
            v-model="search"
            placeholder="Search name"
            prepend-inner-icon="mdi-magnify"
            outlined
            hide-details
            class="mr-2"
            style="
              background-color: white;
              border-radius: 4px;
              border: 2px solid #007bff;
              height: 56px;
            "
          ></v-text-field>
          <v-btn
            color="primary"
            @click="onSearch"
            style="
              min-width: 40px;
              border-radius: 4px;
              background-color: #007bff;
              color: white;
              height: 56px;
            "
          >
            <v-icon left>mdi-magnify</v-icon>
            Search
          </v-btn>
        </div>
        <div class="d-flex">
          <v-btn text class="mr-2" style="text-transform: none">이름순</v-btn>
          <v-btn text class="mr-2" style="text-transform: none"
            >좋아요 순</v-btn
          >
          <v-btn text style="text-transform: none">조회수 순</v-btn>
        </div>
      </v-col>

      <!-- Cards -->
      <v-col cols="12" v-for="(item, index) in filteredItems" :key="index">
        <v-card class="mb-2" height="140px" style="border: none">
          <v-row no-gutters>
            <v-col cols="4">
              <v-img
                :src="item.firstImage1 || placeholderImage"
                height="130px"
                width="500px"
              ></v-img>
            </v-col>
            <v-col cols="8" class="position-relative">
              <v-btn
                icon
                class="position-absolute"
                style="top: 0; right: 0; background-color: transparent"
              >
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
              <v-card-text>
                <v-chip color="green" text-color="white" class="mb-2"
                  >여행지</v-chip
                >
                <h3 style="margin-top: 4px; margin-bottom: 4px">
                  {{ item.title }}
                </h3>
                <p style="margin-top: 2px; margin-bottom: 4px">
                  위도: {{ item.latitude }}, 경도: {{ item.longitude }}
                </p>
                <div class="d-flex align-center" @click="heartClick(item.no)">
                  <v-icon left>mdi-heart</v-icon>
                  <span>{{ item.hit }}</span>
                </div>
              </v-card-text>
            </v-col>
          </v-row>
        </v-card>
        <v-divider></v-divider>
      </v-col>

      <!-- Pagination -->
      <v-col cols="12" class="d-flex justify-center">
        <v-pagination
          v-model="page"
          :length="10"
          :total-visible="5"
          @click="onPageChange(page)"
        ></v-pagination>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-breadcrumbs {
  font-size: 14px;
}
.v-card-text h3 {
  margin: 0;
  font-size: 1.25rem;
}
.v-card-text p {
  margin: 0;
  color: grey;
}
.v-card {
  height: 140px;
}
</style>
