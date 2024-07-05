package com.example.BookStore.service;

import com.example.BookStore.DTO.OrderDTO;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.mapstruct.OrderMapper;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Order;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Autowired
    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    private Order findOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Response.notFound("Order", id)));
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDTOWithBooks).toList();
    }

    public OrderDTO getOrderById(String id) {
        return orderMapper.toDTOWithBooks(findOrderById(id));
    }

    @Transactional
    public OrderDTO deleteOrder(String id) {
        Order order = findOrderById(id);

        for (Book book : order.getBooks()) {
            book.getOrders().remove(order);
        }
        order.getBooks().clear();

        order.getUser().getOrders().remove(order);
        order.setUser(null);

        orderRepository.delete(order);
        return orderMapper.toDTO(order);
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTO.setDateCreate(LocalDateTime.now());
        orderDTO.setDateUpdate(LocalDateTime.now());

        List<String> bookIdsAsString = orderDTO.getBooks().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        orderDTO.setBooks(bookIdsAsString);


        Order order = orderMapper.toEntityWithBooksAndUser(orderDTO, bookRepository, userRepository);
        return orderMapper.toDTOWithBooks(orderRepository.save(order));
    }

    @Transactional
    public OrderDTO updateOrder(String id, OrderDTO orderDTO) {
        Order orderDB = findOrderById(id);

        orderDB.setDateUpdate(LocalDateTime.now());

        //- Chỉ update 2 fields
        if (orderDTO.getName() != null) {
            orderDB.setName(orderDTO.getName());
        }

        if (orderDTO.getPhone() != null) {
            orderDB.setPhone(orderDTO.getPhone());
        }


        return orderMapper.toDTOWithBooks(orderRepository.save(orderDB));
    }


}


