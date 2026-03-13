package com.candyy.bookweb.repositories;

import com.candyy.bookweb.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<BookEntity, String> {
    List<BookEntity> findByAuthorId(Integer authorId);
}
