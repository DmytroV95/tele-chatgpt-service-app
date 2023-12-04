import {Component, OnInit} from '@angular/core';
import {UserLoginRequest} from "../dto/user-login-request";
import {AuthenticationService} from "../service/authentication.service";

@Component({
    selector: 'app-user-login',
    templateUrl: './user-login.component.html',
    styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {
    loginRequest: UserLoginRequest = {
        email: '',
        password: ''
    };
    messages: string[] = [];

    constructor(public authenticationService: AuthenticationService) {
    }

    ngOnInit(): void {
        this.messages = this.authenticationService.messageService.messages;
    }

    login(): void {
        this.authenticationService.login(this.loginRequest).subscribe(
            (response) => {
                console.log(response);
            },
            (error) => {
                console.error('Error during login:', error);
            }
        );
    }
}
