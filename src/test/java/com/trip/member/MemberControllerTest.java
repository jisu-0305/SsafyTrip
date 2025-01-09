package com.trip.member;

import com.trip.config.SecurityConfig;
import com.trip.member.controller.MemberController;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// MemberController만 테스트할 수 있도록 설정.
@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class) // Security 설정 포함
public class MemberControllerTest {

    // MockMvc: Spring MVC의 요청과 응답을 테스하기 위해 사용
    // 서버를 실행하지 않고, 가짜(Mock) 환경에서 컨트롤러 레벨의 동작을 테스트
    @Autowired
    private MockMvc mockMvc;

    // Mock 객체 주입
    // 컨트롤러 테스트에서는 서비스 레이어의 실제 동작을 테스트하지 않고, Mock으로 대체
    @MockBean
    private MemberService memberService;

    @Test
    void testRegister() throws Exception {
        Mockito.doNothing().when(memberService).registerMember(any(RegisterRequestDTO.class));

        // perform(...): HTTP 요청을 시뮬레이션하는 메서드
        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "홍",
                                  "gender": "M",
                                  "email": "mong@naver.com",
                                  "password": "123",
                                  "birthdate": "1990-05-11",
                                  "address": "서울특별시 강남구 역삼동 123-45",
                                  "role": "ROLE_USER"
                               }
                                """))
                .andExpect(status().isCreated()) // andExpect: API 테스트 시, 응답을 검증할 대 사용
                .andExpect(jsonPath("$.message").value("회원가입 성공하였습니다."));
                // jsonPath()는 JSON 응답에서 특정 필드를 확인, $.message는 JSON 응답 객체의 message 필드를 가리키고
                // .value("회원가입 성공하였습니다.")는 그 값이 "회원가입 성공하였습니다." 확인
    }

    @Test
    void testCheckSession() throws Exception {
        mockMvc.perform(get("/api/members/check-session")
                        //mockMvc에서 sessionAttr 메서드를 사용해서 테스트 시에 세션에 특정 속성(attribute)을 미리 설정
                        .sessionAttr("userId", "userId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Session 체크 완료!"));

        mockMvc.perform(get("/api/members/check-session"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Session에 해당 User 정보가 없음"));
    }
}
