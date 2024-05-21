import {Injectable} from "@angular/core";
import {EMPTY, Observable, Subject} from "rxjs";
import SockJS from "sockjs-client";
import * as Stomp from 'stompjs';


@Injectable({ providedIn:  "root" })
export class WebsocketService {

    private stompClient;
    private connectionStatusSubject: Subject<boolean> = new Subject<boolean>();

    connect(headers: any): Observable<any> {
      const socket = new SockJS('http://localhost:8080/ws', null, {

      });

       this.stompClient = Stomp.over(socket);
        return new Observable(observer => {
            this.stompClient.connect(headers, () => {
                observer.next();
                this.connectionStatusSubject.next(true);
            },
                (err) => {
                console.log("Error from connect ", err)
                }
            );
        });
    }

    getConnectionStatus(): Observable<boolean> {
        return this.connectionStatusSubject.asObservable();
    }

    updateLocation(message) {
       this.stompClient.send('/app/message', {}, message);
    }

    subscribe(): Observable<any> {
        return new Observable(observer => {
            this.stompClient.subscribe('/topic/messages', (message) => {
                observer.next(message.body);
            });
        });
    }

    sendLocation(userLocation: any){
        this.stompClient.send("/app/location.update", {}, JSON.stringify(userLocation));
    }

    startRide(dto: {rideId: string }){
      this.stompClient.send("/app/ride.startRide", {}, JSON.stringify(dto));
    }

    endRide(dto: {rideId: string }){
      this.stompClient.send("/app/ride.endRide", {}, JSON.stringify(dto));
    }

    getDriverLocation(dto: any){
      this.stompClient.send("/app/ride.requestUserLocation", {}, JSON.stringify(dto));
    }

    onNewRide(rideRequest: any){
        this.stompClient.send("/app/ride.new", {}, JSON.stringify(rideRequest));
    }

    rideApproval(approvalRequest: any){
        this.stompClient.send("/app/ride.approving", {}, JSON.stringify(approvalRequest));
    }

    driverSubscribe(): Observable<any> {
        if (this.stompClient && this.stompClient.connected) {
            return new Observable(observer => {
                this.stompClient.subscribe('/user/driver', (message) => {
                    observer.next(message.body);
                });
            });
        } else {
            console.log("Stomp client is not connected")
            // Handle the case where the client is not connected
            console.error('StompClient is not connected.');
            return EMPTY; // or any other observable indicating failure
        }
    }

    riderSubscribe(): Observable<any> {
        if (this.stompClient && this.stompClient.connected) {
            return new Observable(observer => {
                this.stompClient.subscribe('/user/rider', (message) => {
                    observer.next(message.body);
                });
            });
        } else {
            console.log("Stomp client is not connected")
            // Handle the case where the client is not connected
            console.error('StompClient is not connected.');
            return EMPTY; // or any other observable indicating failure
        }
    }

    disconnect() {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect(() => {
                console.log('Disconnected from WebSocket.');
                this.connectionStatusSubject.next(false);
            });
        } else {
            console.error('StompClient is not connected.');
        }
    }
}
