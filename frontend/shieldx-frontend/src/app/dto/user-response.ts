export class UserResponse {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  mobile: string;
  address: string;
  status: 'active' | 'deactivate';

  constructor(
    userId: number,
    firstName: string,
    lastName: string,
    email: string,
    mobile: string,
    address: string,
    status: 'active' | 'deactivate'
  ) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.mobile = mobile;
    this.address = address;
    this.status = status;
  }
}
