package com.trip.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.entity.Member;
import com.trip.member.enums.Gender;
import com.trip.member.enums.Role;
import com.trip.member.enums.Status;
import com.trip.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // application-test.properties 설정 사용
public class MemberIntegrationTest {

    @LocalServerPort
    private int port = 8080;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
//
//    @AfterEach
//    void cleanUp() {
//        memberRepository.deleteAll(); // 테스트 데이터 정리
//    }

    @Test
    @DisplayName("회원 생성 통합 테스트")
    void createMemberTest() throws Exception {
        // given
        String baseUrl = "http://localhost:" + port + "/api/members/register";
        RegisterRequestDTO requestDto = new RegisterRequestDTO();
        requestDto.setName("Integration User");
        requestDto.setEmail("integration@example.com");
        requestDto.setPassword("encodedPassword");
        requestDto.setGender(Gender.M);
        requestDto.setBirthDate(LocalDate.of(1990, 5, 15));
        requestDto.setAddress("123 Integration Street");
        requestDto.setRole(Role.ROLE_USER);

        // when
        var response = restTemplate.postForEntity(baseUrl, requestDto, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Member savedMember = memberRepository.findByEmail("integration@example.com").orElse(null);
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("Integration User");
        assertThat(savedMember.getEmail()).isEqualTo("integration@example.com");
    }

    @Test
    @DisplayName("회원 조회 통합 테스트")
    void getMemberTest() {
        // given
        Member member = Member.builder()
                .name("Existing User")
                .email("existing@example.com")
                .password("encodedPassword")
                .gender(Gender.F)
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("789 Existing St")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        String baseUrl = "http://localhost:" + port + "/api/members/" + member.getUserId();

        // when
        var response = restTemplate.getForEntity(baseUrl, Member.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Existing User");
        assertThat(response.getBody().getEmail()).isEqualTo("existing@example.com");
    }

/*    @Test
    @DisplayName("회원 삭제 통합 테스트")
    void deleteMemberTest() {
        // given
        Member member = Member.builder()
                .name("To Be Deleted")
                .email("delete@example.com")
                .password("encodedPassword")
                .gender(Gender.F)
                .birthDate(LocalDate.of(2000, 5, 10))
                .address("456 Delete St")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        String baseUrl = "http://localhost:" + port + "/api/members/" + member.getUserId();

        // when
        restTemplate.delete(baseUrl);

        // then
        Optional<Member> deletedMember = memberRepository.findByUserId(member.getUserId());
        assertThat(deletedMember).isNotPresent();
    }*/
}

