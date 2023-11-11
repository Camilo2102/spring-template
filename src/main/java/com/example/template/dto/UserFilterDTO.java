package com.example.template.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterDTO {
    private String name;
    private String role;
    private String companyId;
    private String active;

    public UserFilterDTO() {
        this.name = "";
        this.role = "";
        this.companyId = "";
        this.active = "";
    }
}
