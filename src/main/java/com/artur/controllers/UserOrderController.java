package com.artur.controllers;

import com.artur.exception.ForbiddenException;
import com.artur.repo.specification.OrderSpecification;
import com.artur.security.UserPrincipal;
import com.artur.service.UserOrderService;
import com.artur.service.dto.UserOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserOrderController {
    private final UserOrderService userOrderService;

    public UserOrderController(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    @PostMapping("/users/{id}/orders")
    public ResponseEntity<UserOrderDto> crateOrderByUserId(@PathVariable final Long id, @RequestBody @Valid UserOrderDto userOrderDto, final Authentication auth){
        var principal = (UserPrincipal)auth.getPrincipal();
        if (!principal.getId().equals(id)){
            throw new ForbiddenException();
        }
        return ResponseEntity.ok().body(userOrderService.crateOrderByUserId(id, userOrderDto));
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<UserOrderDto>> getAllOrders(OrderSpecification orderSpecification, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(userOrderService.getAllOrders(orderSpecification, pageable));
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<UserOrderDto>> getAllOrdersByUserId(@PathVariable final Long userId, OrderSpecification orderSpecification, @PageableDefault Pageable pageable, final Authentication auth){
//        var principal = (UserPrincipal)auth.getPrincipal();
//        if (!principal.getId().equals(userId)){
//            throw new ForbiddenException();
//        }
        return ResponseEntity.ok().body(userOrderService.getAllOrdersByUserId(userId, orderSpecification, pageable));
    }

    @GetMapping("/users/{userId}/active_order")
    public ResponseEntity<UserOrderDto> getActiveOrderForUser(@PathVariable final Long userId, final Authentication auth, @PageableDefault Pageable pageable){
//        var principal = (UserPrincipal)auth.getPrincipal();
//        if (!principal.getId().equals(userId)){
//            throw new ForbiddenException();
//        }
        return ResponseEntity.ok().body(userOrderService.getActiveOrderForUser(userId));
    }

}
