import { Component } from '@angular/core';
import {error} from "@angular/compiler-cli/src/transformers/util";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-driveredit',
  templateUrl: './driveredit.component.html',
  styleUrl: './driveredit.component.css'
})
export class DrivereditComponent {

  driverForm: FormGroup;
  isLoading = false;
  error: string = null;

  ngOnInit() {
    this.driverForm = new FormGroup({
      firstname: new FormControl(null, [Validators.required]),
      lastname: new FormControl(null, Validators.required),
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, Validators.required),
      cartype: new FormControl(null, Validators.required),
      carplate: new FormControl(null, Validators.required)
    })
  }

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(){

    if(!this.driverForm.valid)
      return;

    const firstName = this.driverForm.value.firstname;
    const lastName = this.driverForm.value.lastname;
    const email = this.driverForm.value.email;
    const password = this.driverForm.value.password;
    const cartype = this.driverForm.value.cartype;
    const carplate = this.driverForm.value.carplate;

    console.log(this.driverForm.value)

    this.authService.onboardDriver(firstName,lastName,email,password,cartype,carplate)
        .subscribe({
          next: (resp) => {
            console.log("Response in next ", resp);
            if(resp.statusCode == "BAD_REQUEST") {
              return this.error = resp.body;
            }
            this.router.navigate(["/admin/drivers"]);
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
          }
        })

    this.driverForm.reset();
  }
}
