import React from 'react';
import './LoadingWidget.css';

interface LoadingWidgetProps {
  active?: boolean;
}

const LoadingWidget: React.FC<LoadingWidgetProps> = ({ active = true }) => {
  if (!active) return null;

  return (
    <div className="loading-widget">
      <div className="spinner"></div>
    </div>
  );
};

export default LoadingWidget;
