import "./index.css";
import { Provider } from "react-redux";
import { store } from "./store/store";
import { RequestsPage } from "@pages/RequestsPage";

function App() {
  return <Provider store={store}><RequestsPage/></Provider>;
}

export default App;
