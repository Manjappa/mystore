package com.manjappa.store.mapper;

import com.manjappa.store.dtos.CartDto;
import com.manjappa.store.dtos.CartItemDto;
import com.manjappa.store.entities.Cart;
import com.manjappa.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto  toCartDto(Cart cart);

    @Mapping(target ="totalPrice",  expression="java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);
}
