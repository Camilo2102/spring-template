package com.example.template.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICRUDService<T> {

    /**
     * Obtener todos los registros
     * @return lista con todos los registros
     * @throws Exception en caso de obtener algun error
     */
    public List<T> getAll(T t) throws Exception;

    /**
     * Obtiene todos los registros a partir de un paginador
     * @param page paginador con la pagina y los registros
     * @return lista de los registros dentro del paginador
     * @throws Exception en caso de obtener algun error al paginar
     */
    public List<T> getAllPageable(Pageable page) throws Exception;

    /**
     * Obtiene un registro por el id dado
     * @param id id por el cual buscar el registro
     * @return estado de la busqueda en la base de datos
     * @throws Exception en caso de obtener un registro nulo
     */
    public T findById(String id) throws Exception;

    /**
     * Guarde el registro
     * @param t el objeto a guardar
     * @return el objeto guardado
     * @throws Exception exepcion en caso de un fallo al ingresar
     */
    public T save(T t) throws Exception;

    /**
     * Obtiene los registros que segun el filtro que se aplica
     * @param t el filtro a aplicar
     * @param page la cantidad de registros
     * @return los registros filtrados
     */
    public List<T> getAllByFilter(T t, Pageable page);

    /**
     * Obtiene la cantidad de registros que se tienen segun el filtro aplicado
     * @param t el filtro a aplicar
     * @return la cantidad de registros
     */
    public long countByFilter(T t);


    /**
     * ELimina el registro
     * @param id id dado para seleccionar el registro a eliminar
     * @throws Exception Execpcion en caso de fallar la eliminacion
     */
    public void delete(String id) throws Exception;

    /**
     * Encargado de obtener la cantidad de registros de la tabla
     * @return cantidad de elementos de la tabla
     */
    public long count();

    /**
     * Guarda una lista de elementos de tipo T en el repositorio.
     *
     * @param t Lista de elementos de tipo T que se desea guardar.
     */
    public void saveAll(List<T> t);

    /**
     * Elimina todos los elementos relacionados con el identificador especificado.
     *
     * @param id Identificador que se utiliza para identificar y eliminar los elementos relacionados.
     */
    public void deleteAll(String id);
}

