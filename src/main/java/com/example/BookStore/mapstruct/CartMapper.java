package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.DTO.CartDTO;
import com.example.BookStore.model.*;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
    BookMapper bookMapper = BookMapper.INSTANCE;

    @Mapping(source = "user", target = "user", ignore = true)
    @Mapping(source = "cartBooks", target = "books", ignore = true)
    CartDTO toDTO(Cart cart);

    @Mapping(source = "user", target = "user", ignore = true)
    @Mapping(source = "books", target = "cartBooks", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    //- Convert Order -> OrderDTO
    //! Dung khi RESPONSE
    default CartDTO toDTOWithBooks(Cart cart) {
        CartDTO cartDTO = toDTO(cart);

        List<BookDTO> bookDTOs = cart.getCartBooks().stream()
                .map(cartBook -> bookMapper.toDTO(cartBook.getBook()))
                .toList();
        cartDTO.setBooks(bookDTOs);

        return cartDTO;
    }

    //- Convert OrderDTO -> Order
    //! Dùng khi REQUEST
    default Cart toEntityWithBooks(CartDTO cartDTO, BookRepository bookRepository) {
        Cart cart = toEntity(cartDTO);

        List<Book> books = bookMapper.toListEntity(cartDTO.getBooks(), bookRepository);

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            int quantity = cartDTO.getQuantities().get(i);

            CartBookId cartBookId = new CartBookId(cart.getId(), book.getId());
            CartBook cartBook = new CartBook(cartBookId, cart, book, quantity);


            if (book.getCartBooks() == null) {
                book.setCartBooks(new ArrayList<>());
            }
            book.getCartBooks().add(cartBook);

            if (cart.getCartBooks() == null) {
                cart.setCartBooks(new ArrayList<>());
            }
            cart.getCartBooks().add(cartBook);
        }

        return cart;
    }

    default Cart toEntityWithBooksAndUser(CartDTO cartDTO, BookRepository bookRepository, UserRepository userRepository) {
        Cart cart = toEntityWithBooks(cartDTO, bookRepository);

        User user = userRepository.findById(String.valueOf(cartDTO.getUser()))
                .orElseThrow(() -> new RuntimeException(Response.notFound("User", String.valueOf(cartDTO.getUser()))));

        user.setCart(cart);
        cart.setUser(user);
        return cart;
    }
}
