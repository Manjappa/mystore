package com.manjappa.store.exceptions;

public class CartNotFoundException extends RuntimeException{
    public  CartNotFoundException() {
        super("Cart is empty");
    }
}
