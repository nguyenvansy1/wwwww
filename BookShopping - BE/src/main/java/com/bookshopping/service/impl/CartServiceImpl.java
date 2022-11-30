package com.bookshopping.service.impl;

import com.bookshopping.model.Cart;
import com.bookshopping.repository.CartRepository;
import com.bookshopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

}
