package com.example.template.controller;

import com.example.template.utils.BeanGenerator;
import com.example.template.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
public class ApiController {

    private final BeanGenerator beanGenerator;

    @Autowired
    public ApiController(BeanGenerator beanGenerator){
        this.beanGenerator = beanGenerator;
    }

    @RequestMapping("/**")
    public ResponseEntity<?> userRoute(HttpServletRequest request, @RequestBody(required = false) List<Object> body, @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, @RequestParam(value = "id", required = false) String id ) throws ClassNotFoundException {
        String path = RequestUtil.getPartFromURI(request.getRequestURI(), 2);
        GeneralController<?> controller = this.beanGenerator.getRequiredController(path);
        controller.setAClass(this.beanGenerator.getClassFromPath(path));
        return controller.handleRequest(request, body, pageNumber, pageSize, id);
    }





}
