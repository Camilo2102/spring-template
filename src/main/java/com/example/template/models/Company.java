package com.example.template.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Company extends GeneralModel {
    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 120)
    private String description;

    @Column(nullable = false, length = 20)
    private String phone;

}
