package com.example.template.service;

import com.example.template.models.Credential;
import com.example.template.models.TemplateUser;
import com.example.template.repository.TemplateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService extends GeneralService<TemplateUser> {
    private final TemplateUserRepository userRepository;

    @Autowired
    public UserService(TemplateUserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public TemplateUser getByCredential(Credential credential) {
        return userRepository.findByCredential(credential);
    }

}
