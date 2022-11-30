package com.bookshopping.controller;

import com.bookshopping.model.Book;
import com.bookshopping.model.CartItem;
import com.bookshopping.payload.request.CartItemRequest;
import com.bookshopping.payload.response.ResponseMessage;
import com.bookshopping.repository.CartRepository;
import com.bookshopping.service.BookService;
import com.bookshopping.service.CartItemService;
import com.bookshopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/getCartByUserId")
    public ResponseEntity<List<CartItem>> getCartByUserId(@RequestParam Integer id) {
        return new ResponseEntity<>(cartItemService.findAllCartItemsByUser(id), HttpStatus.OK);
    };

    @GetMapping("/addToCart")
    public ResponseEntity<ResponseMessage> addToCart(@RequestParam int amount,
                                                     @RequestParam Integer cartId,
                                                     @RequestParam Integer bookId) {
        Book book = bookService.findById(bookId);

        int record;
        CartItem cartItem = cartItemService.findByCartIdAndBookId(cartId, bookId);
        if(cartItem == null) {
            if(book.getAmount() < amount){
                return new ResponseEntity<>(new ResponseMessage("Sản phẩm tối đa còn lại trong hệ thống là "
                        + book.getAmount()), HttpStatus.BAD_REQUEST);
            }
            record = cartItemService.save(amount, cartId, bookId);
        } else {
            if(book.getAmount() < amount + cartItem.getAmount()){
                return new ResponseEntity<>(new ResponseMessage("Sản phẩm tối đa còn lại trong hệ thống là "
                        + book.getAmount()), HttpStatus.BAD_REQUEST);
            }
            record = cartItemService.update(cartItem.getId(), amount + cartItem.getAmount());
        }
        if(record <= 0) {
            return new ResponseEntity<>(new ResponseMessage("Lỗi không thêm được sản phẩm vào giỏ hàng"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Thêm vào giỏ hàng thành công !!!"), HttpStatus.OK);
    };

    @GetMapping("/updateCartItem")
    public ResponseEntity<ResponseMessage> create(@RequestParam int amount,
                                                  @RequestParam Integer cartItemId,
                                                  @RequestParam Integer bookId) {
        Book book = bookService.findById(bookId);

        if(book.getAmount() < amount){
            return new ResponseEntity<>(new ResponseMessage("Sản phẩm tối đa còn lại trong hệ thống là "
                    + book.getAmount()), HttpStatus.BAD_REQUEST);
        }
        int record = 0;
        CartItem cartItem = cartItemService.findById(cartItemId);
        if(cartItem == null) {
            return new ResponseEntity<>(new ResponseMessage("Không tìm thấy giỏ hàng !!!"), HttpStatus.BAD_REQUEST);
        } else {
            record = cartItemService.update(cartItemId, amount);
        }
        if(record <= 0) {
            return new ResponseEntity<>(new ResponseMessage("Lỗi không thêm được sản phẩm vào giỏ hàng"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Thêm vào giỏ hàng thành công !!!"), HttpStatus.OK);
    };

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> delete(@RequestParam Integer cartItemId) {
        int record = 0;
        CartItem cartItem = cartItemService.findById(cartItemId);
        if(cartItem == null) {
            return new ResponseEntity<>(new ResponseMessage("Không tìm thấy giỏ hàng !!!"), HttpStatus.BAD_REQUEST);
        } else {
            record = cartItemService.update(cartItemId, 0);
        }
        if(record <= 0) {
            return new ResponseEntity<>(new ResponseMessage("Lỗi không thêm được sản phẩm vào giỏ hàng"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Thêm vào giỏ hàng thành công !!!"), HttpStatus.OK);
    };

    @PostMapping("/synchronizedCart")
    public ResponseEntity<?> synchronizedCart(@RequestParam Integer cartId, @RequestBody List<CartItemRequest> cartItemRequests) {
        System.out.println("synchronizedCart");
        Book book;
        CartItem cartItem;
        for (CartItemRequest cartItemRequest : cartItemRequests) {
            book = bookService.findById(cartItemRequest.getBookId());
            cartItem = cartItemService.findByCartIdAndBookId(cartId, cartItemRequest.getBookId());
            if(cartItem == null) {
                if(book.getAmount() >= cartItemRequest.getAmount()) {
                    cartItemService.save(cartItemRequest.getAmount(), cartId, cartItemRequest.getBookId());
                }
            } else {
                if(book.getAmount() >= cartItemRequest.getAmount() + cartItem.getAmount()) {
                    cartItemService.update(cartItem.getId(), cartItem.getAmount() + cartItemRequest.getAmount());
                }
            }
        }
        return new ResponseEntity<>(new ResponseMessage("Đồng bộ giỏ hành thành công"), HttpStatus.OK);
    }


    @GetMapping("/insert/{cartId}")
    public ResponseEntity<?> insertMaterialToCart(@PathVariable("cartId") Long[] cartId) {
        try {
            for (int i=0; i<cartId.length;i++){
                cartRepository.updateStatus(cartId[i]);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
