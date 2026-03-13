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
    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    public void bookCreatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        BookEntity book = TestDataUtil.createTestBook(author.getId());

        BookEntity savedBookEntity = bookRepository.save(book);

        Optional<BookEntity> result = bookRepository.findById(book.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void manyBooksCreatedAndRecalled() {
        List<BookEntity> books = new ArrayList<>();

        for (var i = 0; i < 3; i++) {
            AuthorEntity author = TestDataUtil.createTestAuthor();
            BookEntity book = TestDataUtil.createTestBook(author.getId());
            book.setIsbn(Integer.toString(i));

            BookEntity savedBookEntity = bookRepository.save(book);
            books.add(savedBookEntity);
        }

        Iterable<BookEntity> result = bookRepository.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(books);
    }

    @Test
    public void bookCreatedUpdatedAndRecalled() {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        BookEntity savedBook = bookRepository.save(book);

        final String newTitle = "LoTR";

        savedBook.setTitle(newTitle);

        savedBook = bookRepository.save(savedBook);

        Optional<BookEntity> result = bookRepository.findById(savedBook.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBook);
    }

    @Test
    public void bookCreatedAndDeleted() {
        AuthorEntity author = TestDataUtil.createTestAuthor();

        BookEntity book = TestDataUtil.createTestBook(author.getId());
        BookEntity savedBook = bookRepository.save(book);

        bookRepository.deleteById(savedBook.getIsbn());

        Optional<BookEntity> result = bookRepository.findById(savedBook.getIsbn());

        assertThat(result).isEmpty();
    }
}
