export default(props)=>({
    xAxis: {
        type: 'category',
        data: props.xData
    },
    yAxis: {
        type: 'value'
    },
    series: [{
        data: props.yData,
        type: 'line'
    }]
})