package com.manjappa.store.mapper;

import com.manjappa.store.dtos.OrderDto;
import com.manjappa.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);
}
