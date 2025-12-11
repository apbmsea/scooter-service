export enum Status {
    AVAILABLE = 'AVAILABLE',
    RENTED = 'RENTED',
    MAINTENANCE = 'MAINTENANCE'
  }
  
  export enum ZoneType {
    ALLOWED = 'ALLOWED',
    FORBIDDEN = 'FORBIDDEN'
  }
  
  export interface ScooterDTO {
    id: number;
    lat: number;
    lng: number;
    batteryLevel: number;
    status: Status;
  }
  
  export interface ScooterInfo {
    id: string;
    lng: number;
    lat: number;
  }
  
  export interface ScooterUpdateEvent {
    scooterId: string;
    lat: number;
    lng: number;
    batteryLevel: number;
    status: string;
    timestamp: string;
  }
  
  export interface ParkingZone {
    id: number;
    name: string;
    type: ZoneType;
    area: any;
  }
  
  export interface ApiResponse<T> {
    data: T;
    status: number;
    message?: string;
  }