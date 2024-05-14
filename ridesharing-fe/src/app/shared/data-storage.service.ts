import { HttpClient  } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { tap } from "rxjs";
import {UserDto} from "../auth/auth-service";
import {UserService} from "../admin/user.service";
@Injectable({providedIn: 'root'})
export class DataStorageService {

    constructor(
        private http: HttpClient,
        private userService: UserService){ }

    fetchUsers(){
          return this.http.get<UserDto[]>('http://localhost:8080/api/v1/auth/users')
          .pipe(
           tap(users => {
             this.userService.setUsers(users);
          })
        )
    }
}
