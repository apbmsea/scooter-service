import { Route, Routes, useNavigate } from "react-router-dom";
import AuthMF from "../microfrontends/AuthMF";
import HomePage from "../pages/HomePage/ui/HomePage";
import UserMF from "userMF/App";
import HeaderMF from "headerMF/App";
import "./index.css";
import { useEffect } from "react";
import { event } from "sharedMF/event";

function App() {
  const navigate = useNavigate();

  useEffect(() => {
    const unsubscribe = event.on("navigate", (data: unknown) => {
      if (
        typeof data === "object" &&
        data !== null &&
        "path" in data &&
        typeof data.path === "string"
      ) {
        navigate((data as { path: string }).path);
      } else {
        throw new Error("Invalid navigate event payload");
      }
    });

    return unsubscribe;
  }, []);

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        minHeight: "100vh",
        overflow: "hidden",
      }}
    >
      <HeaderMF />
      <div
        style={{
          flex: 1,
          minHeight: 0,
          display: "flex",
        }}
      >
        <Routes>
          <Route
            path="/home"
            element={
              <div style={{ flex: 1, display: "flex", minHeight: 0 }}>
                <HomePage />
              </div>
            }
          />

          <Route
            path="/auth/*"
            element={
              <div style={{ flex: 1, display: "flex", minHeight: 0 }}>
                <AuthMF />
              </div>
            }
          />

          <Route
            path="/users/*"
            element={
              <div style={{ flex: 1, display: "flex", minHeight: 0 }}>
                <UserMF />
              </div>
            }
          />
        </Routes>
      </div>
    </div>
  );
}

export default App;
