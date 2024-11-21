import myaxios from "@/api/axios.js";

// 회원가입 API
function registerAxios(member, success) {
  const registerData = {
    name: member.name,
    gender: member.gender,      // 'M' 또는 'F'
    email: member.email,
    password: member.password,
    birthdate: member.birthdate, // 날짜 형식 변경
    address: member.address,
    role: 'USER',              // 기본값 USER
    status: 'ACTIVE'           // 기본값 ACTIVE
  }

  return myaxios.post("/members/register", registerData)
    .then(success)
}

// 이메일 중복 체크 API
function checkEmailDuplicationAxios(checkEmail, success) {
  return myaxios.get(`/members/${checkEmail}`)
    .then(success)
    .catch(error => {
      console.error('이메일 중복 체크 에러:', error)
      throw error
    })
}

// 로그인 API
const loginAPI = {
  login: (credentials) => 
    myaxios.post('/members/login', {
      email: credentials.email,
      passWord: credentials.password
    })
}

export { registerAxios, checkEmailDuplicationAxios, loginAPI }