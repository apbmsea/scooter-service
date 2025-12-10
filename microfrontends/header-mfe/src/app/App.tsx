import { Provider } from 'react-redux';
import Header from '../widgets/Header/Header';
import { store } from './store/store';

function App() {
	return (
		<Provider store={store}>
			<Header />
		</Provider>
	);
}
export default App;
