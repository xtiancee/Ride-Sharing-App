import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from "@angular/router";
import { Observable } from "rxjs";
import {User} from "../auth/user.model";
import {DataStorageService} from "../shared/data-storage.service";
import {UserService} from "./user.service";

@Injectable({providedIn: 'root'})
export class UserResolverService implements Resolve<User[]> {

  constructor(
    private dataStorageService: DataStorageService,
    private userService: UserService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    User[] | Observable<User[]> | Promise<User[]> {

    const users = this.userService.getUsers();
    if(users.length === 0)
      return this.dataStorageService.fetchUsers();

    return users;
  }
}
