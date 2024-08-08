package com.example.BookStore.service;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.DTO.CategoryBooksDTO;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.model.*;
import com.example.BookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
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
        book.setCartBooks(new ArrayList<>());
        book.setOrderBooks(new ArrayList<>());
        book.setId(null);
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

        // Remove references from CartBook
        if (book.getCartBooks() != null) {
            for (CartBook cartBook : book.getCartBooks()) {
                Cart cart = cartBook.getCart();
                cart.getCartBooks().remove(cartBook);
            }
            book.getCartBooks().clear();
        }

        // Remove references from OrderBook
        if (book.getOrderBooks() != null) {
            for (OrderBook orderBook : book.getOrderBooks()) {
                Order order = orderBook.getOrder();
                order.getOrderBooks().remove(orderBook);
            }
            book.getOrderBooks().clear();
        }

        bookRepository.delete(book);
        return bookMapper.toDTO(book);
    }

    public List<CategoryBooksDTO> getBooksGroupedByCategory() {
        List<BookDTO> books = this.getAllBooks();
        Map<String, List<BookDTO>> groupedBooks = books.stream()
                .filter(book -> book.getCategory() != null)
                .collect(Collectors.groupingBy(BookDTO::getCategory));

        return groupedBooks.entrySet().stream()
                .map(entry -> new CategoryBooksDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
