import myaxios from "@/api/axios.js";

function deleteUser(userData) {
  console.log(userData);

  return myaxios.delete("/mypage/delete", {
    data: {
      email: userData.email,
      password: userData.password,
    },
  });
}
export { deleteUser };
