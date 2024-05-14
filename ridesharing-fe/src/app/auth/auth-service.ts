import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { BehaviorSubject, catchError, tap, throwError } from "rxjs";
import { User } from "./user.model";
import {UserService} from "../admin/user.service";

export interface UserDto {
  id: string,
  firstName: string,
  lastName: string,
  email: string,
  type: string,
  userTypeId?: string
}
export interface AuthResponseData {
  access_token: string,
  user: UserDto
}

@Injectable({providedIn: 'root'})
export class AuthService {

  user = new BehaviorSubject<User>(null);
  private tokenExpirationTimer: any;

  constructor(private http: HttpClient, private router: Router, private usersService: UserService) {}

  login(email: string, password: string) {
    return this.http.post<AuthResponseData>(
      'http://localhost:8080/api/v1/auth/authenticate',
      { email: email, password: password }
    ).pipe(
      tap(rp => {
        const userDto = rp.user;
        const user = new User(userDto.id, userDto.firstName, userDto.lastName,
          userDto.email, userDto.type, rp.access_token, userDto.userTypeId);
        localStorage.setItem('userData', JSON.stringify(user));
        this.user.next(user)
      })
    )
  }
  signup(firstName:string, lastName:string, email: string, password: string){
    return this.http.post<AuthResponseData | any>(
      'http://localhost:8080/api/v1/auth/rider',
      {firstName: firstName, lastName:lastName, email:email, password: password, userId: ""})
      .pipe(
        tap(rp => {
          console.log("Response from tap", rp)
          if(rp.statusCode !== "BAD_REQUEST"){
            console.log(rp);
            rp = rp.body;
            const userDto = rp.user;
            const user = new User(userDto.id, userDto.firstName, userDto.lastName,
              userDto.email, userDto.type, rp.access_token, userDto.user);
            localStorage.setItem('userData', JSON.stringify(user));
            this.user.next(user);
          }
        }));
  }

  onboardDriver(firstName:string, lastName:string, email: string, password: string, cartype:string, carplate: string){
    return this.http.post<AuthResponseData | any>(
        'http://localhost:8080/api/v1/auth/driver',
        {
          firstName: firstName, lastName:lastName, email:email,
          password: password, carType:cartype, carNumberPlate:carplate, userId: ""
        })
        .pipe(
            tap(rp => {
              console.log("Response from tap", rp)
              if(rp.statusCode !== "BAD_REQUEST"){
                const userDto = rp.body;
                const user = new User(userDto.id, userDto.firstName, userDto.lastName,
                    userDto.email, userDto.type);
                this.usersService.addUser(user);
              }
            }));
  }

  logout(){
    this.user.next(null);
    this.router.navigate(['/auth']);
    localStorage.removeItem('userData');
  }

  autoLogin(){
    const userData: {id: string, firstName:string, lastName:string, email:string, type:string, token:string, userTypeId:string} = JSON.parse(localStorage.getItem('userData'));
    if(!userData)
      return;

    const loadedUser = new User(userData.id, userData.firstName, userData.lastName, userData.email, userData.type, userData.token, userData.userTypeId);
    if(loadedUser.token){
      this.user.next(loadedUser);
      if(loadedUser.type === "ADMIN"){
        this.router.navigate(["/admin"])
      }else if(loadedUser.type === "DRIVER"){
        this.router.navigate(["/driver"])
      }else{
        this.router.navigate(["/rider"])
      }
    }
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer =  setTimeout(() => {
      this.logout()
    }, expirationDuration);
  }

  private handleError(errorRes: HttpErrorResponse) {

    let errorMsg = "An Unknown Error Occured!";

    if(!errorRes.error){
      return throwError(errorMsg);
    }

    switch(errorRes.error.error.message){
      case 'EMAIL_EXISTS':
        errorMsg = "This email already exists";
        break;

      case 'EMAIL_NOT_FOUND':
        errorMsg = 'Email does not exist';
        break;

      case 'INVALID_PASSWORD':
        errorMsg = 'This password is not correct';
        break;
    }

    return throwError(errorMsg)
  }

  private handleAuth(authResp: AuthResponseData){
    //const user = new User(email, userId, token, expirationDate)
   // this.user.next(user);
   // this.autoLogout(expiresIn * 1000);
  //  localStorage.setItem('userData', JSON.stringify(user));
  }
}
