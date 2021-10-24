import * as echarts from 'echarts'
import {useEffect} from 'react'

export default (props) => {

    useEffect(()=>{
        let dom = document.getElementById('echarts')
        let myChart = echarts.init(dom)
        console.log(props.option)
        myChart.setOption(props.option)
    },[...Object.values(props)])

    return (
        <div id='echarts' style={{height:400,width:500}}></div>
    )
}