package com.example.template.service;

import com.example.template.constants.MessageConstants;
import com.example.template.models.GeneralModel;
import com.example.template.repository.GeneralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que cuenta con todos los metodos basicos del crud implementados y listos para su uso en un controlador que extienda de generalControler
 * @param <T> Clase que extienda de general model y que sea una entity para poder establecer el repositorio
 */
@Service
public class GeneralService<T extends GeneralModel> implements ICRUDService<T> {

    /**
     * Repositorio general, para tener los datos basicos del crud
     */
    protected final GeneralRepository<T> generalRepository;

    @Autowired
    public GeneralService(GeneralRepository<T> generalRepository) {
        this.generalRepository = generalRepository;
    }

    /**
     * Obtiene todos los datos de la entity T
     * @return una lista con todos los registros de T
     * @throws Exception error en caso de que falle la peticion
     */
    @Override
    public List<T> getAll(T t) throws Exception {
        return (List<T>) generalRepository.findByFilter(t, Pageable.unpaged());
    }

    /**
     * Obtiene todos los datos de la entity T teniendo en cuenta el paginado
     * @return una lista con todos los registros de T dentro del paginado
     * @throws Exception error en caso de que falle la peticion
     */
    @Override
    public List<T> getAllPageable(Pageable page) throws Exception {
        return generalRepository.findAll(page);
    }

    /**
     * Obtiene todos los registros segun el filtro
     * @param t el filtro a aplicar
     * @param page la cantidad de registros
     * @return la lista de los filtrso
     */
    @Override
    @Cacheable(value = "getAllByFilter", cacheManager = "cacheManager")
    public List<T> getAllByFilter(T t, Pageable page) {
        return generalRepository.findByFilter(t, page);
    }

    /**
     * Obtiene la cantidad de registros apra un filtro
     * @param t el filtro a aplicar
     * @return el nuymero ed registrsos del filtro
     */
    @Override
    @Cacheable(value = "countByFilter")
    public long countByFilter(T t) {
        return generalRepository.countByFilter(t);
    }

    /**
     * Obtiene un elemento por el id que se registro
     * @param id id por el cual buscar el registro
     * @return un optional que contiene el objeto
     * @throws Exception en caso de no encontrarse tira un error para indicar que no se econtró
     */
    @Override
    @Cacheable(value = "findById", key = "#id")
    public T findById(String id) throws Exception {
        Optional<T> t = generalRepository.findById(id);
        if(t.isEmpty()){
            throw new Exception(MessageConstants.EMPTY_MESSAGE);
        }
        return t.get();
    }

    /**
     * Guarda un registro T
     * @param t el objeto a guardar
     * @return el objeto que se guardo
     * @throws Exception en caso de obtener un dato duplicado o que no cumpla con las condiciones del registro lanza la excepcion
     */
    @Override
    public T save(T t) throws Exception {
        try {
            return generalRepository.save(t);
        }catch (Exception e){
            throw new Exception(MessageConstants.DUPLICATED_MESSAGE);
        }
    }

    /**
     * Elimina el registro en base al id ingresado
     * @param id id dado para seleccionar el registro a eliminar
     * @throws Exception en caso de que no se pueda eliminar envia un mensaje
     */
    @Override
    public void delete(String id) throws Exception {
        try {
            generalRepository.deleteById(id);
        }catch (Exception e){
            throw new Exception(MessageConstants.FAILED_MESSAGE);
        }

    }

    /**
     * Cuenta la candidad de registros totales de una tabla
     * @return el total de registros
     */
    @Override
    public long count() {
        return generalRepository.count();
    }


    /**
     * Guarda una lista de elementos de tipo T en el repositorio.
     *
     * @param t Lista de elementos de tipo T que se desea guardar.
     */
    @Override
    public void saveAll(List<T> t) {
        generalRepository.saveAll(t);
    }

    /**
     * Elimina todos los elementos relacionados con el identificador especificado.
     *
     * @param id Identificador que se utiliza para identificar y eliminar los elementos relacionados.
     */
    @Override
    public void deleteAll(String id) {
        generalRepository.deleteAllById(id);
    }
}
