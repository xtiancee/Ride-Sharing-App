

export class RideRequest {
    constructor(
        public id: string,
        public riderName: string,
        public fair: string,
        public source: string,
        public destination: string,
    ) {}
}
