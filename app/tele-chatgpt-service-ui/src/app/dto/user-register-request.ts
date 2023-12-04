export interface UserRegisterRequest {
  email: string;
  password: string;
  repeatPassword: string;
  firstName: string;
  lastName: string;
  registerCode: string;
}
