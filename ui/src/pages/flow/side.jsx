import React, {useEffect,useState} from 'react'
import {Dom, Addon,Markup} from '@antv/x6'
import {ReactShape} from '@antv/x6-react-shape'

import IconFont from '../../font'
import BaseComponent from './components/baseComponent';
import barOption from './components/barOption';
import data from './data'
import axios from 'axios'
import {test,getTable,getTableColumn} from '../../api/table.js'
const { Dnd } = Addon
  
 

const fieldType = {
  Age:'number',
  Married:'string',
  EducationLevel:'string',
  Employed:'string',
  CreditScore:'number'
}

var column = []
		  
function DatasetSide(props){
	
  const [dnd, setDnd] = useState(null)
  const [openKey, setOpenKey] = useState('1')
  const [isOpen, setIsOpen] = useState(false)
  const [size, setNodeSize] = useState({})
  const [jobId,setJobId] = useState('')
  const [barData,setBarData] = useState({})

    useEffect(()=>{
		console.log('effect执行了')
      const d = new Dnd({
        target: props.graph,
        scaled: false,
        animation: true,
        getDropNode(draggingNode, options){
          console.log(options)
          return draggingNode.getData().dropNode
        },
        validateNode(droppingNode, options){
          console.log(droppingNode,options)
        }
    })
	console.log(d)
    setDnd(d)
    },[props.graph])

	const chartOption = (item,type,graph,e)=>{
		const node = graph.createNode({
		  width: 300,
		  height: 300,
		  shape:'react-shape',
		  data:{
		    brush:false,
		    dataset:{},
		    item,
		    type,
		    move:true
		  },
		  component: <BaseComponent  type={type} size={size}/>,
		  portMarkup: [Markup.getForeignObjectMarkup(),{
		      tagName: 'circle',
		      selector: 'portBody',
		    }],
		  attrs: { 
		    body: {
		      fill: 'rgb(40,40,40)',
		      // fill: 'transparent',
		      stroke: '#000',
		    },
		  },
		  ports:{
		    items: [
		      {
		        group: 'out',
		      },
		      {
		        group: 'in',
		      },
		      {
		        group: 'in',
		      },
		    ],
		    groups: {
		      in: {
		        position: {
		          name: 'top',
		        },
		        attrs: {
		          fo: {
		            magnet: 'passive',
		            width:24,
		            height:24,
		            fill: 'tranaparent',
		            y: -30
		          },
		        },
		        zIndex:10
		      },
		      out: {
		        position: {
		          name: 'absolute',
		          args: { x: '105%', y: '98%' }
		        },
		        attrs: {
		          portBody: {
		            magnet: true,
		            r:10,
		            fill: 'rgb(40,40,40)',
		            // y: 40
		          },
		        },
		      },
		    },
		  },
		  // portMarkup: [Markup.getForeignObjectMarkup()],
		  // portMarkup: [{
		  //   tagName: 'circle',
		  //   selector: 'portBody',
		  // },],
		  
		})
		const dragNode = graph.createNode({
		  width:24,
		  height:24,
		  shape:'react-shape',
		  component: <span draggable={true}><IconFont type='hetu-ODIyuanshujuji' style={{fontSize:18}}/></span>,
		  data:{
		    dropNode:node
		  }
		})
		console.log('e.nativeEvent',e)
		dnd.start(dragNode,e.nativeEvent)
	}

    const drop = (e) => {
       console.log(e)
      const {graph} = props
      const target = e.currentTarget
      const type = target.getAttribute('data-type')
	  const parent = target.getAttribute('parent')
	  // console.log(parent)
	  // console.log(type)
      let item = {}
	  
      if(type === 'dataset'){
		  const tableDescription = {
				  dataSource: "databoard_gluttony.test2",
				  limit: 2000,
				  project: [
					"*"
				  ]
				}
		  axios.all([getTable(tableDescription),getTableColumn({datasource:'databoard_gluttony.test2'})]).then(
		  axios.spread((res1, res2)=>{
			  console.log('表数据：',res1)
			  console.log('列数据：',res2)
			  Object.keys(res2).map(val=>{
				  column.push({
					  key:val,
					  dataIndex:val,
					  title:val
				  })
			  })
			  item =  {
			    id: new Date().getTime()+'',
			    option: {
			      column,
			      data:res1
			    }
			  }
			  console.log(item)
			  chartOption(item,type,graph,e)
		  }
		  ))
		  
      }else{
		  const promiseData = {
						  job: "start_job",
						  jobType: "PivotChart",
						  jobDescription: {
							input: "airuuid",//后面要变成动态的
							axisDimension: "1",
							mark: "bar",
							operations: [
							  {
							  type: "x-axis",
							   operation: {  attribute: parent,
							                        binning: "none",
							                        aggregation: type,
							                        sort: "none"
							                        }
							  }
							],
							output: "createView"
						  },
						  jobId:"1212",
						  workspaceId: "4f5s4f25s4g8z5eg"
						}
			test(promiseData).then(res =>{
					  console.log(res)
					  if(fieldType[type] === 'number'){
					    item = {
					      id: new Date().getTime()+'',
					      option: barOption({
					        xAxisData: data.map(item=>item[type]),
					        data: data.map(item=>item[type]),
					        xAxis:type,
					        title:parent+'-'+type,
					        xAxisType:'value'
					      })
					    }
					  }else{
					    const s = new Set(data.map(item=>item[type]))
					    const obj = {}
					  		const arr = []
					  		for(var i=0;i<res.responseJobInfor.responseValues[0].axisCalibration.length;i++)
					  		{
					  			arr.push(res.responseJobInfor.responseValues[0].axisCalibration[i].toString())
					  		}
					    item = {
					      id: new Date().getTime()+'',
					      option: barOption({
					        xAxisData: arr,
					        data: res.responseJobInfor.responseValues[0].height,
					        xAxis:type,
					        title:parent+'-'+type,
					        xAxisType:'category'
					      })
					    }
					  }
					  chartOption(item,type,graph,e)
				  })
      }
    }
    const openMenu = (key) => {
      setIsOpen(true)
      setOpenKey(key)
    }
  
    return (
        <div className='hetu_side'>
            <div className='hetu_side_title'>
                <span></span>
                <span></span>
            </div>
            <div>
              <div key={'1'} className='hetu_side_dataset'>
                <span className='hetu_side_item'  draggable={true} onMouseDown={drop} data-type='dataset'>airuuid.csv</span>
                <span className='hetu_side_item_logo' draggable={true}>
                  <IconFont type='hetu-ODIyuanshujuji' style={{fontSize:18}}/>
                </span>
                <span className='hetu_side_item_dropdown' onClick={()=>openMenu('1')}><IconFont type='hetu-xiala'/></span>
              </div>
              {
                isOpen && openKey === '1' && 
                <div className='hetu_side_item_attrs'>
                  {
                    Object.keys(data[0]).map(item=>{
                      if(item!=='name'&&item!=='key'){
                        return (
                          <div key={item} className='hetu_side_item_attrwrapper'>
                            <span className='hetu_side_item_attr' draggable={true} data-type={item} >{item}</span>
							<br/>
							{
								Object.keys(data[0][item]).map(val =>{
									return  <span className='hetu_side_item_attr_children' draggable={true} parent={item} data-type={item,val} onMouseDown={drop}>{val}</span>
								})
							}
                          </div>
                        )
                      }
                    })
                  }
                </div>
              }
            </div>
        </div>
    )
}

class BaseNode extends ReactShape {
  // eslint-disable-next-line class-methods-use-this
  isGroup() {
    return false
  }
}

export default DatasetSide