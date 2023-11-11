package com.example.template.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TemplateUser extends GeneralModel  {

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 1)
    private String role;

    @Column(nullable = false, length = 1)
    private String active;

    @OneToOne
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", active='" + active + '\'' +
                ", credential=" + credential +
                ", company=" + company +
                '}';
    }
}
