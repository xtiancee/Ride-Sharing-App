import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../auth/auth-service";
import {Subscription} from "rxjs";
import {User} from "../auth/user.model";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {

  constructor(private authService: AuthService) {}

  isAuthenticated = false;
  sub: Subscription;
  user: User

  ngOnInit() {
    this.sub = this.authService.user.subscribe(user => {
      this.isAuthenticated = !!user;
      this.user = user;
    })
  }

  onLogout(){
    this.authService.logout();
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
