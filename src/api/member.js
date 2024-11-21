import myaxios from "@/util/axios.js";

function registerAxios(member, success) {
  console.log(JSON.stringify(member));
  myaxios.post("/members/register", JSON.stringify(member))
    .then(success);
}

function checkEmailDuplicationAxios(checkEmail, success) {
  myaxios.get('/members/' + checkEmail)
    .then(success);
}
export { registerAxios, checkEmailDuplicationAxios };


export const loginAPI = {
  login: (credentials) => 
    myaxios.post('/members/login', {
      email: credentials.email,
      passWord: credentials.password
    })
};