package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.DTO.OrderDTO;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Order;
import com.example.BookStore.model.Response;
import com.example.BookStore.model.User;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    BookMapper bookMapper = BookMapper.INSTANCE;

    @Mapping(source = "books", target = "books", ignore = true)
    @Mapping(source = "user", target = "user", ignore = true)
    OrderDTO toDTO(Order order);

    @Mapping(target = "user", source = "user", ignore = true)
    @Mapping(source = "books", target = "books", ignore = true)
    Order toEntity(OrderDTO dto);

    //- Convert Order -> OrderDTO
    //! Dung khi RESPONSE
    default OrderDTO toDTOWithBooks(Order order) {
        OrderDTO orderDTO = toDTO(order);

        List<BookDTO> bookDTOs = bookMapper.toListDTO(order.getBooks());
        orderDTO.setBooks(bookDTOs);

        return orderDTO;
    }

    default List<OrderDTO> toListDTOWithBooks(List<Order> orders) {
        return orders != null
                ? orders.stream().map(this::toDTOWithBooks).toList()
                : Collections.emptyList();
    }

    //- Convert OrderDTO -> Order
    //! Dùng khi REQUEST
    default Order toEntityWithBooks(OrderDTO orderDTO, BookRepository bookRepository) {
        Order order = toEntity(orderDTO);

        List<Book> books = bookMapper.toListEntity(orderDTO.getBooks(), bookRepository);
        for (Book book : books) {
            if (!book.getOrders().contains(order)) {
                book.getOrders().add(order);
            }
        }
        order.setBooks(books);

        return order;
    }

    default Order toEntityWithBooksAndUser(OrderDTO orderDTO, BookRepository bookRepository, UserRepository userRepository) {
        Order order = toEntityWithBooks(orderDTO, bookRepository);

        User user = userRepository.findById(String.valueOf(orderDTO.getUser()))
                .orElseThrow(() -> new RuntimeException(Response.notFound("User", String.valueOf(orderDTO.getUser()))));

        user.getOrders().add(order);
        order.setUser(user);

        if (order.getName() == null) {
            order.setName(user.getName());
        }

        if (order.getAddress() == null) {
            order.setAddress(user.getAddress());
        }

        if (order.getPhone() == null) {
            order.setPhone(user.getPhone());
        }
        return order;
    }
}
