import { Provider } from "react-redux";
import { UsersPage } from "../pages/UsersPage";
import "./index.css";
import { store } from "./store/store";

function App() {
  return (
    <Provider store={store}>
      <UsersPage />
    </Provider>
  );
}

export default App;
