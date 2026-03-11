package com.candyy.bookweb.repositories;

import com.candyy.bookweb.TestDataUtil;
import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {
    private final BookRepository underTests;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository bookRepository) {
        this.underTests = bookRepository;
    }

    @Test
    public void bookCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);

        BookEntity savedBookEntity = underTests.save(bookEntity);

        Optional<BookEntity> result = underTests.findById(bookEntity.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void manyBooksCreatedAndRecalled() {
        List<BookEntity> bookEntities = new ArrayList<>();

        for (var i = 0; i < 3; i++) {
            AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
            BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
            bookEntity.setIsbn(Integer.toString(i));

            BookEntity savedBookEntity = underTests.save(bookEntity);
            bookEntities.add(savedBookEntity);
        }

        Iterable<BookEntity> result = underTests.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(bookEntities);
    }

    @Test
    public void bookCreatedUpdatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        BookEntity savedBookEntity = underTests.save(bookEntity);

        final String newTitle = "LoTR";

        savedBookEntity.setTitle(newTitle);

        savedBookEntity = underTests.save(savedBookEntity);

        Optional<BookEntity> result = underTests.findById(savedBookEntity.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void bookCreatedAndDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

        BookEntity bookEntity = TestDataUtil.createTestBook(authorEntity);
        BookEntity savedBookEntity = underTests.save(bookEntity);

        underTests.deleteById(savedBookEntity.getIsbn());

        Optional<BookEntity> result = underTests.findById(savedBookEntity.getIsbn());

        assertThat(result).isEmpty();
    }
}
