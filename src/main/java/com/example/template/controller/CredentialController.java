package com.example.template.controller;

import com.example.template.dto.PasswordChangeDTO;
import com.example.template.dto.RegisterUserDTO;
import com.example.template.models.Credential;
import com.example.template.models.TemplateUser;
import com.example.template.utils.EmailUtil;
import com.example.template.utils.EncryptUtil;
import com.example.template.utils.IdGeneratorUtil;
import com.example.template.utils.TokenUtil;
import com.auth0.jwt.interfaces.Claim;
import com.example.template.constants.MessageConstants;
import com.example.template.constants.StatusConstants;
import com.example.template.models.Company;
import com.example.template.service.CompanyService;
import com.example.template.service.CredentialService;
import com.example.template.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


//ANotacion necesaria para decir que es un controlador y llamar las rutas
@RestController
@RequestMapping("/auth")
@Transactional
public class CredentialController extends GeneralController<Credential> {

    private final CredentialService credentialService;
    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public CredentialController(CredentialService credentialService, UserService userService, CompanyService companyService) {
        super(credentialService);
        this.credentialService = credentialService;
        this.userService = userService;
        this.companyService = companyService;
    }

    @Override
    @Hidden
    public List<Credential> getAll(Credential credential) throws Exception {
        throw new Exception(MessageConstants.NOT_IMPLEMENTED_ROUTE);
    }

    @Override
    @Hidden
    public long getAllCount() throws Exception {
        throw new Exception(MessageConstants.NOT_IMPLEMENTED_ROUTE);
    }

    @Override
    @Hidden
    public Credential getByID(String id) throws Exception {
        throw new Exception(MessageConstants.NOT_IMPLEMENTED_ROUTE);
    }

    @Override
    @Hidden
    public List<Credential> getAll(int pageNumber, int pageSize) throws Exception {
        throw new Exception(MessageConstants.NOT_IMPLEMENTED_ROUTE);
    }

    @Override
    @Hidden
    public Credential create(Credential credential) throws Exception {
        throw new Exception(MessageConstants.NOT_IMPLEMENTED_ROUTE);
    }

    @Override
    public Credential update(Credential credential) throws Exception {
        Credential credentialEncrypted = credentialWithEncryptedPassword(credential);
        return credentialService.save(credentialEncrypted);
    }

    private Credential credentialWithEncryptedPassword(Credential credential) {
        String hashedPassword = EncryptUtil.encryptValue(credential.getPassword());
        credential.setPassword(hashedPassword);
        return credential;
    }

    private Credential credentialWithEncryptedCode(Credential credential) {
        String hashedCode = EncryptUtil.encryptValue(credential.getCode());
        credential.setCode(hashedCode);
        return credential;
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Credential credential) throws Exception {
        Map<String, String> response = new HashMap<>();

        Optional<Credential> credentialFound = credentialService.findByUserAndMail(credential.getMail());

        if (credentialFound.isEmpty()) {
            throw new Exception(StatusConstants.UNAUTHORIZED);
        }


        boolean state = EncryptUtil.checkValues(credential.getPassword(), credentialFound.get().getPassword());
        if (!state) {
            throw new Exception(StatusConstants.UNAUTHORIZED);
        }

        Credential credentialData = credentialFound.get();
        TemplateUser user = userService.getByCredential(credentialData);

        if (user.getActive().equals("N")) {
            throw new Exception(StatusConstants.UNAUTHORIZED);
        }

        Map<String, String> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("role", String.valueOf(user.getRole()));

        String token = TokenUtil.generateToken(userData, 30);

        response.put(StatusConstants.STATUS, StatusConstants.AUTHORIZED);
        response.put("token", token);
        response.put("company", user.getCompany().getId());
        response.put("user", user.getId());
        response.put("role", user.getRole());
        return response;
    }

