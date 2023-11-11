package com.example.template.models.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Email {
    private String emailTo;
    private String subject;
    private String content;

}
