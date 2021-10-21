import {useDrag} from 'react-dnd'
import './index.scss'

export default function NodeItem(props){

    const [{isDragging},drag] = useDrag({
        type:'nodetype',
        item:{type:'nodetype'},
        collect:(monitor)=>({
            isDragging:monitor.isDragging()
        })
    })
    
    return(
        <div className='node-item' ref={drag} style={{opacity:isDragging ? 0.5 : 1}}>
            {props.children}
        </div>
    )
}