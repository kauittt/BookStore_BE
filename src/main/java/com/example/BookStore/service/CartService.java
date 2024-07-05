package com.example.BookStore.service;

import com.example.BookStore.DTO.CartDTO;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.mapstruct.CartMapper;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper = CartMapper.INSTANCE;
    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final BookRepository bookRepository;

    @Autowired
    public CartService(CartRepository cartRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    private Cart findCartById(String id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Response.notFound("Cart", id)));
    }

    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream().map(cartMapper::toDTOWithBooks).toList();
    }

    public CartDTO getCartById(String id) {
        return cartMapper.toDTOWithBooks(findCartById(id));
    }

    @Transactional
    public CartDTO addBooksByIds(String id, CartDTO cartDTO) {
        Cart cartDB = findCartById(id);

        List<Book> books = bookMapper.toListEntity(cartDB.getBooks(), bookRepository);
        for (Book book : books) {
            if (!book.getCarts().contains(cartDB)) {
                book.getCarts().add(cartDB);
            }
        }
        cartDB.getBooks().addAll(books);


        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));
    }

    @Transactional
    public CartDTO removeBooksByIds(String id, CartDTO cartDTO) {
        Cart cartDB = findCartById(id);

        List<Book> books = bookMapper.toListEntity(cartDB.getBooks(), bookRepository);
        for (Book book : books) {
            book.getCarts().remove(cartDB);
        }
        cartDB.getBooks().removeAll(books);


        return cartMapper.toDTOWithBooks(cartRepository.save(cartDB));
    }

    @Transactional
    public CartDTO cleanCart(String id) {
        Cart cart = findCartById(id);

        //- Remove Book's references
        for (Book book : cart.getBooks()) {
            book.getCarts().remove(cart);
        }
        cart.getBooks().clear();

        return cartMapper.toDTO(cartRepository.save(cart));
    }
}
