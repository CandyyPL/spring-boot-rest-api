package com.candyy.bookweb.repositories;

import com.candyy.bookweb.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<BookEntity, String>, PagingAndSortingRepository<BookEntity, String> {
    List<BookEntity> findByAuthorId(Integer authorId);
}
