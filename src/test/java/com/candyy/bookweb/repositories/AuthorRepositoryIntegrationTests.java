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
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Test
    public void authorCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

        authorRepository.save(authorEntity);
        Optional<AuthorEntity> result = authorRepository.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void manyAuthorsCreatedAndRecalled() {
        List<AuthorEntity> authorEntities = new ArrayList<>();

        for (var i = 0; i < 3; i++) {
            AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

            AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
            authorEntities.add(savedAuthorEntity);
        }

        Iterable<AuthorEntity> result = authorRepository.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(authorEntities);
    }

    @Test
    public void authorCreatedUpdatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        final String newName = "Alice";
        final Integer newAge = 26;

        savedAuthorEntity.setName(newName);
        savedAuthorEntity.setAge(newAge);

        savedAuthorEntity = authorRepository.save(savedAuthorEntity);

        Optional<AuthorEntity> result = authorRepository.findById(savedAuthorEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedAuthorEntity);
    }

    @Test
    public void authorCreatedAndDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);

        authorRepository.deleteById(savedAuthorEntity.getId());

        Optional<AuthorEntity> result = authorRepository.findById(savedAuthorEntity.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void authorsWithAgeLessThanRecalled() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthor();
        authorEntityA.setName("Robert");
        authorEntityA.setAge(20);
        authorRepository.save(authorEntityA);

        AuthorEntity authorEntityB = TestDataUtil.createTestAuthor();
        authorEntityB.setName("William");
        authorEntityB.setAge(30);
        authorRepository.save(authorEntityB);

        AuthorEntity authorEntityC = TestDataUtil.createTestAuthor();
        authorEntityC.setName("Leonardo");
        authorEntityC.setAge(40);
        authorRepository.save(authorEntityC);

        Iterable<AuthorEntity> result = authorRepository.ageLessThan(35);

        assertThat(result).containsExactly(authorEntityA, authorEntityB);
    }
}
