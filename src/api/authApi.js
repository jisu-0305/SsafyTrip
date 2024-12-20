import myaxios from "@/api/axios.js";

const authApi = {
  // 회원가입 API
  register: (member) => {
    const registerData = {
      name: member.name,
      gender: member.gender,
      email: member.email,
      password: member.password,
      birthdate: member.birthdate,
      address: member.address,
      role: 'ROLE_USER',
      status: 'ACTIVE'
    }
    return myaxios.post("/members/register", registerData)
  },

  // 이메일 중복 체크 API
  checkEmailDuplication: (email) => 
    myaxios.get(`/members/${email}`),

  // 로그인 API
  login: (credentials) => 
    myaxios.post('/members/login', {
      email: credentials.email,
      passWord: credentials.password
    }),

  // 로그아웃 API
  logout: () => 
    myaxios.post('/members/logout'),

  // 세션 체크 API
  checkSession: () => 
    myaxios.get('/members/check-session')
}

export default authApi;