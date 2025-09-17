package com.manjappa.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {
    private final Long id;
    private final String name;
    private final BigDecimal price;
}