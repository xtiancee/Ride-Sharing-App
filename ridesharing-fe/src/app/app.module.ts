import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { AuthComponent } from './auth/auth.component';
import { AdminComponent } from './admin/admin.component';
import { RiderComponent } from './rider/rider.component';
import { DriverComponent } from './driver/driver.component';
import { UsersComponent } from './admin/users/users.component';
import { DriversComponent } from './admin/drivers/drivers.component';
import {AppRoutingModule} from "./app-routing.module";
import { SignupComponent } from './signup/signup.component';
import { LoadingSpinnerComponent } from './shared/loading-spinner/loading-spinner.component';
import {DropdownDirective} from "./shared/dropdown.directive";
import {AuthInterceptorService} from "./auth/auth.interceptor";
import { DrivereditComponent } from './admin/driveredit/driveredit.component';
import {StompRService} from "@stomp/ng2-stompjs";
import { RiderRidesComponent } from './rider/rider-rides/rider-rides.component';
import { DriverRidesComponent } from './driver/driver-rides/driver-rides.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    AuthComponent,
    AdminComponent,
    RiderComponent,
    DriverComponent,
    UsersComponent,
    DriversComponent,
    SignupComponent,
    LoadingSpinnerComponent,
    DropdownDirective,
    DrivereditComponent,
    RiderRidesComponent,
    DriverRidesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [StompRService, { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