    @GetMapping("/validateCredential")
    public boolean validateUserName(@RequestParam("credential") String credential) {
        return credentialService.findByUserAndMail(credential).isPresent();
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterUserDTO registerUserDTO) throws Exception {
        Map<String, String> response = new HashMap<>();

        String code = IdGeneratorUtil.generateUUID(6);
        registerUserDTO.getUser().setId(IdGeneratorUtil.generateUUID());
        registerUserDTO.getUser().setId(IdGeneratorUtil.generateUUID());

        registerUserDTO.getCredential().setId(IdGeneratorUtil.generateUUID());
        registerUserDTO.getCompany().setId(IdGeneratorUtil.generateUUID());

        registerUserDTO.getCredential().setCode(code);

        Credential encryptedCredential = credentialWithEncryptedCode(registerUserDTO.getCredential());
        Credential createdCredential = credentialService.save(encryptedCredential);

        Company createdCompany = companyService.save((Company) registerUserDTO.getCompany());

        TemplateUser user = registerUserDTO.getUser();
        user.setCredential(createdCredential);
        user.setCompany(createdCompany);

        String temporalToken = EmailUtil.sendPasswordChangeMail(user, createdCredential.getMail(), code);

        userService.save(user);

        response.put("message", MessageConstants.SUCCESS_MESSAGE);
        response.put("temporalToken", temporalToken);
        return response;
    }

    @PostMapping("/registerUser")
    public Map<String, String> registerUser(@RequestBody TemplateUser user) throws Exception {
        Map<String, String> response = new HashMap<>();

        user.setId(IdGeneratorUtil.generateUUID());
        user.getCredential().setId(IdGeneratorUtil.generateUUID());

        String code = IdGeneratorUtil.generateUUID(6);
        user.getCredential().setCode(code);

        Credential encryptedCredential = credentialWithEncryptedCode(user.getCredential());
        Credential createdCredential = credentialService.save(encryptedCredential);

        Company company = companyService.findById(user.getCompany().getId());

        user.setCredential(createdCredential);
        user.setCompany(company);

        EmailUtil.sendPasswordChangeMail(user, createdCredential.getMail(), code);

        userService.save(user);

        response.put("message", MessageConstants.SUCCESS_MESSAGE);
        return response;
    }



    @GetMapping("/recoverPassword")
    public Map<String, String> recoverPassword(@RequestParam("mail") String mail) throws Exception {
        Map<String, String> response = new HashMap<>();

        Optional<Credential> credentialFound = credentialService.findByUserAndMail(mail);

        if (credentialFound.isEmpty()) {
            throw new Exception("Mail not found");
        }

        Credential credential = credentialFound.get();

        String code = IdGeneratorUtil.generateUUID(6);
        credential.setCode(code);

        Credential encryptedCredential = credentialWithEncryptedCode(credential);
        credentialService.save(encryptedCredential);

        TemplateUser userFound = userService.getByCredential(credential);

        EmailUtil.sendPasswordChangeMail(userFound, mail, code, true);

        response.put("message", "Email send successfully");
        return response;
    }


    @PostMapping("/enableUser")
    public Map<String, String> enableUser(@RequestBody PasswordChangeDTO passwordChangeDTO) throws Exception {
        Map<String, String> response = new HashMap<>();

        Map<String, Claim> payload;
        payload = TokenUtil.validateToken(passwordChangeDTO.getToken());

        String id = payload.get("id").asString();

        TemplateUser userFind = userService.findById(id);

        boolean isValid = EncryptUtil.checkValues(passwordChangeDTO.getCode(), userFind.getCredential().getCode());

        if (!isValid) {
            throw new Exception(MessageConstants.FAILED_MESSAGE);
        }

        userFind.getCredential().setPassword(passwordChangeDTO.getPassword());
        userFind.getCredential().setCode(null);

        Credential encryptedCredential = credentialWithEncryptedPassword(userFind.getCredential());
        userFind.setCredential(encryptedCredential);

        userFind.setActive("S");

        userService.save(userFind);

        response.put("message", "valid");

        return response;
    }

    @DeleteMapping("/disableUser")
    public Map<String, String> deleteUser(@RequestParam("id") String id) throws Exception {
        TemplateUser findUser = userService.findById(id);
        findUser.setActive("N");
        userService.save(findUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", MessageConstants.SUCCESS_MESSAGE);
        return response;
    }

    @GetMapping("/validateToken")
    public Map<String, String> validateToken(){
        Map<String, String> response = new HashMap<>();
        response.put("message", "valid");
        return response;
    }
}
