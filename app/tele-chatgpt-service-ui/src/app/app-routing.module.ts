import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRegisterComponent } from './user-register/user-register.component';
import { UserLoginComponent } from './user-login/user-login.component';
import {UserAdminChatComponent} from "./user-admin-chat/user-admin-chat.component";
import {AuthorizationGuardService} from "./service/authorization-guard.service";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: UserLoginComponent },
  { path: 'register', component: UserRegisterComponent },
  { path: 'user-admin-chat/:id', component: UserAdminChatComponent,
    canActivate: [AuthorizationGuardService] },
  { path: 'user-admin-chat', component: UserAdminChatComponent,
    canActivate: [AuthorizationGuardService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
