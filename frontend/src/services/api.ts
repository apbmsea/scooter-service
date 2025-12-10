import axios from 'axios';
import { ScooterInfo } from '../types';

const API_BASE_URL = 'http://localhost:8081';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const geolocationApi = {
  getScootersNearby: async (lat: number, lng: number, radius: number = 1000) => {
    const response = await api.get<string[]>(`/zones?lat=${lat}&lng=${lng}&radius=${radius}`);
    return response.data;
  },

  getAllScooters: async () => {
    const response = await api.get<ScooterInfo[]>('/zones/scooters/coords');
    return response.data;
  },

  checkZone: async (lat: number, lng: number) => {
    const response = await api.post<string>('/zones/check', null, {
      params: { lat, lng }
    });
    return response.data;
  },

  getScootersList: async () => {
    const response = await api.get<string[]>('/zones/scooters');
    return response.data;
  },

  updateScooterSensor: async (scooterData: any) => {
    const response = await api.post('/sensor/update', scooterData);
    return response.data;
  }
};

export default api;