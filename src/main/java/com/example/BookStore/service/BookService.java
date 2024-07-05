package com.example.BookStore.service;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.Order;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private Book findBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Response.notFound("Book", id)));
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toDTO).toList();
    }

    public BookDTO getBookById(String id) {
        return bookMapper.toDTO(findBookById(id));
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        book.setCarts(new ArrayList<>());
        book.setOrders(new ArrayList<>());
        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Transactional
    public BookDTO updateBook(String id, BookDTO bookDTO) {
        Book bookDB = findBookById(id);

        if (bookDTO.getName() != null) {
            bookDB.setName(bookDTO.getName());
        }
        if (bookDTO.getImage() != null) {
            bookDB.setImage(bookDTO.getImage());
        }
        if (bookDTO.getDescription() != null) {
            bookDB.setDescription(bookDTO.getDescription());
        }
        if (bookDTO.getPrice() != 0) {
            bookDB.setPrice(bookDTO.getPrice());
        }
        if (bookDTO.getAuthor() != null) {
            bookDB.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getQuantity() != 0) {
            bookDB.setQuantity(bookDTO.getQuantity());
        }
        return bookMapper.toDTO(bookRepository.save(bookDB));
    }

    @Transactional
    public BookDTO deleteBook(String id) {
        Book book = findBookById(id);

        for (Cart cart : book.getCarts()) {
            cart.getBooks().remove(book);
        }
        book.getCarts().clear();

        for (Order order : book.getOrders()) {
            order.getBooks().remove(book);
        }
        book.getOrders().clear();

        return bookMapper.toDTO(book);
    }
}
