import {Component, OnDestroy, OnInit} from '@angular/core';
import {map, Subscription, take} from "rxjs";
import {User} from "../auth/user.model";
import {WebsocketService} from "../websocket.service";
import {AuthService} from "../auth/auth-service";
import {RideRequest} from "./ride-request.model";

declare const L: any;

export interface RideRequestResponse {
  rideId: string,
  riderName: string,
  fair: string,
  source: string,
  destination: string
}

@Component({
  selector: 'app-driver',
  templateUrl: './driver.component.html',
  styleUrl: './driver.component.css'
})
export class DriverComponent implements OnInit, OnDestroy {

  rideRequestIsSet = false;
  rideLocIsSet = false;
  currentLoc = null;
  sub: Subscription;
  user: User = null;
  intervalId: any;
  rideRequest: RideRequest = null;
  rideRequestAccepted = false;
  h5TextLabel = "Incoming Ride Request";
  riderId: string;
  riderLocInterval: any;
  map: any;
  driverMarker: any;
  riderMarker: any
  riderLat: any;
  riderLng: any;

  constructor(private webSocketService: WebsocketService,
              private authService: AuthService) {

    this.authService.user.pipe(take(1)).subscribe((user) => this.user = user);

    const headers = {"Authorization": `Bearer ${this.user.token}`}
    this.webSocketService.connect(headers).subscribe();
  }

ngOnInit() {

    this.map = L.map('map').setView([4.863369, 6.999161], 13);

    this.locationUpdate();

    this.sub = this.webSocketService.getConnectionStatus().pipe(
        take(1) // Take only one value (true when connected)
    ).subscribe((connected: boolean) => {
      if (connected) {
        console.log("stomp client is connected");
        this.webSocketService.driverSubscribe()
            .subscribe((message) => {
              console.log("Message from websocket received: ", message)
              message = JSON.parse(message)
              if(message.type === "REQUEST_PENDING"){
                  let reqBody = message.message.ride;
                  this.riderId = reqBody.riderId;

                  let riderLatLng = reqBody.source.split(",");
                  this.riderLat = riderLatLng[1];
                  this.riderLng = riderLatLng[0];

                  console.log("RIDER LAT LNG ", riderLatLng)

                  this.rideRequest = new RideRequest(reqBody.id, reqBody.riderName, reqBody.fare, reqBody.source, reqBody.destination);
                  this.rideRequestIsSet = true;

                  this.riderMarker = L.marker([this.riderLat, this.riderLng]).addTo(this.map);

                  this.riderMarker.bindPopup("My Location")

                  var popup = L.popup();

                 popup
                  .setLatLng(location)
                  .setContent("Rider Location ")
                  .openOn(this.map);
              }

              if(message === "DRIVER_LOCATION"){
                this.riderMarker.setLatLng([message.lng, message.lat]);
              }
            });
      } else {
        console.log("on init stomp client is connected");
      }
    });


    if(!navigator.geolocation)
      console.log("location is not supported");

    navigator.geolocation.getCurrentPosition((position) => {

      const coords = position.coords;
      const location = [coords.latitude, coords.longitude];

      this.currentLoc = [coords.longitude,coords.latitude].toString();

      L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      }).addTo(this.map);

      this.driverMarker = L.marker(location).addTo(this.map);

      this.driverMarker.bindPopup("My Location")

      var popup = L.popup();

      popup
          .setLatLng(location)
          .setContent("My Location ")
          .openOn(this.map);
    });
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

  onRequestApproval(status: boolean){

    if(!status){
      this.rideRequestIsSet = false;
    }

    const approvalDto = {
      rideId: this.rideRequest.id,
      driverId: this.user.email,
      driverName: `${this.user.firstName} ${this.user.lastName}`,
      driverLocation: this.currentLoc,
      approved: status
    }

    console.log("Data for approval ", approvalDto);

    this.webSocketService.rideApproval(approvalDto);

    if(status) {
      this.h5TextLabel = "Proceed to Rider Location and click the start Ride Button after you get to location"
      this.rideRequestAccepted = true;
      this.getRiderLocation();
    }
  }


  getRiderLocation(){

    this.riderLocInterval = setInterval(() => {
      const dto = {
        from: this.user.email,
        fromType: this.user.type,
        userToLocate: this.riderId
      }

      console.log("Driver To Locate ", dto)
      this.webSocketService.getDriverLocation(dto);
    },5000)
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.webSocketService.disconnect();
    clearInterval(this.intervalId);
    if(this.riderLocInterval){
      clearInterval(this.riderLocInterval)
    }
  }
}
