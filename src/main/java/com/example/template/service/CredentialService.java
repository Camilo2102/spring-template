package com.example.template.service;

import com.example.template.models.Credential;
import com.example.template.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Definir anotacion @Service para poder usar el autowired
@Service("CredentialService")
public class CredentialService extends GeneralService<Credential>{

    //Al crear una interface es necesario implementarla, en este caso la anotacion autowired se encarga de inicalizarla

    private final CredentialRepository credentialRepository;

    @Autowired
    public CredentialService(CredentialRepository credentialRepository) {
        super(credentialRepository);
        this.credentialRepository = credentialRepository;
    }

    public Optional<Credential> findByUserAndMail(String identifier){
        return credentialRepository.findByUserNameOrMail(identifier, identifier);
    }
}
