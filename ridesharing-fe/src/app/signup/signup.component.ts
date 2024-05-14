import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit{

  riderForm: FormGroup;
  isLoading = false;
  error: string = null;

  ngOnInit() {
    this.riderForm = new FormGroup({
      firstname: new FormControl(null, [Validators.required]),
     lastname: new FormControl(null, Validators.required),
     email: new FormControl(null, [Validators.required, Validators.email]),
     password: new FormControl(null, Validators.required)
    })
  }

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(){

    if(!this.riderForm.valid)
      return;

    const firstName = this.riderForm.value.firstname;
    const lastName = this.riderForm.value.lastname;
    const email = this.riderForm.value.email;
    const password = this.riderForm.value.password;

    console.log(this.riderForm.value)

    this.authService.signup(firstName,lastName,email,password)
      .subscribe({
        next: (resp) => {
          if(resp.statusCode === "BAD_REQUEST")
           return this.error = resp.body;

          this.riderForm.reset();
          this.router.navigate(["/rider"])
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
          console.log("completed");
        }
      })

  }
}
