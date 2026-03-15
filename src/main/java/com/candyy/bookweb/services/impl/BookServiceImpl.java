package com.candyy.bookweb.services.impl;

import com.candyy.bookweb.entities.BookEntity;
import com.candyy.bookweb.repositories.BookRepository;
import com.candyy.bookweb.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity save(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        Iterable<BookEntity> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public List<BookEntity> findByAuthorId(Integer authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    @Override
    public boolean exists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);

        return bookRepository.findById(isbn).map(dbBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(dbBook::setTitle);
            Optional.ofNullable(bookEntity.getAuthorId()).ifPresent(dbBook::setAuthorId);

            return bookRepository.save(dbBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exist! BookService.exist method should prevent this error from being thrown. Double check code.")
        );
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
