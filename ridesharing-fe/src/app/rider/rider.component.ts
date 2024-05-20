import { Component, OnDestroy, OnInit} from '@angular/core';
import {WebsocketService} from "../websocket.service";
import { Subscription, take} from "rxjs";
import {AuthService} from "../auth/auth-service";
import {User} from "../auth/user.model";

declare const L: any;

@Component({
  selector: 'app-rider',
  templateUrl: './rider.component.html',
  styleUrl: './rider.component.css'
})
export class RiderComponent implements OnInit, OnDestroy {

  textLabel = 'Please wait while we find you a close driver for your trip ...';
  rideLocIsSet = false;
  rideInProgress = false;
  currentLoc = null;
  destLoc = null;
  sub: Subscription;
  user: User = null;
  intervalId:any;
  driverId: string;
  driverName = "Awaiting..";
  driverLoc = "Awaiting";
  driverApproved = false;
  map: any;
  driverLocInterval: any;
  riderMarker: any;
  driverMarker: any;
  ridePayload: any
  driverNotFound = false;

  constructor(private webSocketService: WebsocketService,
              private authService: AuthService) {
    this.authService.user.pipe(take(1)).subscribe((user) => this.user = user);

    const headers = {"Authorization": `Bearer ${this.user.token}`, "Say": "wow"}
    this.webSocketService.connect(headers).subscribe();
  }
  ngOnInit() {

    this.map = L.map('map').setView([4.863369, 6.999161], 13);

    this.sub = this.webSocketService.getConnectionStatus().pipe(
        take(1) // Take only one value (true when connected)
    ).subscribe((connected: boolean) => {
      if (connected) {
        this.webSocketService.riderSubscribe()
            .subscribe((message) => {
              message = JSON.parse(message)
              if(message.type === "DRIVER_APPROVED"){
                this.driverName = message.message.driverName;
                this.driverLoc = message.message.driverLng + "," + message.message.driverLat;
                this.driverId = message.message.ride.driverId;
                this.textLabel = `Hi ${this.user.firstName} ${this.user.lastName}, Your driver is on his way ...`;
                this.driverApproved = true;

                this.getDriverLocation();

                this.driverMarker = L.marker([message.message.driverLat, message.message.driverLng])
                  .addTo(this.map);
                this.driverMarker.bindPopup("Driver Location")

                var popup = L.popup();

                popup
                    .setLatLng(location)
                    .setContent("Driver Location")
                    .openOn(this.map);
              }

              if(message === "DRIVER_LOCATION"){
                this.driverMarker.setLatLng([message.lng, message.lat]);
              }
              if(message.type === "DRIVER_NOT_FOUND"){
                this.driverNotFound = true;
              }

            });
      } else {
        console.log("on init stomp client is connected");
        // Handle connection failure if needed
      }
    });

    if(!navigator.geolocation)
      console.log("location is not supported");

    navigator.geolocation.getCurrentPosition((position) => {

      const coords = position.coords;
      const location = [coords.latitude, coords.longitude];
      this.currentLoc = [coords.longitude, coords.latitude].toString();

      L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      }).addTo(this.map);

      this.riderMarker = L.marker(location).addTo(this.map);

      this.riderMarker.bindPopup("My Location")

      var popup = L.popup();

      popup
          .setLatLng(location)
          .setContent("My Location ")
          .openOn(this.map);

      this.map.on('click', (e) => {

        if(this.rideInProgress){
          return;
        }

        this.rideLocIsSet = false;
        this.destLoc = null;

        popup
            .setLatLng(e.latlng)
            .setContent("You picked the location " + e.latlng.lng + " " + e.latlng)
            .openOn(this.map);

        this.destLoc = [e.latlng.lng, e.latlng.lat].toString();
        this.rideLocIsSet = true;
      })
    })
  }

  locationUpdate() {

    this.intervalId = setInterval(() => {

      if(!navigator.geolocation){
        return;
      }else{
        navigator.geolocation.getCurrentPosition((position) => {

          const coords = position.coords;

          const userLoc = {
            userTypeId: this.user.email,
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            userId: this.user.id,
            userType: this.user.type,
            lat: coords.latitude,
            lng: coords.longitude
          }

          console.log("Location DATA: ", userLoc)

          this.webSocketService.sendLocation(userLoc);

        },(error) => {
          console.log("Error ", error);
        }, {
          enableHighAccuracy: false,
          // timeout: 5000,
          maximumAge: 0
        })
      }

    }, 5000);
  }

  onProceed(){

    this.rideLocIsSet = false;
    this.locationUpdate();

    navigator.geolocation.getCurrentPosition((position) => {

      const coords = position.coords;

      const rideRequest = {
        riderId: this.user.email,
        riderName: this.user.firstName + " " + this.user.lastName,
        fare: 150,
        source: [coords.longitude, coords.latitude].toString(),
        destination: this.destLoc.toString()
      }

      this.ridePayload = rideRequest;
      this.sendForRide();


    },(error) => {
      console.log("Error ", error);
    }, {
      enableHighAccuracy: false,
      // timeout: 5000,
      maximumAge: 0
    });

    this.rideInProgress = true;
  }

  sendForRide() {
    this.driverNotFound = false;
    this.textLabel = 'Please wait while we find you a close driver for your trip ...';
    this.webSocketService.onNewRide(this.ridePayload);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.webSocketService.disconnect();
    if(this.intervalId){
      clearInterval(this.intervalId);
    }

    if(this.driverLocInterval){
      clearInterval(this.intervalId);
    }
  }

  onCancelRideRequest(){
    this.rideLocIsSet = false;
    this.rideInProgress = false;
  }

  getDriverLocation(){

   this.driverLocInterval = setInterval(() => {
      const dto = {
        from: this.user.email,
        fromType: this.user.type,
        userToLocate: this.driverId
      }

      this.webSocketService.getDriverLocation(dto);
    },5000)
  }
}
