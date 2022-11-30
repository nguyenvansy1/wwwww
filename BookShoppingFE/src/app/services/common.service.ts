import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ResponseMessage} from '../models/response-message';
const API_URL = 'http://localhost:8080/email/getOtpRegister';
@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor(private http: HttpClient) { }

  getOtpRegister(email: string): Observable<ResponseMessage> {
    return this.http.get<ResponseMessage>(API_URL + '?email=' + email);
  }
}
