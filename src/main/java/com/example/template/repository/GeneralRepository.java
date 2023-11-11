package com.example.template.repository;

import com.example.template.models.GeneralModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio general que permite las peticiones basicas del crud
 * @param <T> clase que debe extender de general model, debe ser una entity
 *           t
 */
@Repository
public interface GeneralRepository<T extends GeneralModel> extends CrudRepository<T, String> {

    @Query("select 1")
    public List<T> findByFilter(T t, Pageable page);
    @Query("select 1")
    public long countByFilter(T t);

    @Query("select 1")
    public void deleteAllById(String id);
    public List<T> findAll(Pageable page);
}
