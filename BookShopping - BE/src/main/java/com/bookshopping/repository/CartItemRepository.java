package com.bookshopping.repository;


import com.bookshopping.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query(value = "select * from cart_item where cart_id = :cartId and book_id = :bookId", nativeQuery = true)
    CartItem findByCartIdAndBookId(Integer cartId, Integer bookId);
    @Query(value = "select * from cart_item join cart on cart_item.cart_id = cart.id " +
            "where cart.user_id = :userId and cart_item.amount > 0 and cart_item.status = 0", nativeQuery = true)
    List<CartItem> findCartItemsByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query(value = "insert into cart_item set amount = :amount, cart_id = :cartId, book_id = :bookId, status = 0", nativeQuery = true)
    int save(int amount, Integer cartId, Integer bookId);

    @Transactional
    @Modifying
    @Query(value = "update cart_item set amount = :amount where id = :id", nativeQuery = true)
    int update(Integer id, int amount);
}
