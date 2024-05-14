export class User {
  constructor(
    public id: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public type: string,
    public token?: string,
    public userTypeId?: string
  ) {}
}
