import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {TelegramMessageRequest} from "../dto/telegram-message-request";

@Injectable({
  providedIn: 'root'
})
export class TelegramMessengerService {

  private baseUrl = 'http://localhost:8088/api/telegram';

  constructor(private httpClient: HttpClient) {
  }

  sendMessageToTelegram(requestData: TelegramMessageRequest): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);

    const requestMessage = {
      chatId: requestData.chatId,
      message: requestData.message
    };
    return this.httpClient
      .post<TelegramMessageRequest>(
        `${this.baseUrl}/sendMessage`, requestMessage, {headers})
      .pipe(
        catchError((error) => {
          console.error('Error sending message:', error);
          return throwError(error);
        }),
      );
  }
}
