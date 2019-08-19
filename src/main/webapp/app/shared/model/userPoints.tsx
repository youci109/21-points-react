export interface UserPoints {
  userId?: string;
  upoints?: number;
  userName?: string;
  email?: string;
}

export const defaultValue: Readonly<UserPoints> = {};
