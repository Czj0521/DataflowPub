import React,{useEffect, useState} from 'react'
import {Table} from 'antd'
import * as echarts from 'echarts'

import IconFont from '../../../font'
import TableSidebar from './tableSidebar'
import axios from 'axios'
import './style.scss'


function BaseComponent(props){
			console.log('item:',props.node.getData().item)
	
	//console.log(props.node.getData().item.option.column)
    const [myChart, setMyChart] = useState(null)
    const [container, setContainer] = useState({})
    const [backgroundColor, setBackgroundColor] = useState('white')
	const [data,setData] = useState([])
	const [length,setLength] = useState(0)
	const [filter,setFilter] = useState([])
	const [column,setColumn] = useState([])
    // console.log(props)
	
	var data1 = []
	useEffect(() => {
		//因为接口不同，所以每一个数据操作的数据返回值都要变
		if(!!props.node.getData().item.option.data)
		{
		setData(props.node.getData().item.option.data)
		setColumn(props.node.getData().item.option.column)
		setLength(props.node.getData().item.option.data.length)
		}
		},[])
	

	
    useEffect(()=>{
        const item = props.node.getData().item
        if(props.type === 'dataset') return
        const dom = document.getElementById(item.id)
        const myDom = echarts.init(dom)
        myDom.setOption(item.option)
        setMyChart(myDom)
        setContainer(dom)
        setBackgroundColor('white')
        // myDom.on('brush',(e)=>{
        //     props.node.setData({brush:true})
        // })
        // myDom.on('brushselected',(e)=>{
        //     props.node.setData({brush:true})
        //     console.log(e)
        // })
        myDom.on('brushEnd',(e)=>{
            // console.log(e)
            let selectedData = ''
            const areas = e.areas
            areas.forEach((v)=>{
                if(v.brushType === 'lineY'){
                    selectedData += 'Y: ' + v.coordRange.join('~') + '\n'
                }else if(v.brushType === 'lineX'){
                    selectedData = item.option.xAxis.data.slice(v.coordRange[0],v.coordRange[1]+1)
                }else if(v.brushType === 'rect'){
                    selectedData += 'X: ' + item.option.xAxis.data.slice(v.coordRange[0][0],v.coordRange[0][1]+1).join(',') + '\n'
                    selectedData += 'Y: ' + v.coordRange[1].join('~') + '\n'
                }
            })
            const data = props.node.getData()
            // console.log(selectedData)
            props.node.setData({
                ...data,
                select:selectedData,
                brush:false
            })
        })
        myDom.on('globalcursortaken',(e)=>{
            props.node.setData({brush:true})
        })
        myDom.on('mousedown',(e)=>{
            // console.log(e)
            props.node.setData({brush:true})
        })
        myDom.on('mouseup',(e)=>{
            // console.log(e)
            props.node.setData({brush:false})
        })
    },[JSON.stringify(props.node.getData().item)])

    useEffect(()=>{
        if(!container.clientHeight) return
        if(!myChart) return
        myChart.resize({
            width: container.clientWidth - 20,
            height: container.clientHeight - 20,
        })
        
    },[container,container.clientWidth,container.clientHeight])

    const clearSelect = () => {
        myChart.dispatchAction({
            type:'brush',
            areas:[]
        })
        myChart.dispatchAction({
            type: 'takeGlobalCursor',
            brushOption: {
                // 参见 brush 组件的 brushType。如果设置为 false 则关闭“可刷选状态”。
                brushType: false,
                // 参见 brush 组件的 brushMode。如果不设置，则取 brush 组件的 brushMode 设置。
                brushMode: 'single'
            }
        })
        props.node.setData({
            ...props.node.getData(),
            select:[]
        },{
            overwrite:true
        })
        const children = props.node.getChildren()
        children && children.forEach(item=>{
            let filter = JSON.parse(JSON.stringify(item.getData().filter))
            delete filter[props.node.id]
            item.setData({
                ...item.getData(),
                filter
            },{
                overwrite:true
            })
        })
    }
	
    return <div className='hetu_basecomponent_wrapper' draggable={true}>
        <div className='hetu_basecomponent' id={props.node.getData().item.id}>
            {
                props.type === 'dataset' && 
                <Table 
                    columns={column}
                    dataSource={data} 
					title={()=>length+'条数据'}
                    size='small' 
                    style={{height:'100%',width:'100%',overflow:'auto',backgroundColor:'transparent',color:'white'}}
                    sticky={true}
                />
            }
        </div>
            {
                props.type !== 'dataset' && 
                <IconFont type='hetu-clearselection' onClick={clearSelect}
                    style={{position:'absolute',right:15, top:15,zIndex:15,color:'#bbb',cursor:'pointer'}}/>
            }
            <TableSidebar setColumn={setColumn} data={data} setData={setData} filter={filter}/>
    </div>
    
}

export default BaseComponent