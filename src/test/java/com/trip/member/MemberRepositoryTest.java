package com.trip.member;

import com.trip.member.entity.Member;
import com.trip.member.enums.Gender;
import com.trip.member.enums.Role;
import com.trip.member.enums.Status;
import com.trip.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 테스트")
//    @Rollback(false)
    void saveMember() {
        // given
        Member member = Member.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getUserId()).isGreaterThan(0); // ID가 자동 생성되었는지 확인
        assertThat(savedMember.getEmail()).isEqualTo("john@example.com");

    }


    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void findByEmail() {
        // given
        Member member = Member.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .password("encodedPassword")
                .gender(Gender.F)
                .birthDate(LocalDate.of(1995, 5, 15))
                .address("456 Another St")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail("jane@example.com");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("Jane Doe");
        assertThat(foundMember.get().getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @DisplayName("ID로 회원 조회 테스트")
    void findByUserId() {
        // given
        Member member = Member.builder()
                .name("Alex Smith")
                .email("alex@example.com")
                .password("encodedPassword")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1985, 8, 20))
                .address("789 Somewhere St")
                .role(Role.ROLE_ADMIN)
                .status(Status.ACTIVE)
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByUserId(savedMember.getUserId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("Alex Smith");
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteMember() {
        // given
        Member member = Member.builder()
                .name("Delete Me")
                .email("delete@example.com")
                .password("encodedPassword")
                .gender(Gender.F)
                .birthDate(LocalDate.of(2000, 2, 2))
                .address("Delete Address")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        memberRepository.delete(savedMember);
        Optional<Member> deletedMember = memberRepository.findByUserId(savedMember.getUserId());

        // then
        assertThat(deletedMember).isNotPresent();
    }

    @Test
    @DisplayName("이메일과 비밀번호로 회원 조회 테스트")
    void findByEmailAndPassword() {
        // given
        Member member = Member.builder()
                .name("Password Check")
                .email("password@example.com")
                .password("encodedPassword")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1998, 12, 12))
                .address("Password Address")
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmailAndPassword("password@example.com", "encodedPassword");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("Password Check");
    }


}
