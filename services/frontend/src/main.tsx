import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { App } from "./App";

// Точка входа в приложение - рендерим главный компонент в DOM
createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <App />
  </StrictMode>
);
