import {useEffect, useState, useRef} from 'react'
import {Layout,Modal} from 'antd'
import HeaderMenu from '../../component/header/headermenu'
import ChartForm from './chartForm'
import pieConfig from './pieConfig'
import lineConfig from './lineConfig'
import ChartCom from './chart'
import * as echarts from 'echarts'
import './index.scss'

const {Header,Content} = Layout

export default function LuckySheet (props){

    const [visible, setVisible] = useState(false)
    const [visible1, setVisible1] = useState(false)
    const [data, setData] = useState({})
    const [arr,setArr] = useState([])
    const [option,setOption] = useState({})
    const formRef = useRef();

    useEffect(()=>{
        const initData = [[1,2,3,4,5,6],[7,8,9,10,11,12],[13,14,15,16,17,18,19],[20,21,22,23,24,25]]
        const options = {
            container: 'luckysheet', 
            title: 'Luckysheet Demo', 
            lang: 'zh', 
            data:[{ "name": "Sheet1", color: "", "status": "1", "order": "0", "data": [], "config": {}, "index":0, }, { "name": "Sheet2", color: "", "status": "0", "order": "1", "data": [], "config": {}, "index":1 }, { "name": "Sheet3", color: "", "status": "0", "order": "2", "data": [], "config": {}, "index":2 }],
            plugins: ['chart'],
            cellRightClickConfig: {
                copy: false, // copy
                copyAs: false, // copy as
                paste: false, // paste
                insertRow: false, // insert row
                insertColumn: false, // insert column
                deleteRow: false, // delete the selected row
                deleteColumn: false, // delete the selected column
                deleteCell: false, // delete cell
                hideRow: false, // hide the selected row and display the selected row
                hideColumn: false, // hide the selected column and display the selected column
                rowHeight: false, // row height
                columnWidth: false, // column width
                clear: false, // clear content
                matrix: false, // matrix operation selection
                sort: false, // sort selection
                filter: false, // filter selection
                chart: false, // chart generation
                image: false, // insert picture
                link: false, // insert link
                data: false, // data verification
                cellFormat: false // Set cell format
            }
        
        }
        const data = window.luckysheet.transToCellData(initData)
        options.data[0].celldata = data
        window.luckysheet.create(options)
        let d = window.luckysheet.getSheetData(0)   
        // window.luckysheet.transToData(d)    
        // console.log()
        document.onclick=(e)=>{
            const chart = document.getElementById('chart')
            chart.style.display = 'none'
        }
        document.getElementById("luckysheet-cell-main").oncontextmenu=(e)=>{
            // console.log(e)
            let data = window.luckysheet.getRangeArray("twoDimensional")
            // let position = window.luckysheet.
            data = data.filter(arr=>{
                return arr.filter(i=>i!=null).length!==0
            })
            setArr(data)
            const chart = document.getElementById('chart')
            chart.style.left = e.clientX + 'px'
            chart.style.top = e.clientY + 'px'
            chart.style.display = 'block'
        }
    },[])
    useEffect(()=>{
        if(JSON.stringify(data)!=='{}'){

            renderChart()
        }
    },[data])
    useEffect(()=>{
        if(visible1){
            let dom = document.getElementById('echarts')
            console.log(visible1,dom)
            let myChart = echarts.init(dom)
            myChart.setOption(option)
        }
    },[option])

    const down = () => {
        return false
    }
    const selectChart = () => {
        setVisible(true)
    }
    const handleCancel = () => {
        setVisible(false)
        JSON.stringify(formRef.current.values)!=='{}' && getData()
    }

    const  getData = () => {
        const {values} = formRef.current
        switch(values.type){
            case 'pie':
                let value = arr.map(item=>({value:item[parseInt(values.value)-1]}))
                setData({value})
                return
            case 'line':
                let xData = arr.map(item=>item[parseInt(values.xData)-1])
                let yData = arr.map(item=>item[parseInt(values.yData)-1])
                setData({xData,yData})
                return
        }
    }

    const renderChart = () => {
        let option = getOption()
        setVisible1(true)
        setOption(option)
    }
    const getOption = () => {
        switch(formRef.current.values.type){
            case 'pie':
                return pieConfig(data)
            case 'line':
                return lineConfig(data)
        }
    }

    const onCancel = () =>{
        setVisible1(false)
    }

    return (
        <Layout>
            <Header>
                <HeaderMenu/>
            </Header>
            <Content>
                <div id='luckysheet' style={{height:'calc(100vh - 64px)'}}></div>
                <div id='chart'>
                    <ul>
                        <li onClick={selectChart}>图表生成</li>
                    </ul>
                </div>
            </Content>
            <Modal
                title='选择图类型'
                visible={visible}
                footer={null}
                onCancel={handleCancel}
            >
                <ChartForm ref={formRef} handleCancel={handleCancel}/>
            </Modal>
            <Modal
                title='图表展示'
                visible={visible1}
                footer={null}
                onCancel={onCancel}
            >
                <div id='echarts' style={{height:400,width:500}}></div>
            </Modal>
        </Layout>
    )
}