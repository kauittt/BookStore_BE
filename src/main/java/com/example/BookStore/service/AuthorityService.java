package com.example.BookStore.service;

import com.example.BookStore.model.Authority;
import com.example.BookStore.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    public Optional<Authority> getAuthorityById(String id) {
        return authorityRepository.findById(id);
    }

    @Transactional
    public Authority saveAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Transactional
    public void deleteAuthority(String id) {
        authorityRepository.deleteById(id);
    }
}
