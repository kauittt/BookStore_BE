package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.*;
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

    @Mapping(source = "cartBooks", target = "books", ignore = true)
    CartResponseDTO toDTO(Cart cart);

    @Mapping(source = "bookIds", target = "cartBooks", ignore = true)
    Cart toEntity(CartRequestDTO cartRequestDTO);

    //- Convert Order -> OrderDTO
    //! Dung khi RESPONSE
    default CartResponseDTO toDTOWithBooks(Cart cart) {
        CartResponseDTO cartResponseDTO = toDTO(cart);

        List<CartBookDTO> bookDTOs = cart.getCartBooks().stream().map(cartBook -> new CartBookDTO(bookMapper.toDTO(cartBook.getBook()), cartBook.getQuantity())).toList();

        cartResponseDTO.setBooks(bookDTOs);

        return cartResponseDTO;
    }

    //- Convert OrderDTO -> Order
    //! Dùng khi REQUEST
    default Cart toEntityWithBooks(CartRequestDTO cartRequestDTO, BookRepository bookRepository) {
        Cart cart = toEntity(cartRequestDTO);

        List<Book> books = bookMapper.toListEntity(cartRequestDTO.getBookIds(), bookRepository);

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            int quantity = Integer.parseInt(cartRequestDTO.getQuantities().get(i));

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

    default Cart toEntityWithBooksAndUser(CartRequestDTO cartRequestDTO, String userId, BookRepository bookRepository, UserRepository userRepository) {
        Cart cart = toEntityWithBooks(cartRequestDTO, bookRepository);

        User user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new RuntimeException(Response.notFound("User", userId)));

        user.setCart(cart);
        cart.setUser(user);
        return cart;
    }
}
