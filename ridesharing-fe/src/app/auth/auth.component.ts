import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "./auth-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements OnInit {

  loginForm: FormGroup;
  isLoading = false;
  error: string = null;

  ngOnInit() {
    this.loginForm = new FormGroup({
        email: new FormControl(null, [Validators.required, Validators.email]),
        password: new FormControl(null, Validators.required)
    });
  }

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(){

    if(!this.loginForm.valid){
      return;
    }
    const email = this.loginForm.value.email;
    const password = this.loginForm.value.password;

    this.isLoading = true;
    this.error = null;

    this.authService.login(email,password)
      .subscribe({
        next: (resp) => {
          if(resp.user.type === "ADMIN"){
            this.router.navigate(["/admin"])
          }else if(resp.user.type === "DRIVER"){
            this.router.navigate(["/driver"])
          }else{
            this.router.navigate(["/rider"])
          }
        },
        error: (err) => {
          console.log("Error is ", err)
          if(err && err.status === 403){
            this.error = "Invalid credentials."
          }else{
            this.error = "An unknown error occurred!"
          }
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
          console.log("completed")
        }
      })
    this.loginForm.reset();
  }
}
