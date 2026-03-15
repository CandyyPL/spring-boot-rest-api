package com.candyy.bookweb.repositories;

import com.candyy.bookweb.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
    List<AuthorEntity> ageLessThan(int age);
}
