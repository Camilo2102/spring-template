package com.example.template.dto;

import com.example.template.models.Credential;
import com.example.template.models.TemplateUser;
import com.example.template.models.Company;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    private Credential credential;
    private TemplateUser user;
    private Company company;
}
