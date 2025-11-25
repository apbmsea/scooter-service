export interface HandledErrorData {
  message: string;
  errors: Record<string, string>;
}

export interface HandledError {
  status: number;
  data: HandledErrorData;
}

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  role: "USER" | "OPERATOR" | "ADMIN";
  createdAt: string;
  updatedAt: string;
}
