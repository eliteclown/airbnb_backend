package com.karthik.projects.airBnbApp.dtos;

import com.karthik.projects.airBnbApp.entities.User;
import com.karthik.projects.airBnbApp.entities.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {
    private Long id;
    private User userEntity;
    private String name;
    private Gender gender;
    private Integer age;
}
