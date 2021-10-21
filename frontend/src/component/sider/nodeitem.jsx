import {HddOutlined} from '@ant-design/icons';
import {useDrag} from 'react-dnd'
import './index.scss'

const nodeIconStyle={fontSize: 16,position:'absolute',top:7,color:'#999',left:7}

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
            <div className='node-border'></div>
            <HddOutlined style={nodeIconStyle}/>
            <div className='node-content'>开始</div>
        </div>
    )
}