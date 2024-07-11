package com.example.BookStore.controller;

import com.example.BookStore.DTO.OrderRequestDTO;
import com.example.BookStore.DTO.OrderResponseDTO;
import com.example.BookStore.model.Response;
import com.example.BookStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            OrderResponseDTO orderDTO = orderService.getOrderById(id);
            return ResponseEntity.ok(orderDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderDTO) {
        try {
            OrderResponseDTO savedOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        try {
            OrderResponseDTO orderDTO = orderService.deleteOrder(id);
            return ResponseEntity.ok(orderDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody OrderDTO orderDTO) {
//        try {
//            OrderDTO savedOrder = orderService.updateOrder(id, orderDTO);
//            return ResponseEntity.ok(savedOrder);
//        } catch (Exception ex) {
//            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }


}
