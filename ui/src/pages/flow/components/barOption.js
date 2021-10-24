export default (props) => {
    console.log(props,props.data)
    //1.计算x轴范围(mod 10之后的范围)
    // let min=Math.ceil(Math.min(...props.xAxisData)/10),max=parseInt(Math.max(...props.xAxisData)/10)
    // console.log(min,max)
    // let arr=[],x_data=[];
    // //2.初始化x轴数组
    // for(let i =min,j=0;i<=max;i++,j++){
    //     arr[j]=0;
    //     x_data[j]=i*10;
    // }
    // //3.将原有数据归一化到新的x轴，柱状图 10表示1-10
    // props.data&&props.data.map(item=>{
    //     for(let i = min,j=0; i <= max; i ++,j++){
    //         if((item/10)>(i-1)&&((item/10)<=(i))){
    //             arr[j]++;
    //         }
    //     }
    // })

    // console.log(arr)
    return {
        // legend: {
        //     data: ['bar', 'bar2', 'bar3', 'bar4'],
        //     left: '10%'
        // },
        title:{
            text:props.title,
            textStyle:{
                color:'#bbb'
            }
        },
        toolbox:{
            iconStyle:{
                color:'#bbb',
                borderColor:'#bbb'
            }
        },
        brush: {
            // toolbox: ['rect',  'lineX', 'lineY', 'keep', 'clear'],
            toolbox: ['lineX'],
            xAxisIndex: 0
        },
        
        // dataZoom: [
        //     {
        //         show: true,
        //         start: 94,
        //         end: 100
        //     }
        // ],
        // toolbox: {
        //     feature: {
        //         magicType: {
        //             type: ['stack', 'tiled']
        //         },
        //         dataView: {}
        //     }
        // },
        tooltip: {},
        xAxis: {
            data: props.xAxisData,
            name: props.xAxis,
            type:'category',
            axisLine: {onZero: true},
            splitLine: {show: false},
            splitArea: {show: false},
            axisLabel:{
                color:'#bbb',
                width: 50,
                overflow: "breakAll",
                interval:0,
                // align:'left'
            },
            nameTextStyle:{
                color:'#bbb',
            },
        },
        yAxis: {
            axisLabel:{
                color:'#bbb'
            },
            nameTextStyle:{
                color:'#bbb'
            },
            splitLine:{
                // show:false,
                lineStyle:{
                    color:"#666"
                },
               
            }
        },
        grid: {
            bottom:40
        },
        series: [
            {
                itemStyle:{
                    color:"#809fd5"
                },
                name: 'bar',
                type: 'bar',
                stack: 'one',
                // barCategoryGap: "0%",
                data: props.data
            },
        ]
    }
}