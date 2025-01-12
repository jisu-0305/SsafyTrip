package com.trip.member;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.entity.Member;
import com.trip.member.repository.MemberRepository;
import com.trip.member.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void registerMember_Success() {
        // given
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail("test@example.com");
        registerRequestDTO.setPassword("password123");

        when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(new Member());

        // when
        memberService.registerMember(registerRequestDTO);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void registerMember_Failure() {
        // given
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail("test@example.com");
        registerRequestDTO.setPassword("password123");

        when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenThrow(RuntimeException.class);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(registerRequestDTO));
    }

    @Test
    void loginMember_Success() {
        // given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("password123");

        Member member = new Member();
       // member.setPassword("encodedPassword");

        when(memberRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())).thenReturn(true);

        // when
        memberService.loginMember(loginRequestDTO);

        // then
        verify(memberRepository, times(1)).findByEmail(loginRequestDTO.getEmail());
    }

    @Test
    void loginMember_Failure() {
        // given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("wrongPassword");

        Member member = new Member();
        //member.setPassword("encodedPassword");

        when(memberRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> memberService.loginMember(loginRequestDTO));
    }

    @Test
    void checkDuplicateEmail_True() {
        // given
        String email = "test@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(new Member()));

        // when
        boolean result = memberService.checkDuplicateEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void checkDuplicateEmail_False() {
        // given
        String email = "test@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        boolean result = memberService.checkDuplicateEmail(email);

        // then
        assertThat(result).isFalse();
    }
}