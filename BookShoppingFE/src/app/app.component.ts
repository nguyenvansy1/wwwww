import {Component, OnInit} from '@angular/core';
import {CartStorageService} from './services/cart-storage.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'LoginOAuth2FE';
  constructor(private cartStorageService: CartStorageService) {
  }
  ngOnInit(): void {
    this.cartStorageService.loadCart();
  }
}
