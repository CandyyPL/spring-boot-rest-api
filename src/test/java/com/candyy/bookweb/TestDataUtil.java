package com.candyy.bookweb;

import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.entities.BookEntity;

public final class TestDataUtil {

    public static final String authorName = "John";
    public static final Integer authorAge = 21;

    public static final String bookIsbn = "1";
    public static final String bookTitle = "Witcher";

    private TestDataUtil() {}

    public static AuthorEntity createTestAuthor() {
        return AuthorEntity.builder()
                .name(authorName)
                .age(authorAge)
                .build();
    }

    public static BookEntity createTestBook(final Integer authorId) {
        return BookEntity.builder()
                .isbn(bookIsbn)
                .title(bookTitle)
                .authorId(authorId)
                .build();
    }
}
