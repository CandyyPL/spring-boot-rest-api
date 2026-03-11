package com.candyy.bookweb.services.impl;

import com.candyy.bookweb.entities.AuthorEntity;
import com.candyy.bookweb.repositories.AuthorRepository;
import com.candyy.bookweb.services.AuthorService;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity createAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }
}
