package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.OrderBookDTO;
import com.example.BookStore.DTO.OrderRequestDTO;
import com.example.BookStore.DTO.OrderResponseDTO;
import com.example.BookStore.model.*;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    BookMapper bookMapper = BookMapper.INSTANCE;

    @Mapping(source = "orderBooks", target = "books", ignore = true)
    OrderResponseDTO toDTO(Order order);

    @Mapping(source = "userId", target = "user", ignore = true)
    @Mapping(source = "bookIds", target = "orderBooks", ignore = true)
    Order toEntity(OrderRequestDTO orderRequestDTO);

    //- Convert Order -> OrderDTO
    //! Dung khi RESPONSE
    default OrderResponseDTO toDTOWithBooks(Order order) {
        OrderResponseDTO orderDTO = toDTO(order);

        List<OrderBookDTO> orderBookDTOList = order.getOrderBooks().stream()
                .map(orderBook -> new OrderBookDTO(bookMapper.toDTO(orderBook.getBook()), orderBook.getQuantity())).toList();

        orderDTO.setBooks(orderBookDTOList);

        return orderDTO;
    }

    default List<OrderResponseDTO> toListDTOWithBooks(List<Order> orders) {
        return orders != null
                ? orders.stream().map(this::toDTOWithBooks).collect(Collectors.toList())
                : Collections.emptyList();
    }

    //- Convert OrderDTO -> Order
    //! Dùng khi REQUEST
    default Order toEntityWithBooks(OrderRequestDTO orderDTO, BookRepository bookRepository) {
        Order order = toEntity(orderDTO);

        List<Book> books = bookMapper.toListEntity(orderDTO.getBookIds(), bookRepository);

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            int quantity = Integer.parseInt(orderDTO.getQuantities().get(i));

            OrderBookId orderBookId = new OrderBookId(order.getId(), book.getId());
            OrderBook orderBook = new OrderBook(orderBookId, order, book, quantity);

            if (book.getOrderBooks() == null) {
                book.setOrderBooks(new ArrayList<>());
            }
            book.getOrderBooks().add(orderBook);

            if (order.getOrderBooks() == null) {
                order.setOrderBooks(new ArrayList<>());
            }
            order.getOrderBooks().add(orderBook);
        }
        return order;
    }

    default Order toEntityWithBooksAndUser(OrderRequestDTO orderDTO, BookRepository bookRepository, UserRepository userRepository) {
        Order order = toEntityWithBooks(orderDTO, bookRepository);

        User user = userRepository.findById(String.valueOf(orderDTO.getUserId()))
                .orElseThrow(() -> new RuntimeException(Response.notFound("User", String.valueOf(orderDTO.getUserId()))));

        user.getOrders().add(order);
        order.setUser(user);

        //- Set các field
        order.setDateCreate(LocalDateTime.now());
        order.setDateUpdate(LocalDateTime.now());
        order.setName(user.getName());
        order.setAddress(user.getAddress());
        order.setPhone(user.getPhone());
        return order;
    }
}
