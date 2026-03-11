package com.candyy.bookweb.repositories;

import com.candyy.bookweb.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, String> {
}
