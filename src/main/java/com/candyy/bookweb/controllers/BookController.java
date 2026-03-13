package com.candyy.bookweb.controllers;

import com.candyy.bookweb.dto.BookDTO;
import com.candyy.bookweb.entities.BookEntity;
import com.candyy.bookweb.mappers.Mapper;
import com.candyy.bookweb.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;
    private final Mapper<BookEntity, BookDTO> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDTO> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> createUpdateBook(
            @PathVariable("isbn") final String isbn,
            @RequestBody final BookDTO bookDTO
    ) {
        BookEntity book = bookMapper.mapFrom(bookDTO);

        boolean bookExists = bookService.exists(isbn);

        BookEntity savedBook = bookService.save(isbn, book);
        BookDTO savedBookDTO = bookMapper.mapTo(savedBook);

        if(!bookExists) {
            return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(savedBookDTO, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/books")
    public List<BookDTO> getAllBooks() {
        List<BookEntity> books = bookService.findAll();
        return books.stream().map(bookMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> getBook(@PathVariable("isbn") final String isbn) {
        Optional<BookEntity> book = bookService.findOne(isbn);

        return book.map(bookEntity -> {
            BookDTO bookDTO = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> partialUpdateBook(
            @PathVariable("isbn") final String isbn,
            @RequestBody final BookDTO bookDTO
    ) {
        if (!bookService.exists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity book = bookMapper.mapFrom(bookDTO);
        BookEntity savedBook = bookService.partialUpdate(isbn, book);

        BookDTO savedBookDTO = bookMapper.mapTo(savedBook);

        return new ResponseEntity<>(savedBookDTO, HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable("isbn") final String isbn) {
        if (!bookService.exists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.delete(isbn);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
