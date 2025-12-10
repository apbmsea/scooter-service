export interface Request {
  method: string;
  status: string;
  message: string;
  time: string;
  service: string;
  type: 'SUCCESS' | 'WARNING' | 'ERROR';
}