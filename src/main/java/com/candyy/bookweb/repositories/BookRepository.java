package com.candyy.bookweb.repositories;

import com.candyy.bookweb.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    List<BookEntity> findByAuthorId(Integer authorId);
}
