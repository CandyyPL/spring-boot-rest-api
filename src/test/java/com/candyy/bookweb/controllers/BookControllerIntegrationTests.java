package com.candyy.bookweb.controllers;

import com.candyy.bookweb.TestDataUtil;
import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.entities.BookEntity;
import com.candyy.bookweb.repositories.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookRepository bookRepository;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookRepository bookRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.bookRepository = bookRepository;
    }

    @AfterEach
    void setup() {
        bookRepository.deleteAll();
    }

    @Test
    public void createBookGivesCorrectResponse() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void createBookReturnsSavedBook() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void recallAllBooksGivesCorrectResponse() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void recallAllBooksReturnsBooksList() throws Exception {
        List<BookEntity> books = new ArrayList<>();

        for (var i = 0; i < 3; i++) {
            AuthorEntity author = TestDataUtil.createTestAuthor();

            BookEntity book = TestDataUtil.createTestBook(author.getId());
            book.setIsbn(Integer.toString(i));

            BookEntity savedBook = bookRepository.save(book);

            books.add(savedBook);
        }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn")
                        .value(books.get(0).getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[2].isbn")
                        .value(books.get(2).getIsbn())
        );
    }

    @Test
    public void recallBookGivesCorrectResponse() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        BookEntity book = TestDataUtil.createTestBook(author.getId());

        BookEntity savedBook = bookRepository.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void recallBookGivesCorrectResponseWhenBookDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/0")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void recallBookReturnsCorrectBook() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        BookEntity book = TestDataUtil.createTestBook(author.getId());

        BookEntity savedBook = bookRepository.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBook.getIsbn())
        );
    }

    @Test
    public void fullUpdateBookGivesCorrectResponse() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        String bookJson = objectMapper.writeValueAsString(book);

        bookRepository.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void fullUpdateBookReturnsCorrectBook() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        bookRepository.save(book);

        book.setTitle("Test Title");

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void partialUpdateBookGivesCorrectResponse() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        String bookJson = objectMapper.writeValueAsString(book);

        bookRepository.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void partialUpdateBookGivesCorrectResponseWhenBookDoesNotExist() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void partialUpdateBookReturnsCorrectBook() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        bookRepository.save(book);

        BookEntity partialBook = BookEntity.builder().title("Test Title").build();
        String bookJson = objectMapper.writeValueAsString(partialBook);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(partialBook.getTitle())
        );
    }

    @Test
    public void deleteBookGivesCorrectResponse() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        BookEntity savedBook = bookRepository.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void deleteBookGivesCorrectResponseWhenBookDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/0")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
