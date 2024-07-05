package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.DTO.CartDTO;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Cart;
import com.example.BookStore.model.Response;
import com.example.BookStore.model.User;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
    BookMapper bookMapper = BookMapper.INSTANCE;

    @Mapping(target = "user", source = "user", ignore = true)
    @Mapping(source = "books", target = "books", ignore = true)
    CartDTO toDTO(Cart cart);

    @Mapping(target = "user", source = "user", ignore = true)
    @Mapping(source = "books", target = "books", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    //- Convert Order -> OrderDTO
    //! Dung khi RESPONSE
    default CartDTO toDTOWithBooks(Cart cart) {
        CartDTO cartDTO = toDTO(cart);

        List<BookDTO> bookDTOs = bookMapper.toListDTO(cart.getBooks());
        cartDTO.setBooks(bookDTOs);

        return cartDTO;
    }

    //- Convert OrderDTO -> Order
    //! Dùng khi REQUEST
    default Cart toEntityWithBooks(CartDTO cartDTO, BookRepository bookRepository) {
        Cart cart = toEntity(cartDTO);

        List<Book> books = bookMapper.toListEntity(cartDTO.getBooks(), bookRepository);
        for (Book book : books) {
            if (!book.getCarts().contains(cart)) {
                book.getCarts().add(cart);
            }
        }
        cart.setBooks(books);

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
