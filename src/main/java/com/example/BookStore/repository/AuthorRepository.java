package com.example.BookStore.repository;

import com.example.BookStore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends JpaRepository<Author, String> {
}
