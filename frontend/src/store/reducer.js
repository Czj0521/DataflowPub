import * as Types from './actionTypes'
import {combineReducers} from 'redux'
const defaultState = {
    node:[],
    brush:false
}

function reducer(state=defaultState, action){
    let newState = JSON.parse(JSON.stringify(state))
    switch(action.type){
        case Types.ADD_NODE:
            newState.node.push(action.data)
            return newState
        case Types.SET_BRUSH:
            newState.brush = action.d
            return newState
        default:
            return state;
    }
}

// const reducers = combineReducers({
//     reducer
// })
export default reducer