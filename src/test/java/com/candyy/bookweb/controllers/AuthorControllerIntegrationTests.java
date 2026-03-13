package com.candyy.bookweb.controllers;

import com.candyy.bookweb.TestDataUtil;
import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.entities.BookEntity;
import com.candyy.bookweb.repositories.AuthorRepository;
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
public class AuthorControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorControllerIntegrationTests(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            AuthorRepository authorRepository,
            BookRepository bookRepository
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @AfterEach
    void setup() {
        authorRepository.deleteAll();
    }

    @Test
    public void createAuthorGivesCorrectResponse() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void createAuthorReturnsSavedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
        );
    }

    @Test
    public void recallAllAuthorsGivesCorrectResponse() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void recallAllAuthorsReturnsAuthorsList() throws Exception {
        List<AuthorEntity> authors = new ArrayList<>();

        for (var i = 0; i < 3; i++) {
            AuthorEntity author = TestDataUtil.createTestAuthor();
            AuthorEntity savedAuthor = authorRepository.save(author);

            authors.add(savedAuthor);
        }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(authors.get(0).getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[2].id").value(authors.get(2).getId())
        );
    }

    @Test
    public void recallAuthorGivesCorrectResponse() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + savedAuthor.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void recallAuthorGivesCorrectResponseWhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/0")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void recallAuthorReturnsCorrectAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        );
    }

    @Test
    public void fullUpdateAuthorGivesCorrectResponse() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void fullUpdateAuthorGivesCorrectResponseWhenAuthorDoesNotExist() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void fullUpdateAuthorReturnsCorrectAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        authorEntity.setName("Test Name");
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
        );
    }

    @Test
    public void partialUpdateAuthorGivesCorrectResponse() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        AuthorEntity partialAuthor = AuthorEntity.builder().name("Test Name").build();
        String authorJson = objectMapper.writeValueAsString(partialAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void partialUpdateAuthorGivesCorrectResponseWhenAuthorDoesNotExist() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void partialUpdateAuthorReturnsCorrectAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        AuthorEntity partialAuthor = AuthorEntity.builder().name("Test Name").build();
        String authorJson = objectMapper.writeValueAsString(partialAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(partialAuthor.getName())
        );
    }

    @Test
    public void deleteAuthorGivesCorrectResponse() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void deleteAuthorGivesCorrectResponseWhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/0")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void deleteAuthorGivesCorrectResponseWhenAuthorHasAnyBooks() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorRepository.save(author);

        BookEntity book = TestDataUtil.createTestBook(savedAuthor.getId());
        bookRepository.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }
}
