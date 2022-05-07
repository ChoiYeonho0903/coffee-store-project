package com.example.coffeestore.order.controller.api;

import com.example.coffeestore.order.controller.dto.CreateOrderRequestDto;
import com.example.coffeestore.order.domain.OrderItem;
import com.example.coffeestore.order.service.OrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class OrderRestController {

    private final OrderService orderService;

//    @GetMapping("/orders")
//    public OrderResponseDto orderList(@RequestParam UUID orderId) {
//
//    }

    @PostMapping("/orders")
    public Object addOrder(@RequestBody @Validated CreateOrderRequestDto orderRequestDto, BindingResult bindingResult) {
        long totalPrice = 0L;
        for (OrderItem orderItem : orderRequestDto.getOrderItems()) {
            totalPrice += orderItem.getPrice();
        }
        if (totalPrice < 10000) {
            bindingResult.addError(new ObjectError("order", "주문 총액은 10,000원 이상이어야 한다. 현재 값 = " + totalPrice));
        }

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 error={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        return orderService.createOrder(orderRequestDto);
    }
}
