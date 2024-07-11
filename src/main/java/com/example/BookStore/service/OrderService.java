package com.example.BookStore.service;

import com.example.BookStore.DTO.OrderRequestDTO;
import com.example.BookStore.DTO.OrderResponseDTO;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.mapstruct.OrderMapper;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Order;
import com.example.BookStore.model.OrderBook;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.BookRepository;
import com.example.BookStore.repository.OrderRepository;
import com.example.BookStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDTOWithBooks).toList();
    }

    public OrderResponseDTO getOrderById(String id) {
        return orderMapper.toDTOWithBooks(findOrderById(id));
    }

    @Transactional
    public OrderResponseDTO deleteOrder(String id) {
        Order order = findOrderById(id);

        if (order.getOrderBooks() != null) {
            // Remove OrderBook references
            for (OrderBook orderBook : order.getOrderBooks()) {
                Book book = orderBook.getBook();
                book.getOrderBooks().remove(orderBook);
            }
            order.getOrderBooks().clear();
        }


        if (order.getUser() != null) {
            order.getUser().getOrders().remove(order);
            order.setUser(null);
        }

        orderRepository.delete(order);
        return orderMapper.toDTO(order);
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderDTO) {
        Order order = orderMapper.toEntityWithBooksAndUser(orderDTO, bookRepository, userRepository);
        order.setId(null);
        return orderMapper.toDTOWithBooks(orderRepository.save(order));
    }

//    @Transactional
//    public OrderResponseDTO updateOrder(String id, OrderRequestDTO orderDTO) {
//        Order orderDB = findOrderById(id);
//
//        orderDB.setDateUpdate(LocalDateTime.now());
//
//        //- Chỉ update 2 fields
//        if (orderDTO.g() != null) {
//            orderDB.setName(orderDTO.getName());
//        }
//
//        if (orderDTO.getPhone() != null) {
//            orderDB.setPhone(orderDTO.getPhone());
//        }
//
//
//        return orderMapper.toDTOWithBooks(orderRepository.save(orderDB));
//    }


}


