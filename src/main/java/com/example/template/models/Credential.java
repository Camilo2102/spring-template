package com.example.template.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Entity indica que va a ser una tabla en la base de datos
//Getter y Setter genera getters y setters automaticamente
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Credential extends GeneralModel {

    @Column(nullable = false, length = 60, unique = true)
    private String mail;
    @Column(nullable = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(nullable = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String code;
    @Column(nullable = false, length = 12, unique = true)
    private String userName;


    @Override
    public String toString() {
        return "Credential{" +
                "mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
