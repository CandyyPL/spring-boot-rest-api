package com.candyy.bookweb.services;

import com.candyy.bookweb.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity save(Integer id, AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Page<AuthorEntity> findAll(Pageable pageable);

    Optional<AuthorEntity> findOne(Integer id);

    boolean exists(Integer id);

    AuthorEntity partialUpdate(Integer id, AuthorEntity authorEntity);

    void delete(Integer id);
}
