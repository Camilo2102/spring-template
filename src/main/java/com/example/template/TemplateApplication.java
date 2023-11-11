package com.example.template;

import com.example.template.utils.EmailUtil;
import com.example.template.utils.TokenUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Template api",
                version = "0.1",
                description = "Listado de endpoints de template",
                license = @License(name = "Apache 2.0", url = "http://nose:&"),
                contact = @Contact(url = "http://template.com", name = "Template", email = "template@mail.com")
        )
)
@ComponentScan(basePackages = "com.example.template.repository")
@EnableCaching
public class TemplateApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initializeValues();
    }

    private void initializeValues() {
        TokenUtil.initializeTokenUtil();
        EmailUtil.initializeEmailUtil();
    }
}