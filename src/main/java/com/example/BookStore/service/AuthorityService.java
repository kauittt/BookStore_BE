package com.example.BookStore.service;

import com.example.BookStore.DTO.AuthorityDTO;
import com.example.BookStore.mapstruct.AuthorityMapper;
import com.example.BookStore.model.Authority;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper = AuthorityMapper.INSTANCE;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    //! Important
    @Transactional
    public List<Authority> getAllAuthoritiesByUsername(String username) {
        return authorityRepository.findAuthoritiesByUsername(username);
    }

    private Authority findAuthorityById(String id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Response.notFound("Order", id)));
    }

    public List<AuthorityDTO> getAllAuthorities() {
        return authorityRepository.findAll().stream().map(authorityMapper::todTO).toList();
    }

    public AuthorityDTO getAuthorityById(String id) {
        return authorityMapper.todTO(findAuthorityById(id));
    }
}
