import React, { useState } from 'react';
import './App.css';
import SimpleMap from './components/MapComponent';
import { ScooterInfo } from './types';

const App: React.FC = () => {
  const [selectedScooter, setSelectedScooter] = useState<ScooterInfo | null>(null);
  const [zoneResult, setZoneResult] = useState('');
  

  const handleScooterClick = (scooter: ScooterInfo) => {
    setSelectedScooter(scooter);
  };

  const handleZoneCheck = (result: string) => {
    setZoneResult(result);
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <header style={{ background: '#1976d2', color: 'white', padding: '20px' }}>
        <p>Geolocation Service Frontend</p>
      </header>

      <main style={{ flex: 1, padding: '20px', maxWidth: '1200px', margin: '0 auto', width: '100%' }}>
        <div style={{ display: 'flex', gap: '20px', marginBottom: '20px', flexWrap: 'wrap' }}>
          <div style={{ flex: 1, minWidth: '300px' }}>
            <h2>Карта</h2>
            <SimpleMap 
              onScooterClick={handleScooterClick}
              onZoneCheck={handleZoneCheck}
            />
            <div style={{ marginTop: '10px', fontSize: '14px', color: '#666' }}>
              Кликните на карту для проверки зоны парковки
              <br />
              Кликните на самокат для информации
            </div>
          </div>
          
          <div style={{ width: '300px', background: '#f5f5f5', padding: '20px', borderRadius: '8px' }}>
            
            {selectedScooter && (
              <div style={{ marginTop: '15px', padding: '15px', background: 'white', borderRadius: '4px' }}>
                <h4>Самокат #{selectedScooter.id}</h4>
                <p><strong>Широта:</strong> {selectedScooter.lat.toFixed(6)}</p>
                <p><strong>Долгота:</strong> {selectedScooter.lng.toFixed(6)}</p>
              </div>
            )}
            
            {zoneResult && (
              <div style={{ marginTop: '15px', padding: '15px', background: '#e8f5e9', borderRadius: '4px' }}>
                <h4>Проверка зоны</h4>
                <p>{zoneResult}</p>
              </div>
            )}
          </div>
        </div>
        
      </main>

      <footer style={{ background: '#333', color: 'white', padding: '20px', textAlign: 'center' }}>
      </footer>
    </div>
  );
};

export default App;