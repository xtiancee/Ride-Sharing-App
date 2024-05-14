import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { User } from "../auth/user.model";
import {UserDto} from "../auth/auth-service";
@Injectable({providedIn: 'root'})
export class UserService {

  usersChanged = new Subject<UserDto[]>();
  private users: UserDto[] = [];

  setUsers(users: UserDto[]){
    this.users = users;
    this.usersChanged.next(this.users.slice());
  }

  getUsers() {
    return this.users.slice();
  }

  getDrivers() {
      const users = this.users.slice();
      return this.users.filter((user) => user.type === "DRIVER").slice()
  }


  getUser(index: number) {
    return this.users[index]
  }

  addUser(user: User){
    this.users.push(user);
    this.usersChanged.next(this.users.slice());
  }

  //
  // updateRecipe(index:number, newRecipe: Recipe){
  //   this.users[index] = newRecipe;
  //   this.usersChanged.next(this.users.slice());
  // }

  deleteUser(index: number){
    this.users.splice(index, 1);
    this.usersChanged.next(this.users.slice());
  }
}
