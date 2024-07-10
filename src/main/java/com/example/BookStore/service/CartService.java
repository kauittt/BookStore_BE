package com.example.BookStore.service;

import com.example.BookStore.DTO.CartDTO;
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

import java.util.List;
import java.util.stream.Collectors;

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

    private Cart findCartById(String id) {
        Cart cart = cartRepository.findCartByUserId(id);
        if(cart == null) {
            throw new RuntimeException(Response.notFound("Cart", id));
        }
        return cart;
    }

    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream().map(cartMapper::toDTOWithBooks).toList();
    }

    public CartDTO getCartById(String id) {
        return cartMapper.toDTOWithBooks(findCartById(id));
    }

    @Transactional
    public CartDTO updateBooksByIds(String id, CartDTO cartDTO) {
        Cart cartDB = findCartById(id);

        List<String> bookIdsAsString = cartDTO.getBooks().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        cartDTO.setBooks(bookIdsAsString);

        List<Book> books = bookMapper.toListEntity(cartDTO.getBooks(), bookRepository);

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            int quantity = cartDTO.getQuantities().get(i);

            CartBookId cartBookId = new CartBookId(cartDB.getId(), book.getId());
            CartBook cartBook = cartDB.getCartBooks().stream()
                    .filter(cb -> cb.getId().equals(cartBookId))
                    .findFirst()
                    .orElse(null);

            if (cartBook == null) {
                // If the cartBook doesn't exist and the quantity is positive, create a new CartBook
                if (quantity > 0) {
                    cartBook = new CartBook(cartBookId, cartDB, book, quantity);
                    cartBook = entityManager.merge(cartBook);
                    cartDB.getCartBooks().add(cartBook);
                    book.getCartBooks().add(cartBook);
                }
            } else {
                // Update the quantity if CartBook already exists
                int newQuantity = cartBook.getQuantity() + quantity;
                if (newQuantity > 0) {
                    cartBook.setQuantity(newQuantity);
                    entityManager.merge(cartBook);
                } else if (newQuantity == 0) {
                    // Remove the cartBook if the new quantity is zero
                    cartDB.getCartBooks().remove(cartBook);
                    book.getCartBooks().remove(cartBook);
                    entityManager.remove(entityManager.contains(cartBook) ? cartBook : entityManager.merge(cartBook));
                } else {
                    // Handle the case where newQuantity would be negative
                    throw new IllegalArgumentException("Resulting quantity cannot be negative.");
                }
            }
        }

        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));
    }


//    @Transactional
//    public CartDTO removeBooksByIds(String id, CartDTO cartDTO) {
//        Cart cartDB = findCartById(id);
//
//        List<String> bookIdsAsString = cartDTO.getBooks().stream()
//                .map(String::valueOf)
//                .collect(Collectors.toList());
//        cartDTO.setBooks(bookIdsAsString);
//
//        List<Book> books = bookMapper.toListEntity(cartDTO.getBooks(), bookRepository);
//        for (Book book : books) {
//            book.getCarts().remove(cartDB);
//        }
//        cartDB.getBooks().removeAll(books);
//
//
//        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));
//    }

    @Transactional
    public CartDTO cleanCart(String id) {
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

    @Transactional
    public CartDTO deleteCart(String id) {
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
}
