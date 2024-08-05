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
        if(cart == null) {
            throw new RuntimeException(Response.notFound("Cart", id));
        }
        return cart;
    }

    public List<CartResponseDTO> getAllCarts() {
        return cartRepository.findAll().stream().map(cartMapper::toDTOWithBooks).toList();
    }

    public CartResponseDTO getCartById(String id) {
        return cartMapper.toDTOWithBooks(findCartById(id));
    }

    public CartResponseDTO getCartByUsername(String username) {
        return cartMapper.toDTOWithBooks(cartRepository.findCartByUsername(username));
    }

    //! Cần tìm hiểu
    @Transactional
    public CartResponseDTO updateBooksByIds(String id, CartRequestDTO cartRequestDTO) {
        Cart cartDB = findCartById(id);

        List<Book> books = bookMapper.toListEntity(cartRequestDTO.getBookIds(), bookRepository);

        // Create a map for book quantities
        Map<String, Integer> bookQuantities = new HashMap<>();
        for (int i = 0; i < cartRequestDTO.getBookIds().size(); i++) {
            bookQuantities.put(cartRequestDTO.getBookIds().get(i), Integer.parseInt(cartRequestDTO.getQuantities().get(i)));
        }

        for (Book book : books) {
            int quantity = bookQuantities.get(String.valueOf(book.getId()));

            // Kiểm tra số lượng sản phẩm trong kho
            if (book.getQuantity() < quantity) {
                throw new IllegalArgumentException("Not enough stock for book: " + book.getName());
            }

            CartBookId cartBookId = new CartBookId(cartDB.getId(), book.getId());
            CartBook cartBook = cartDB.getCartBooks().stream()
                    .filter(cb -> cb.getId().equals(cartBookId))
                    .findFirst()
                    .orElse(null);

            if (cartBook == null && quantity > 0) {
                // Create a new CartBook if it doesn't exist and quantity is positive
                cartBook = new CartBook(cartBookId, cartDB, book, quantity);
                entityManager.persist(cartBook);
                cartDB.getCartBooks().add(cartBook);
                book.getCartBooks().add(cartBook);
            } else if (cartBook != null) {
                int newQuantity = cartBook.getQuantity() + quantity;
                if (newQuantity > 0) {
                    // Update quantity if newQuantity is positive
                    cartBook.setQuantity(newQuantity);
                    entityManager.merge(cartBook);
                } else if (newQuantity == 0) {
                    // Remove CartBook if newQuantity is zero
                    cartDB.getCartBooks().remove(cartBook);
                    book.getCartBooks().remove(cartBook);
                    entityManager.remove(cartBook);
                } else {
                    // Handle negative quantity case
                    throw new IllegalArgumentException("Resulting quantity cannot be negative.");
                }
            }
        }

        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));

//        for (int i = 0; i < books.size(); i++) {
//            Book book = books.get(i);
//            int quantity = Integer.parseInt(cartRequestDTO.getQuantities().get(i));
//
//            CartBookId cartBookId = new CartBookId(cartDB.getId(), book.getId());
//            CartBook cartBook = cartDB.getCartBooks().stream()
//                    .filter(cb -> cb.getId().equals(cartBookId))
//                    .findFirst()
//                    .orElse(null);
//
//            if (cartBook == null) {
//                // If the cartBook doesn't exist and the quantity is positive, create a new CartBook
//                if (quantity > 0) {
//                    cartBook = new CartBook(cartBookId, cartDB, book, quantity);
//                    cartBook = entityManager.merge(cartBook);
//                    cartDB.getCartBooks().add(cartBook);
//                    book.getCartBooks().add(cartBook);
//                }
//            } else {
//                // Update the quantity if CartBook already exists
//                int newQuantity = cartBook.getQuantity() + quantity;
//                if (newQuantity > 0) {
//                    cartBook.setQuantity(newQuantity);
//                    entityManager.merge(cartBook);
//                } else if (newQuantity == 0) {
//                    // Remove the cartBook if the new quantity is zero
//                    cartDB.getCartBooks().remove(cartBook);
//                    book.getCartBooks().remove(cartBook);
//                    entityManager.remove(entityManager.contains(cartBook) ? cartBook : entityManager.merge(cartBook));
//                } else {
//                    // Handle the case where newQuantity would be negative
//                    throw new IllegalArgumentException("Resulting quantity cannot be negative.");
//                }
//            }
//        }
//
//        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));
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
}
