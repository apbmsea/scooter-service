import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import { ScooterInfo } from '../types';
import { geolocationApi } from '../services/api';

delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

interface MapComponentProps {
  onScooterClick?: (scooter: ScooterInfo) => void;
  onZoneCheck?: (result: string) => void;
}

function ClickHandler({ onCheck }: { onCheck?: (lat: number, lng: number) => void }) {
  useMapEvents({
    click: (e) => {
      if (onCheck) {
        onCheck(e.latlng.lat, e.latlng.lng);
      }
    },
  });
  return null;
}


const MapComponent: React.FC<MapComponentProps> = ({ onScooterClick, onZoneCheck }) => {
  const [scooters, setScooters] = useState<ScooterInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const center: [number, number] = [56.838011, 60.597465];

  useEffect(() => {
    loadScooters();
  }, []);

  const loadScooters = async () => {
    try {
      const data = await geolocationApi.getAllScooters();
      setScooters(data);
    } catch (err) {
      console.error('Ошибка загрузки самокатов:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleMapClick = async (lat: number, lng: number) => {
    if (onZoneCheck) {
      try {
        const result = await geolocationApi.checkZone(lat, lng);
        onZoneCheck(result);
        alert(`Результат проверки зоны: ${result}`);
      } catch (err) {
        console.error(err);
      }
    }
  };

  const scooterIcon = new L.Icon({
    iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
  });

  return (
    <div style={{ height: '500px', width: '100%', position: 'relative' }}>
      {loading && (
        <div style={{ 
          position: 'absolute', 
          zIndex: 1000, 
          top: '10px', 
          left: '10px', 
          background: 'white', 
          padding: '10px',
          borderRadius: '4px',
          boxShadow: '0 2px 5px rgba(0,0,0,0.2)'
        }}>
          Загрузка самокатов...
        </div>
      )}
      
      <MapContainer 
        center={center} 
        zoom={13} 
        style={{ height: '100%', width: '100%' }}
      >
        <TileLayer
  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
  attribution="" 
        />
        
        <ClickHandler onCheck={handleMapClick} />
        
        {scooters.map((scooter) => (
          <Marker
            key={scooter.id}
            position={[scooter.lat, scooter.lng] as [number, number]}
            icon={scooterIcon}
            eventHandlers={{
              click: () => onScooterClick && onScooterClick(scooter),
            }}
          >
            <Popup>
              <div>
                <strong>Самокат #{scooter.id}</strong>
                <br />
                Координаты: 
                <br />
                {scooter.lat.toFixed(6)}, {scooter.lng.toFixed(6)}
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
      
      <div style={{ 
        position: 'absolute', 
        bottom: '10px', 
        right: '10px', 
        zIndex: 1000 
      }}>
        <button 
          onClick={loadScooters}
          style={{ 
            padding: '10px 15px',
            margin: '0px 0px -10px 0px', 
            background: '#1976d2', 
            color: 'white', 
            border: 'none', 
            borderRadius: '4px',
            cursor: 'pointer'
          }}
        >
          Обновить ({scooters.length})
        </button>
      </div>
    </div>
  );
};

export default MapComponent;