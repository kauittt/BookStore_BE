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

import java.time.LocalDateTime;
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

    public List<OrderResponseDTO> getOrdersByUserid(String id) {
        List<Order> orders = orderRepository.findOrdersByUserId(id);
        return orderMapper.toListDTOWithBooks(orders);
    }

    public List<OrderResponseDTO> getOrdersByUsername(String username) {
        List<Order> orders = orderRepository.findOrdersByUsername(username);
        return orderMapper.toListDTOWithBooks(orders);
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

    @Transactional
    public OrderResponseDTO updateOrder(String id, OrderResponseDTO orderDTO) {
        // Fetch the existing order from the database
        Order orderDB = findOrderById(id);

        // Update the date of update
        orderDB.setDateUpdate(LocalDateTime.now());

        // Only update the fields if they are provided in the DTO
        if (orderDTO.getName() != null) {
            orderDB.setName(orderDTO.getName());
        }
        if (orderDTO.getPhone() != null) {
            orderDB.setPhone(orderDTO.getPhone());
        }
        if (orderDTO.getAddress() != null) {
            orderDB.setAddress(orderDTO.getAddress());
        }

        // Save the updated order back to the repository and map it to DTO
        return orderMapper.toDTOWithBooks(orderRepository.save(orderDB));
    }


}


