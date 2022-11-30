package com.bookshopping.service.impl;

import com.bookshopping.model.Book;
import com.bookshopping.repository.BookRepository;
import com.bookshopping.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int updateAmount(Integer bookId, int amount) {
        return bookRepository.updateAmount(bookId, amount);
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findBookByNumberRecord(int numberRecord) {
        return bookRepository.findBookByNumberRecord(numberRecord);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findBooksByAuthor(author);
    }

    @Override
    public List<Book> findBookByNumberRecordSameAuthor(String author, int numberRecord) {
        return bookRepository.findBookByNumberRecordSameAuthor(author, numberRecord);
    }

    @Override
    public List<Book> findBookByNumberRecordSameCategory(String category, int numberRecord) {
        return bookRepository.findBookByNumberRecordSameCategory(category, numberRecord);
    }

    @Override
    public Page<Book> search(String book, Pageable page) {
        return bookRepository.search(book, page);
    }
}
