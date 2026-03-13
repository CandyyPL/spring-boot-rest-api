package com.candyy.bookweb.controllers;

import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.dto.AuthorDTO;
import com.candyy.bookweb.entities.BookEntity;
import com.candyy.bookweb.mappers.Mapper;
import com.candyy.bookweb.services.AuthorService;
import com.candyy.bookweb.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final Mapper<AuthorEntity, AuthorDTO> authorMapper;
    private final BookService bookService;

    public AuthorController(
            AuthorService authorService,
            Mapper<AuthorEntity, AuthorDTO> authorMapper,
            BookService bookService
    ) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
        this.bookService = bookService;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody final AuthorDTO authorDTO) {
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDTO);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity.getId(), authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDTO> getAllAuthors() {
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream()
                .map(authorMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable("id") final Integer id) {
        Optional<AuthorEntity> author = authorService.findOne(id);

        return author.map(authorEntity -> {
            AuthorDTO authorDTO = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> fullUpdateAuthor(
            @PathVariable("id") final Integer id,
            @RequestBody final AuthorDTO authorDTO
    ) {
        if(!authorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDTO);

        AuthorEntity savedAuthor = authorService.save(id, authorEntity);
        AuthorDTO savedAuthorDTO = authorMapper.mapTo(savedAuthor);

        return new ResponseEntity<>(savedAuthorDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> partialUpdateAuthor(
            @PathVariable("id") final Integer id,
            @RequestBody final AuthorDTO authorDTO
    ) {
        if(!authorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDTO);
        AuthorEntity savedAuthor = authorService.partialUpdate(id, authorEntity);

        AuthorDTO savedAuthorDTO = authorMapper.mapTo(savedAuthor);

        return new ResponseEntity<>(savedAuthorDTO, HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable("id") final Integer id) {
        if(!authorService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<BookEntity> authorBooks = bookService.findByAuthorId(id);

        if(authorBooks.isEmpty()) {
            authorService.delete(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
