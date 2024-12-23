package com.trip.member.entity;



import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.enums.Gender;
import com.trip.member.enums.Role;
import com.trip.member.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // 컬럼명은 'user_id'로 지정
    private long userId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)  // ENUM 값을 문자열로 저장
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "address", length = 500)
    private String address;


    @Column(name = "join_date")
    private LocalDateTime joinDate;

    //EnumType.ORDINAL: 내부적으로 숫자 저장, STRING은 ENUM의 이름 저장
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public void updateAddress(String address){
        if(address != null){
            this.address = address;
        }
    }

    public void updateGender(Gender gender){
        if(gender != null){
            this.gender = gender;
        }
    }

    public void deactivate() {
        this.status = Status.INACTIVE; // 상태 변경
    }

    public static Member fromDTO(RegisterRequestDTO dto) {
        return Member.builder()
                .name(dto.getName())
                .gender(dto.getGender())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .role(dto.getRole())
                .build();
    }

    @PrePersist
    public void prePersist(){
        if(this.status == null){
            this.status = Status.ACTIVE;
        }
    }

}
