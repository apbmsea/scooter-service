import { Provider } from "react-redux";
import { UserPage } from "@pages/UserPage";
import { Theme, presetGpnDefault } from "@consta/uikit/Theme";

import { store } from "./store/store";

function App() {
  return (
    <Theme style={{ height: "100%", width: "100%" }} preset={presetGpnDefault}>
      <Provider store={store}>
        <UserPage />
      </Provider>
    </Theme>
  );
}
export default App;
