import {Component, OnInit} from '@angular/core';
import {UserRegisterRequest} from "../dto/user-register-request";
import {AuthenticationService} from "../service/authentication.service";

@Component({
    selector: 'app-user-register',
    templateUrl: './user-register.component.html',
    styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {
    registerRequest: UserRegisterRequest = {
        email: '',
        password: '',
        repeatPassword: '',
        firstName: '',
        lastName: '',
        registerCode: ''
    };
    messages: string[] = [];

    constructor(private authenticationService: AuthenticationService) {
    }

    ngOnInit(): void {
        this.messages = this.authenticationService.messageService.messages;

    }

    register() {
        this.authenticationService.register(this.registerRequest)
            .subscribe(response => {
                    console.log(response);
                },
                (error) => {
                    this.messages = this.authenticationService.messageService.messages;
                    console.error('Error during registration:', error);
                }
            );
    }
}
