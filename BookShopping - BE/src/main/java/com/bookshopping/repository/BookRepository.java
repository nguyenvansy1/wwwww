package com.bookshopping.repository;


import com.bookshopping.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Transactional
    @Modifying
    @Query(value = "update book set amount = :amount where book.id = :bookId", nativeQuery = true)
    int updateAmount(Integer bookId, int amount);

    @Query(value = "select * from book limit :numberRecord", nativeQuery = true)
    List<Book> findBookByNumberRecord(int numberRecord);


    @Query(value = "select * from book b where b.author = :author  limit :numberRecord", nativeQuery = true)
    List<Book> findBookByNumberRecordSameAuthor(String author, int numberRecord);

    @Query(value = "select b.* from book b " +
            "join category c on b.category_id = c.id " +
            "where c.name = :category  limit :numberRecord", nativeQuery = true)
    List<Book> findBookByNumberRecordSameCategory(String category, int numberRecord);

    List<Book> findBooksByAuthor(String author);

    @Query(value = "select * from book join category on book.category_id = category.id " +
            "where book.name like %:search% or book.author like %:search% or category.name like %:search%",
            countQuery = "select count(*) from book join category on book.category_id = category.id " +
                    "where book.name like %:search% or book.author like %:search% or category.name like %:search%",
            nativeQuery = true)
    Page<Book> search(String search, Pageable page);
}
