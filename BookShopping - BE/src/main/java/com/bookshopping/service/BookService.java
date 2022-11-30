package com.bookshopping.service;

import com.bookshopping.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Book save(Book book);

    int updateAmount(Integer bookId, int amount);
    Book findById(Integer id);
    List<Book> findAll();
    List<Book> findBookByNumberRecord(int numberRecord);
    List<Book> findBooksByAuthor(String author);
    List<Book> findBookByNumberRecordSameAuthor(String author, int numberRecord);
    List<Book> findBookByNumberRecordSameCategory(String category, int numberRecord);
    Page<Book> search(String book, Pageable page);
}
