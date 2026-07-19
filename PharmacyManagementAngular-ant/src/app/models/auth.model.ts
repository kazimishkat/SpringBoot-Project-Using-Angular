export interface LoginRequest {
  usernameOrEmail: string; // Updated to match backend LoginRequestDTO
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  userId: number;
  username: string; // Matches user.getUsername() payload
  fullName: string;
  email: string;
  phone: string;
  role: 'SUPER_ADMIN' | 'CUSTOMER' | 'CENTRAL_ADMIN' | 'BRANCH_MANAGER' | 'PHARMACIST' | 'SALESMAN' | 'ACCOUNTANT' | 'STOCK_KEEPER';
  branchId?: number;   // Configured for pharmacy location logic
  branchName?: string; // Configured for pharmacy location logic
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}