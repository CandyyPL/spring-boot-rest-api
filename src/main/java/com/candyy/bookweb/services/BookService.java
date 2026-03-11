package com.candyy.bookweb.services;

import com.candyy.bookweb.entities.BookEntity;

public interface BookService {

    BookEntity createBook(String isbn, BookEntity bookEntity);
}
