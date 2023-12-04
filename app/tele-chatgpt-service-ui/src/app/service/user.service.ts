import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8088/api/users';

  constructor(private httpClient: HttpClient) {
  }

  public updateIsActiveStatus(isActive: boolean): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    });

    return this.httpClient.patch<any>(
        `${this.apiUrl}/${isActive}`,
        null,
        { headers },
    ).pipe(
        catchError((error: any) => {
          console.error('Error during updateIsActiveStatus:', error);
          return throwError(error);
        })
    );
  }
}
