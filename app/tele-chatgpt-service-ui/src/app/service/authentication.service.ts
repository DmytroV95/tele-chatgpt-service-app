import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, catchError, mergeMap, Observable, of, switchMap, tap, throwError} from "rxjs";
import {UserLoginRequest} from "../dto/user-login-request";
import {UserLoginResponse} from "../dto/user-login-response";
import {UserRegisterRequest} from "../dto/user-register-request";
import {UserRegisterResponse} from "../dto/user-register-response";
import {NavigationEnd, Router} from "@angular/router";
import {MessageService} from "./message.service";
import {UserService} from "./user.service";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    private authApiUrl = 'http://localhost:8088/api/auth';

    private isActiveSubject = new BehaviorSubject<boolean>(false);
    isActive$: Observable<boolean> = this.isActiveSubject.asObservable();

    constructor(private httpClient: HttpClient,
                private router: Router,
                public messageService: MessageService,
                private userService: UserService) {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.messageService.clear();
            }
        });
    }

    login(request: UserLoginRequest): Observable<UserLoginResponse> {
        return this.httpClient.post<UserLoginResponse>(
            `${this.authApiUrl}/login`, request, {withCredentials: true})
            .pipe(
                switchMap((response: UserLoginResponse) => {
                    if (response.token) {
                        localStorage.setItem('token', response.token);
                        this.isActiveSubject.next(true);
                        return this.userService.updateIsActiveStatus(true);
                    }
                    return new Observable();
                }),
                tap(() => {
                    this.router.navigate(['/user-admin-chat']);
                    this.messageService.clear();
                }),
                catchError((error: any) => {
                    this.showInfoMessage('Login or Password is not correct');
                    console.error('Error during login:', error);
                    return throwError(error);
                })
            );
    }

    logout(): Observable<any> {
        return this.userService.updateIsActiveStatus(false)
            .pipe(
                mergeMap(() => {
                    localStorage.removeItem('token');
                    this.isActiveSubject.next(false);
                    this.router.navigate(['/login']);
                    return of(null);
                }),
                catchError((error) => {
                    console.error('Error during logout:', error);
                    return throwError(error);
                })
            );
    }

    register(request: UserRegisterRequest): Observable<UserRegisterResponse> {
        if (!this.areAllFieldsFilled(request)) {
            this.showInfoMessage('Please fill in all required fields');
            return throwError('Not all required fields are filled');
        }
        return this.httpClient.post<UserRegisterResponse>(
            `${this.authApiUrl}/register`, request
        ).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && error.status === 500) {
                    this.messageService.clear()
                    this.showInfoMessage('Incorrect input data. Please try again.');
                } else {
                    this.showInfoMessage('An error occurred during registration. Please try again.');
                }
                return throwError(error);
            }),
            tap(() => {
                this.messageService.clear()
                this.router.navigate(['/login']);
            })
        );
    }

    private areAllFieldsFilled(request: UserRegisterRequest): boolean {
        return (
            !!request.email &&
            !!request.password &&
            !!request.repeatPassword &&
            !!request.firstName &&
            !!request.lastName &&
            !!request.registerCode
        );
    }

    showInfoMessage(message: string) {
        if (!this.messageService.messages.includes(message)) {
            this.messageService.add(message);
        }
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem('token');
    }
}
