package com.example.BookStore.service;

import com.example.BookStore.DTO.CartRequestDTO;
import com.example.BookStore.DTO.CartResponseDTO;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.mapstruct.CartMapper;
import com.example.BookStore.model.*;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.CartRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper = CartMapper.INSTANCE;
    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final BookRepository bookRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CartService(CartRepository cartRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    //- Helper
    private Cart findCartById(String id) {
        Cart cart = cartRepository.findCartByUserId(id);
        if (cart == null) {
            throw new RuntimeException(Response.notFound("Cart", id));
        }
        return cart;
    }

    public List<CartResponseDTO> getAllCarts() {
        return cartRepository.findAll().stream().map(cartMapper::toDTOWithBooks).toList();
    }

    public CartResponseDTO getCartById(String id) {
        Cart cart = findCartById(id);
        // Sắp xếp danh sách CartBook theo id của sách
        cart.getCartBooks().sort(Comparator.comparing(cartBook -> cartBook.getBook().getId()));
        return cartMapper.toDTOWithBooks(cart);

    }

    public CartResponseDTO getCartByUsername(String username) {
        Cart cart = cartRepository.findCartByUsername(username);
        // Sắp xếp danh sách CartBook theo id của sách
        cart.getCartBooks().sort(Comparator.comparing(cartBook -> cartBook.getBook().getId()));
        return cartMapper.toDTOWithBooks(cart);
    }

    @Transactional
    public CartResponseDTO updateBooksByIds(String id, CartRequestDTO cartRequestDTO) {
        Cart cartDB = findCartById(id);
        Map<String, Integer> bookQuantities = getBookQuantities(cartRequestDTO);
        List<Book> books = bookMapper.toListEntity(cartRequestDTO.getBookIds(), bookRepository);

        updateCartBooks(cartDB, books, bookQuantities);
        cartDB.getCartBooks().sort(Comparator.comparing(cartBook -> cartBook.getBook().getId()));
        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));
    }

    @Transactional
    public CartResponseDTO cleanCart(String id) {
        Cart cart = findCartById(id);

        // Remove CartBook references
        if (cart.getCartBooks() != null) {
            for (CartBook cartBook : cart.getCartBooks()) {
                Book book = cartBook.getBook();
                book.getCartBooks().remove(cartBook);
            }
            cart.getCartBooks().clear();
        }

        return cartMapper.toDTO(cartRepository.save(cart));
    }


    //- Helper
    @Transactional
    public CartResponseDTO deleteCart(String id) {
        Cart cart = findCartById(id);

        if (cart.getCartBooks() != null) {
            // Remove CartBook references
            for (CartBook cartBook : cart.getCartBooks()) {
                Book book = cartBook.getBook();
                book.getCartBooks().remove(cartBook);
            }
            cart.getCartBooks().clear();
        }

        cart.setUser(null);

        cartRepository.delete(cart);
        return cartMapper.toDTO(cart);
    }

    private Map<String, Integer> getBookQuantities(CartRequestDTO cartRequestDTO) {
        List<String> bookIds = cartRequestDTO.getBookIds();
        List<String> quantities = cartRequestDTO.getQuantities();

        if (bookIds == null || quantities == null || bookIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Book IDs and quantities must be non-null and of the same size.");
        }

        Map<String, Integer> bookQuantities = new HashMap<>();
        for (int i = 0; i < bookIds.size(); i++) {
            bookQuantities.put(bookIds.get(i), Integer.parseInt(quantities.get(i)));
        }

        return bookQuantities;
    }

    private void updateCartBooks(Cart cartDB, List<Book> books, Map<String, Integer> bookQuantities) {
        for (Book book : books) {
            int quantity = bookQuantities.get(String.valueOf(book.getId()));

            CartBookId cartBookId = new CartBookId(cartDB.getId(), book.getId());
            CartBook cartBook = cartDB.getCartBooks().stream()
                    .filter(cb -> cb.getId().equals(cartBookId))
                    .findFirst()
                    .orElse(null);

            if (cartBook == null) {
                handleNewCartBook(cartDB, book, quantity);
            } else {
                handleExistingCartBook(cartDB, book, cartBook, quantity);
            }
        }
    }

    private void handleNewCartBook(Cart cartDB, Book book, int quantity) {
        if (quantity > book.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for book: " + book.getName());
        }
        if (quantity > 0) {
            CartBookId cartBookId = new CartBookId(cartDB.getId(), book.getId());
            CartBook cartBook = new CartBook(cartBookId, cartDB, book, quantity);
            entityManager.persist(cartBook);
            cartDB.getCartBooks().add(cartBook);
            book.getCartBooks().add(cartBook);
        }
    }

    private void handleExistingCartBook(Cart cartDB, Book book, CartBook cartBook, int quantity) {
        int newQuantity = cartBook.getQuantity() + quantity;
        if (newQuantity > book.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for book: " + book.getName());
        }
        if (newQuantity > 0) {
            cartBook.setQuantity(newQuantity);
            entityManager.merge(cartBook);
        } else {
            cartDB.getCartBooks().remove(cartBook);
            book.getCartBooks().remove(cartBook);
            entityManager.remove(cartBook);
        }

    }
}
