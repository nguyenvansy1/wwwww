import {AfterContentInit, AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChildren} from '@angular/core';
import {TokenStorageService} from '../../services/token-storage.service';
import {User} from '../../models/user';
import {BookService} from '../service/book.service';
import {Category} from '../../models/category';
import {Book} from '../../models/book';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {CartItem} from '../../models/cart-item';
import {CartService} from '../service/cart.service';
import {CartStorageService} from '../../services/cart-storage.service';
import {UserService} from '../service/user.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})

export class HomepageComponent implements OnInit, AfterViewInit  {
  @ViewChildren('slides', {read: ElementRef})
  public slidesElement!: QueryList<ElementRef<HTMLLIElement>>;
  user: User;
  categories: Category[] = [];
  books: Book[] = [];
  imageUrl: string;
  numberRecord: number;
  isLogin = false;
  numberRatings: number[] = [1, 2, 3, 4, 5];

  indexSlide = 0;
  images = [];
  userList: User[];
  constructor(private tokenStorageService: TokenStorageService,
              private bookService: BookService,
              private toastrService: ToastrService,
              private cartService: CartService,
              private cartStorageService: CartStorageService,
              private router: Router,
              private userService: UserService,
              private el: ElementRef) {

  }

  ngOnInit(): void {
    this.user = {
      name: ''
    };
    this.userService.getList().subscribe(data => {
      this.userList = data;
      console.log(data);
    });
    if (this.tokenStorageService.checkIsLogin()) {
      this.user = this.tokenStorageService.getUser();
      if (this.tokenStorageService.getUser().roles[0].name === 'ROLE_ADMIN') {
        this.isLogin = true;
      }
    }
    const numberRecord = window.sessionStorage.getItem('numberRecord');
    if (numberRecord) {
      this.numberRecord = Number(numberRecord);
    } else {
      this.numberRecord = 9;
      window.sessionStorage.setItem('numberRecord', '9');
    }
    this.getCategories();
    this.getBooks();
  }

  ngAfterViewInit(): void {
    this.sliderAdvertisement();
  }

  getCategories() {
    this.bookService.getAllCategories().subscribe(categories => {
      this.categories = categories;
    });
  }
  getBooks() {
    this.bookService.getBooksByNumberRecord(this.numberRecord).subscribe(books => {
      if (books.length === this.books.length) {
        this.numberRecord -= 9;
        console.log(this.numberRecord);
        window.sessionStorage.setItem('numberRecord', this.numberRecord.toString());
        // this.toastrService.warning('Hết sản phẩm tìm kiếm', 'Thông báo');
      } else {
        this.books = books;
      }
    });
  }
  viewMore() {
    this.numberRecord += 9;
    window.sessionStorage.setItem('numberRecord', this.numberRecord.toString());
    this.getBooks();
  }

  sliderAdvertisement() {
    const slides = this.slidesElement.toArray().map(x => x.nativeElement);
    const sizeSlides = slides.length;

    // slides.forEach(e => {
    //   e.style.display = 'none';
    //   e.style.opacity = '0';
    // });
    // slides[0].style.display = 'block';
    // slides[0].style.opacity = '1';

    // window.setInterval(() => {
    //   if (this.indexSlide === sizeSlides) {
    //     this.indexSlide = 0;
    //   }
    //   if (this.indexSlide === -1) {
    //     this.indexSlide = sizeSlides - 1;
    //   }
    //   for (let i = 0; i < sizeSlides; i++) {
    //     slides[i].style.display = 'none';
    //     slides[i].style.opacity = '0';
    //   }
    //   slides[this.indexSlide].style.display = 'block';
    //   slides[this.indexSlide].style.opacity = '1';
    //   this.indexSlide++;
    // }, 3000);
  }

  search(q: string) {
    this.router.navigateByUrl('/search?q=' + q + '&page=1');
  }

  addToCart(id: number) {
    console.log('add cart');
    const amount = 1;
    if (amount > 0) {
      if (this.tokenStorageService.checkIsLogin()) {
        this.cartService.addToCart(amount, this.tokenStorageService.getUser().cart.id , id).subscribe(
          next => {
            this.toastrService.success('Thêm vào giỏ hàng thành công !!!');
            this.cartService.reloadCartItems();
          }
        );
      } else {
        this.toastrService.error('Vui lòng đăng nhập tài khoản!!!');
        const cartItem: CartItem = {
          amount: Number(amount)
        };
        this.cartStorageService.addToCart(cartItem);
      }
    }
  }
}
