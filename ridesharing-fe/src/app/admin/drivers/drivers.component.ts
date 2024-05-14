import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../auth/user.model";
import {Subscription} from "rxjs";
import {UserService} from "../user.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrl: './drivers.component.css'
})
export class DriversComponent implements OnInit, OnDestroy {

  users: User[] = [];
  sub: Subscription;

  constructor(private userService: UserService, private route: ActivatedRoute,
              private router: Router) {
    this.users = this.userService.getDrivers();
  }

  ngOnInit() {
    this.sub = this.userService.usersChanged.subscribe(
        (users: User[]) => {
          this.users = users.filter((user) => user.type === "DRIVER");
        });
  }

  ngOnDestroy() {
    this.sub.unsubscribe()
  }

  onAddDriver(){
    this.router.navigate(['/admin/drivers/new'])
  }
}
