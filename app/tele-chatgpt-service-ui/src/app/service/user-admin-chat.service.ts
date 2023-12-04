import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {UserAdminChatResponse} from "../dto/user-admin-chat-response";

@Injectable({
    providedIn: 'root'
})
export class UserAdminChatService {
    private apiUrl = 'http://localhost:8088/api/history';

    constructor(private httpClient: HttpClient) {}

    getAllChatLogs() {
        console.log('Executing getAllChatLogs');
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders()
            .set('Authorization', `Bearer ${token}`);
        console.log('Executing...2');
        return this.httpClient
            .get<UserAdminChatResponse[]>(
                `${this.apiUrl}`, {headers});
    }

    getChatLogsById(id: string) {
        const token = localStorage.getItem('token');
        const headers = new HttpHeaders()
            .set('Authorization', `Bearer ${token}`);
        const params = new HttpParams()
            .set('id', id);
        return this.httpClient
            .get<UserAdminChatResponse[]>(
              `${this.apiUrl}/chat/id`,
              {headers, params});
    }
}
