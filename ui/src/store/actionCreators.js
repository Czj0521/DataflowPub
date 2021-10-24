import * as Types from './actionTypes'
export const addNode = (data) => ({
    type:Types.ADD_NODE,
    data
})

export const setBrush = (d)=>({
    type:Types.SET_BRUSH,
    d
})