package com.candyy.bookweb.repositories;

import com.candyy.bookweb.TestDataUtil;
import com.candyy.bookweb.entities.AuthorEntity;
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
public class AuthorRepositoryIntegrationTests {
    private final AuthorRepository underTests;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository authorRepository) {
        this.underTests = authorRepository;
    }

    @Test
    public void authorCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

        underTests.save(authorEntity);
        Optional<AuthorEntity> result = underTests.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void manyAuthorsCreatedAndRecalled() {
        List<AuthorEntity> authorEntities = new ArrayList<>();

        for (var i = 0; i < 3; i++) {
            AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

            AuthorEntity savedAuthorEntity = underTests.save(authorEntity);
            authorEntities.add(savedAuthorEntity);
        }

        Iterable<AuthorEntity> result = underTests.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(authorEntities);
    }

    @Test
    public void authorCreatedUpdatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthorEntity = underTests.save(authorEntity);

        final String newName = "Alice";
        final Integer newAge = 26;

        savedAuthorEntity.setName(newName);
        savedAuthorEntity.setAge(newAge);

        savedAuthorEntity = underTests.save(savedAuthorEntity);

        Optional<AuthorEntity> result = underTests.findById(savedAuthorEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedAuthorEntity);
    }

    @Test
    public void authorCreatedAndDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthorEntity = underTests.save(authorEntity);

        underTests.deleteById(savedAuthorEntity.getId());

        Optional<AuthorEntity> result = underTests.findById(savedAuthorEntity.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void authorsWithAgeLessThanRecalled() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthor();
        authorEntityA.setName("Robert");
        authorEntityA.setAge(20);
        underTests.save(authorEntityA);

        AuthorEntity authorEntityB = TestDataUtil.createTestAuthor();
        authorEntityB.setName("William");
        authorEntityB.setAge(30);
        underTests.save(authorEntityB);

        AuthorEntity authorEntityC = TestDataUtil.createTestAuthor();
        authorEntityC.setName("Leonardo");
        authorEntityC.setAge(40);
        underTests.save(authorEntityC);

        Iterable<AuthorEntity> result = underTests.ageLessThan(35);

        assertThat(result).containsExactly(authorEntityA, authorEntityB);
    }
}
