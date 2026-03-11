package com.candyy.bookweb.repositories;

import com.candyy.bookweb.entities.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer> {
    Iterable<AuthorEntity> ageLessThan(int age);
}
