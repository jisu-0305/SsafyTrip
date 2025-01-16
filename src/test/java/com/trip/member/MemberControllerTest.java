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

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class) // Security 설정 포함
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void testRegister() throws Exception {
        Mockito.doNothing().when(memberService).registerMember(any(RegisterRequestDTO.class));

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("회원가입 성공하였습니다."));
    }

    @Test
    void testCheckSession() throws Exception {
        mockMvc.perform(get("/api/members/check-session")
                        .sessionAttr("userId", "userId123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Session 체크 완료!"));

        mockMvc.perform(get("/api/members/check-session"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Session에 해당 User 정보가 없음"));
    }
}
