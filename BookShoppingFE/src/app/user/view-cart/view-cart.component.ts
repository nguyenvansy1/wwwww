import {Component, ElementRef, OnInit} from '@angular/core';
import {CartService} from '../service/cart.service';
import {CartItem} from '../../models/cart-item';
import {TokenStorageService} from '../../services/token-storage.service';
import {Book} from '../../models/book';
import {ToastrService} from 'ngx-toastr';
import {CartStorageService} from '../../services/cart-storage.service';

@Component({
  selector: 'app-view-cart',
  templateUrl: './view-cart.component.html',
  styleUrls: ['./view-cart.component.css']
})
export class ViewCartComponent implements OnInit {
  cartItems: CartItem[] = [];
  idCartItemDelete = 0;
  book: Book;
  totalPrice = 0;
  quantityCheck = 0;
  listPayment: number[] = [];

  constructor(private cartService: CartService, private storageService: TokenStorageService,
              private el: ElementRef, private toastrService: ToastrService, private cartStorageService: CartStorageService) {
  }

  ngOnInit(): void {
    if (this.storageService.checkIsLogin()) {
      this.getCartByUserId();
    } else {
      this.cartItems = this.cartStorageService.getItems();
    }
  }

  getCartByUserId() {
    this.cartService.getCartItemByUserId(this.storageService.getUser().id).subscribe(
      items => this.cartItems = items
    );
  }

  openModal(id: number) {
    this.idCartItemDelete = id;
    this.book = this.cartItems.find(c => c.id === id).book;
    const modal = this.el.nativeElement.querySelector('.modal');
    modal.style.display = 'block';
  }

  hiddenModal() {
    const modal = this.el.nativeElement.querySelector('.modal');
    modal.style.display = 'none';
  }

  subQuantity(id: number, bookId: number) {
    const selector = '#cartItem' + id;
    const inputQuantity = this.el.nativeElement.querySelector(selector);

    if (this.storageService.checkIsLogin()) {
      if (inputQuantity.value > 1) {
        this.cartService.updateCartItem(Number(inputQuantity.value) - 1, id, bookId).subscribe(
          next => {
            this.cartItems.find(t => t.id === id).amount--;
            inputQuantity.value--;
            this.cartService.reloadCartItems();
          },
          error => {
            this.toastrService.warning(error.error.message, 'Thông báo');
          }
        );
      } else {
        this.openModal(id);
      }
    } else {
      const cartItem = this.cartItems.find(t => t.id === id);
      if (cartItem.amount === 1) {
        this.openModal(id);
      } else {
        inputQuantity.value--;
        cartItem.amount--;
        this.cartStorageService.updateCart(cartItem);
      }
    }
  }

  addQuantity(id: number, bookId: number) {
    const selector = '#cartItem' + id;
    const inputQuantity = this.el.nativeElement.querySelector(selector);

    if (this.storageService.checkIsLogin()) {
      this.cartService.updateCartItem(Number(inputQuantity.value) + 1, id, bookId).subscribe(
        next => {
          inputQuantity.value++;
          this.cartItems.find(t => t.id === id).amount++;
          this.cartService.reloadCartItems();
        },
        error => {
          this.toastrService.warning(error.error.message, 'Thông báo');
        }
      );
    } else {
      inputQuantity.value++;
      const cartItem = this.cartItems.find(t => t.id === id);
      cartItem.amount++;
      this.cartStorageService.updateCart(cartItem);
    }
  }

  checkedItem(cartItem: CartItem, checkbox) {
    if (checkbox.checked) {
      this.quantityCheck++;
      this.totalPrice += (cartItem.amount * cartItem.book.price);
      this.listPayment.push(cartItem.id);
    } else {
      this.totalPrice -= (cartItem.amount * cartItem.book.price);
      this.quantityCheck--;
      // tslint:disable-next-line:prefer-for-of
      console.log(cartItem.id);
      console.log(this.listPayment[0]);
      console.log(this.listPayment[1]);
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0 ; i < this.listPayment.length; i++) {
        if (cartItem.id === this.listPayment[i]) {
            console.log(111);
            this.listPayment.splice(i, 1);
            console.log(this.listPayment);
        }
      }
    }
    let isCheckedAll = true;
    const checkItems = this.el.nativeElement.querySelectorAll('.checked-item');
    checkItems.forEach(c => {
      if (c.checked === false) {
        isCheckedAll = false;
        return;
      }
    });
    this.el.nativeElement.querySelector('.check-add-all-products').checked = isCheckedAll;
    console.log(this.listPayment);
  }

  checkAllProducts(event) {
    // tslint:disable-next-line:prefer-for-of
    const checkItems = this.el.nativeElement.querySelectorAll('.checked-item');
    console.log(checkItems);
    if (event.target.checked) {
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < this.cartItems.length; i++) {
        this.listPayment.push(this.cartItems[i].id);
      }
      this.quantityCheck = this.cartItems.length;
      checkItems.forEach(t => {
        t.checked = true;
        this.totalPrice = 0;
        this.cartItems.forEach(c => this.totalPrice += (c.amount * c.book.price));
      });
    } else {
      this.listPayment = [];
      checkItems.forEach(t => t.checked = false);
      this.quantityCheck = 0;
      this.totalPrice = 0;
    }
    console.log(this.listPayment);
  }

  removeItem() {
    this.hiddenModal();
    const index = this.cartItems.findIndex(c => c.id === this.idCartItemDelete);
    if (this.storageService.checkIsLogin()) {
      this.cartItems.splice(index, 1);
      this.cartService.deleteCartItem(this.idCartItemDelete).subscribe(
        next => {
          this.cartService.reloadCartItems();
          this.toastrService.success('Đã xoá khỏi giỏ hàng!!!', 'Thông báo');
        }
      );
    } else {
      this.cartStorageService.removeItem(this.cartItems[index]);
    }
  }

  payment() {
    this.cartService.insertCart(this.listPayment).subscribe(data => {
      this.toastrService.success('Thanh toán thành công!!!', 'Thông báo');
      this.quantityCheck = 0;
      this.totalPrice = 0;
      this.ngOnInit();
    });
  }
}
