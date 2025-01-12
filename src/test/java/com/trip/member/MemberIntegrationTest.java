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
//@ActiveProfiles("test") application-test.properties 설정 사용, 운영 DB와 다른 데이터베이스 필요.
public class MemberIntegrationTest {

}

