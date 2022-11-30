import {Cart} from './cart';

export interface User {
  id?: number;
  name?: string;
  email?: string;
  imageUrl?: string;
  cart?: Cart;
}
