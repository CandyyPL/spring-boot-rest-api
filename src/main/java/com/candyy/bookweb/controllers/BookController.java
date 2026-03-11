package com.candyy.bookweb.controllers;

import com.candyy.bookweb.dto.BookDTO;
import com.candyy.bookweb.entities.BookEntity;
import com.candyy.bookweb.mappers.Mapper;
import com.candyy.bookweb.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService bookService;
    private final Mapper<BookEntity, BookDTO> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDTO> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> createBook(@PathVariable("isbn") final String isbn, @RequestBody final BookDTO bookDTO) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDTO);
        BookEntity savedBookEntity = bookService.createBook(isbn, bookEntity);

        return new ResponseEntity<>(bookMapper.mapTo(savedBookEntity), HttpStatus.CREATED);
    }
}
