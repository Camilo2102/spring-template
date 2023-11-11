package com.example.template.config;

import com.example.template.interceptors.LoggerInterceptor;
import com.example.template.interceptors.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@EnableWebMvc
public class TemplateConfig implements WebMvcConfigurer {

    private final TokenHandler tokenHandler;
    private final LoggerInterceptor loggerInterceptor;

    @Autowired
    public TemplateConfig(TokenHandler tokenHandler, LoggerInterceptor loggerInterceptor) {
        this.tokenHandler = tokenHandler;
        this.loggerInterceptor = loggerInterceptor;
    }

    /**
     * Agrega validaciones intermedias para las rutas especificacdas
     * @param registry registro de interceptores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor).addPathPatterns("/**");
        this.initializeAuthRoute(registry);
        this.initializeUserRoute(registry);
        this.initializeInventoryRoute(registry);
    }


    /**
     * Se encarga de inicializar la ruta de auth y agregar los permisos
     * @param registry recibe el registru para asignarle los parametros de la ruta
     */
    private void initializeAuthRoute(InterceptorRegistry registry) {
        this.tokenHandler.addToPermissionListByPath("auth", new char[]{'C', 'M','D', 'P', 'G'});
        registry.addInterceptor(tokenHandler).addPathPatterns("/auth/**").excludePathPatterns(
                "/auth/login",
                "/auth/register",
                "/auth/validateCredential",
                "/auth/enableUser",
                "/auth/recoverPassword"
        );
    }

    private void initializeUserRoute(InterceptorRegistry registry) {
        this.tokenHandler.addToPermissionListByPath("api/user", new char[]{'C', 'M'});
        registry.addInterceptor(tokenHandler).addPathPatterns("/api/user/**");
    }

    private void initializeInventoryRoute(InterceptorRegistry registry){
        this.tokenHandler.addToPermissionListByPath("api/inventory", new char[]{'C', 'M'});
        this.tokenHandler.addToPermissionListByPath("api/inventory/getAllByFilters", new char[]{'C', 'M', 'D', 'P', 'G'});
        this.tokenHandler.addToPermissionListByPath("api/inventory/countAllByFilters", new char[]{'C', 'M', 'D', 'P', 'G'});
        registry.addInterceptor(tokenHandler).addPathPatterns("/api/inventory/**");
    }


    /**
     * Registro de cors y metodos permitidos
     * @param registry registro de cors
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }


}
