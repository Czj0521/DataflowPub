import {HashRouter, Route, Switch,Redirect} from 'react-router-dom'
import FlowComponent from '../pages/flow'
import Table from '../pages/table'
import LucktSheet from '../pages/luckysheet'

export default function router(props){
    return (
        <HashRouter>
            <Switch>
                <Route path='/flow' component={FlowComponent}/>
                <Route path='/table' component={Table}/>
                <Route path='/luckysheet' component={LucktSheet}/>
                <Redirect from='/' to='/flow'/>
            </Switch>
        </HashRouter>
    )
}