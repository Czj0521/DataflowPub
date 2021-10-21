import Router from './router'
import { DndProvider } from 'react-dnd';
import {HTML5Backend } from 'react-dnd-html5-backend'
import {Provider} from 'react-redux'
import store from './store'
import 'antd/dist/antd.css'
import './App.css'

function App() {
  return (
    <Provider store={store}>
      <DndProvider backend={ HTML5Backend  }>
        <div className="App" >
          <Router/>
        </div>
      </DndProvider>
    </Provider>
  );
}

export default App;
