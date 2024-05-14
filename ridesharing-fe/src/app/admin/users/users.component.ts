import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../auth/user.model";
import {pipe, Subscription, take} from "rxjs";
import {UserService} from "../user.service";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit, OnDestroy {

  users: User[] = [];
  sub: Subscription;

  constructor(private userService: UserService) {
    this.users = this.userService.getUsers();
  }

  ngOnInit() {
    this.sub = this.userService.usersChanged.subscribe(
      (users: User[]) => {
              this.users = users;
              console.log("Users in Sub ", JSON.stringify(this.users));
      });

    console.log("Users are ", this.userService.getUsers());
    console.log(this.userService.usersChanged.subscribe())
  }

  ngAfterViewInit() {
    console.log("After view init")
    this.userService.usersChanged.subscribe((users: User[]) => {
      console.log("AFter View Init ", users);
    })
  }


    ngOnDestroy() {
      this.sub.unsubscribe()
  }
}
