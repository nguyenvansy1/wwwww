package com.bookshopping.service;

import com.bookshopping.model.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem findByCartIdAndBookId(Integer cartId, Integer bookId);
    int save(int amount, Integer cartId, Integer bookId);
    int update(Integer id, int amount);
    CartItem findById(Integer id);
    List<CartItem> findAllCartItemsByUser(Integer userId);
}
