package com.example.template.utils;

import com.example.template.models.GeneralModel;
import com.example.template.controller.GeneralController;
import com.example.template.repository.GeneralRepository;
import com.example.template.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanGenerator {

    private final BeanLocator beanLocator;

    @Autowired
    public BeanGenerator(BeanLocator beanLocator){
        this.beanLocator = beanLocator;
    }
    public GeneralController<?> getRequiredController(String repository) throws ClassNotFoundException {
        String repositoryName = repository + "Repository";
        GeneralRepository<?> repositorySelected = beanLocator.getBeanByString(repositoryName, GeneralRepository.class);
        GeneralService<? extends GeneralModel> service = new GeneralService<>(repositorySelected);

        return new GeneralController<>(service);
    }

    public Class<?>  getClassFromPath (String path) throws ClassNotFoundException {
        Class<?> className = beanLocator.getClassByName(StringUtil.firstUpperLetter(path));
        return className.asSubclass(GeneralModel.class);
    }
}
