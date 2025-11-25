import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthMF from "../microfrontends/AuthMF";
import HomePage from "../microfrontends/HomePage";
import UserMF from "userMF/App";
import HeaderMF from "headerMF/App";
import './index.css'

function App() {
  return (
    <BrowserRouter>
      <HeaderMF />
      <Routes>
        <Route path="/home" element={<HomePage />} />
        <Route path="/auth/*" element={<AuthMF />} />
        <Route path="/users/*" element={<UserMF />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
