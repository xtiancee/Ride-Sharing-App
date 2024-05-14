import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AuthComponent} from "./auth/auth.component";
import {AdminComponent} from "./admin/admin.component";
import {RiderComponent} from "./rider/rider.component";
import {DriverComponent} from "./driver/driver.component";
import {SignupComponent} from "./signup/signup.component";
import {UsersComponent} from "./admin/users/users.component";
import {UserResolverService} from "./admin/users-resolver";
import {DriversComponent} from "./admin/drivers/drivers.component";
import {DrivereditComponent} from "./admin/driveredit/driveredit.component";
import {RiderRidesComponent} from "./rider/rider-rides/rider-rides.component";
import {DriverRidesComponent} from "./driver/driver-rides/driver-rides.component";

const appRoutes: Routes = [
  { path: '', component: AuthComponent },
  { path: 'auth', component: AuthComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'admin', component: AdminComponent, resolve:[UserResolverService], children: [
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      { path: 'users', resolve:[UserResolverService], component: UsersComponent },
      { path: 'drivers', resolve:[UserResolverService], component: DriversComponent },
      { path: 'drivers/new', component: DrivereditComponent }
    ] },
    { path: 'rider', component: RiderComponent },
    { path: 'rider-rides', component: RiderRidesComponent },
    { path: 'rider', component: RiderComponent },
    { path: 'driver', component: DriverComponent },
    { path: 'driver/await-ride', component: DriverComponent },
    { path: 'driver-rides', component: DriverRidesComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
