package com.example.template.repository;

import com.example.template.models.Credential;
import com.example.template.models.TemplateUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateUserRepository extends GeneralRepository<TemplateUser> {

    @Query("SELECT U FROM TemplateUser AS U WHERE U.role NOT IN('C', 'M') AND U.active NOT IN ('N')" +
            "AND (:#{#user.name} is null or U.name LIKE  %:#{#user.name}%) " +
            "AND (:#{#user.role} is null or U.role LIKE %:#{#user.role}%) " +
            "AND U.company.id LIKE %:#{#user.company.id}% ")
    List<TemplateUser> findByFilter(
            @Param("user") TemplateUser user,
            Pageable page
    );

    @Query("SELECT COUNT(U) FROM TemplateUser AS U WHERE U.role NOT IN('C', 'M') AND U.active NOT IN ('N')" +
            "AND U.name LIKE  %:#{#user.name}% " +
            "AND U.role LIKE %:#{#user.role}% " +
            "AND U.company.id LIKE %:#{#user.company.id}% ")
    long countByFilter(
            @Param("user") TemplateUser user
    );

    @Query("SELECT u FROM TemplateUser u WHERE u.credential = :credential")
    public TemplateUser findByCredential(@Param("credential") Credential credential);
}
