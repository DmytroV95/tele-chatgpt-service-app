import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "./service/authentication.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    title = 'Admin management service';
    isActive: boolean = false;

    constructor(private authenticationService: AuthenticationService) {
    }

    ngOnInit(): void {
        this.authenticationService.isActive$.subscribe((isActive) => {
            this.isActive = isActive;
        });
    }

    logout(): void {
        this.authenticationService.logout().subscribe(
            (response) => {
                console.log(response);
            },
            (error) => {
                console.error('Error during logout:', error);
            }
        );
    }
}
