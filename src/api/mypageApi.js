import myaxios from "@/api/axios.js";

function deleteUser(userData) {
  return myaxios.delete("/mypage/delete", {
    data: {
      email: userData.email,
      password: userData.password,
    },
  });
}

function getFavorites(params) {
  return myaxios.get('/favorites', {
    params: {
      page: params.page,
      size: params.size || 4,
      word: params.word
    }
  });
}

export { deleteUser, getFavorites };
