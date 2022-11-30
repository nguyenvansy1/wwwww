package com.bookshopping.controller;

import com.bookshopping.model.Book;
import com.bookshopping.payload.response.ResponseMessage;
import com.bookshopping.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findByNumberRecord")
    public ResponseEntity<List<Book>> getBooks(@RequestParam int numberRecord) {
        return new ResponseEntity<>(bookService.findBookByNumberRecord(numberRecord), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createBook(@RequestBody Book book) {
        System.out.println("Create new book");
        Book bookCreate = bookService.save(book);
        if(bookCreate == null)
            return new ResponseEntity<>(new ResponseMessage("Thêm sách thất bại."), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseMessage("Thêm mới sách thành công !!!"), HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Book> findById(@RequestParam Integer id) {
        System.out.println("Find by id");
        Book book = bookService.findById(id);
        System.out.println(book.getId());
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

//    @GetMapping("/findBooksSameCategory")
//    public ResponseEntity<List<Book>> findBooksSameCategory(@RequestParam String category) {
//        System.out.println("Find by category");
//        return new ResponseEntity<>(bookService.findBooksByCategory(category), HttpStatus.OK);
//    }
//
    @GetMapping("/findBooksSameAuthor")
    public ResponseEntity<List<Book>> findBooksSameAuthor(@RequestParam String author) {
        System.out.println("Find by author");
        return new ResponseEntity<>(bookService.findBooksByAuthor(author), HttpStatus.OK);
    }

    @GetMapping("/findBooksSameCategoryLimit")
    public ResponseEntity<List<Book>> findBooksSameCategory(@RequestParam String category,
                                                            @RequestParam(defaultValue = "20") int numberRecord) {
        System.out.println("Find by category");
        return new ResponseEntity<>(bookService.findBookByNumberRecordSameCategory(category, numberRecord), HttpStatus.OK);
    }

    @GetMapping("/findBooksSameAuthorLimit")
    public ResponseEntity<List<Book>> findBooksSameAuthor(@RequestParam String author,
                                                          @RequestParam(defaultValue = "20") int numberRecord) {
        System.out.println("Find by author");
        return new ResponseEntity<>(bookService.findBookByNumberRecordSameAuthor(author, numberRecord), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Book>> search(@RequestParam String q, @PageableDefault(size = 48) Pageable pageable) {
        System.out.println("Search value = " + q);
        return new ResponseEntity<>(bookService.search(q, pageable), HttpStatus.OK);
    }
}
