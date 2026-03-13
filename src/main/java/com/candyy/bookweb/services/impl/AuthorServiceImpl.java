package com.candyy.bookweb.services.impl;

import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.repositories.AuthorRepository;
import com.candyy.bookweb.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity save(Integer id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        Iterable<AuthorEntity> authors = authorRepository.findAll();
        return StreamSupport
                .stream(authors.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorEntity> findOne(Integer id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean exists(Integer id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Integer id, AuthorEntity authorEntity) {
        authorEntity.setId(id);

        return authorRepository.findById(id).map(dbAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(dbAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(dbAuthor::setAge);

            return authorRepository.save(dbAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist! AuthorService.exist method should prevent this error from being thrown. Double check code."));
    }

    @Override
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }
}
