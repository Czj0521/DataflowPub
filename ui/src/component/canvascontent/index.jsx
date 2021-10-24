import {useDrop} from 'react-dnd'
import { Graph } from '@antv/x6'
import {useEffect, useState} from 'react'
import {useSelector, useDispatch} from 'react-redux'
import * as actionCreator from '../../store/actionCreators'
import './index.scss'

export default function (props){
    const node = useSelector(state=>state.node)
    const dispatch = useDispatch()
    const [graph, setGraph] = useState(null)
    useEffect(()=>{
        let graph = new Graph({
            container:document.getElementById('container'),
            width:document.getElementById('container').style.width,
            height:document.getElementById('container').style.height
        })
        setGraph(graph)
    },[])
    useEffect(()=>{
        console.log(graph)
        if(graph){
            graph.addNodes(node)
            console.log(graph.model.getEdges())
        }
    },[node])
    const [collectedProps, drop] = useDrop({
        accept:['nodetype'],
        drop:(item, monitor)=>{
            const {x,y} = monitor.getClientOffset()
            const width = document.getElementById('sidemenu').offsetWidth
            const height = document.getElementById('head').offsetHeight
            // console.log(width)
            const node={
                id:new Date().getTime(),
                shape:'rect',
                x: x-width,
                y: y-height,
                width:100,
                height:40,
                ports:{
                    groups:{
                        in:{
                            position:'top',
                            attrs:{
                                circle:{
                                    r:4,
                                    magnet:true,
                                    stroke:'#31d0c6',
                                    strokeWidth:2,
                                    fill:'#fff'
                                }
                            }
                        },
                        out:{
                            position:'bottom',
                            attrs:{
                                circle:{
                                    r:4,
                                    magnet:true,
                                    stroke:'#31d0c6',
                                    strokeWidth:2,
                                    fill:'#fff'
                                }
                            }
                        }
                    },
                    items:[
                        {
                            id:'port1',
                            group:'in'
                        },
                        {
                            id:'port2',
                            group:'out'
                        },
                        {
                            id:'port3',
                            group:'out'
                        },
                        
                    ]
                }
            }
            dispatch(actionCreator.addNode(node))
        }
    })
    return(
        <div id='container' className='container' ref={drop}></div>
    )
}