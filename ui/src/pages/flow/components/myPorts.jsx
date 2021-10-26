import {useState,useEffect} from 'react'
import {WidthProvider, Responsive} from 'react-grid-layout'
import {Graph,Shape,Addon} from '@antv/x6'

import IconFont from '../../../font'
import './style.scss'

const ResponsiveReactGridLayout = WidthProvider(Responsive);

function MyPorts (props){
	
    const [items, setItems] = useState([])
    const [count, setCount] = useState(0)
    
	/*在onDrop的地方需要做的内容是：
	1. 拖入数据帧
	2. 根据拖入不同的数据帧确认不同的数据源，再根据操作符类型发送request请求
	3. 拿到请求后渲染节点（目前渲染还需要完善内容）
	*/
    const onDrop = (layout, layoutItem, _event) => {
		//console.log(props.node)
        //console.log(123)
        let data = props.node.getData()
		console.log(data)
        data.dataset = {
            ...data.dataset,
            [props.port.id]:[{
                i:count+'',
                id:new Date().getTime()+'',
                x: layoutItem.x,
                y: layoutItem.y, // puts it at the bottom
                w: 2,
                h: 1,
                isDraggable:true,
                isResizable:true,
                isBounded:true,
            }]
        }
		// data.item.option.xAxis.data = 
		// 	[
		// 		"0-10",
		// 		"10-20",
		// 		"20-30"
		// 	]
		//console.log(data.item.option.xAxis.data)
        props.node.setData(data)
		console.log(data)
        // setItems([
            
        // ])
        setCount(count+1)
    }

    const createItems = (el,index) => {
		console.log(el)
        return <div className='hetu_ports_dataset' data-grid={el} key={el.i} >
        <IconFont type='hetu-ODIyuanshujuji' style={{fontSize:19}}/>
      </div>
    }

    return <div className='myports_wrapper' id='myports_wrapper'>
        <ResponsiveReactGridLayout
            className="layout"
            cols= {{lg: 12, md: 10, sm: 6, xs: 4, xxs: 2}}
            rowHeight={24}
            useCSSTransforms={true}
            isResizable={false}
            isDroppable={true}
            isDraggable={false}
            onDrop={onDrop}
            measureBeforeMount={false}
            style={{height:'100%'}} 
            // draggableCancel='.noDraggable'
        >
            {
                props.node.getData().dataset[props.port.id] && props.node.getData().dataset[props.port.id].map((el,index)=>createItems(el,index))
            }
        </ResponsiveReactGridLayout>
        <div className='myports_after'></div>
    </div>
}

export default MyPorts