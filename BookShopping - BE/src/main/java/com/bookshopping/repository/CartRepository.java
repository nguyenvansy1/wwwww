package com.bookshopping.repository;

import com.bookshopping.model.Book;
import com.bookshopping.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Transactional
    @Modifying
    Cart save(Cart cart);

    @Transactional
    @Modifying
    @Query(value = "update cart_item set status = 1 where id=:id", nativeQuery = true)
    void updateStatus(@Param("id") Long id);


}
