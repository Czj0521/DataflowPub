import '../../components/style.scss';
import { useEffect, useState } from 'react';
import * as echarts from 'echarts';
import VirtualTable from '@/components/VirtualTable';
import IconFont from '@/font';
import TableSidebar from './tableSidebar';

function BaseComponent(props) {
  console.log('type',props);

  const [myChart, setMyChart] = useState(null);
  const [container, setContainer] = useState({});
  const [_, setBackgroundColor] = useState('white');
  const [data, setData] = useState([]);
  const [length, setLength] = useState(0);
  const [filter] = useState([]);
  const [column, setColumn] = useState([]);
  const { width, height } = props?.node?.size() || {};
  useEffect(() => {
    // 因为接口不同，所以每一个数据操作的数据返回值都要变
    if (props.node.getData().item.option.data) {
      const { column: columns } = props.node.getData().item.option;
      console.log('column', column);
      setData(props.node.getData().item.option.data);
      setColumn(columns ? columns.map((c) => ({ ...c, width: 300, align: 'center' })) : []);
      setLength(props.node.getData().item.option.data.length);
    }
  }, [props.node]);

  useEffect(() => {
    const { item } = props.node.getData();
    console.log(item);
    setData(item.option.data);
    // setColumn(item.option.column);
    setColumn(item.option.column ? item.option.column.map((c) => ({ ...c, width: 300, align: 'center' })) : []);
    if (props.type === 'dataset') return;
    const dom = document.getElementById(item.id);
    const myDom = echarts.init(dom);
    myDom.setOption(item.option);
    setMyChart(myDom);
    setContainer(dom);
    setBackgroundColor('white');
    myDom.on('brushEnd', (e) => {
      // console.log(e)
      let selectedData = '';
      const { areas } = e;
      areas.forEach((v) => {
        if (v.brushType === 'lineY') {
          selectedData += `Y: ${v.coordRange.join('~')}\n`;
        } else if (v.brushType === 'lineX') {
          selectedData = item.option.xAxis.data.slice(v.coordRange[0], v.coordRange[1] + 1);
        } else if (v.brushType === 'rect') {
          selectedData += `X: ${item.option.xAxis.data.slice(v.coordRange[0][0], v.coordRange[0][1] + 1).join(',')}\n`;
          selectedData += `Y: ${v.coordRange[1].join('~')}\n`;
        }
      });
      const nodeData = props.node.getData();
      // console.log(selectedData)
      props.node.setData({
        ...nodeData,
        select: selectedData,
        brush: false,
      });
    });
    myDom.on('globalcursortaken', (e) => {
      props.node.setData({ brush: true });
    });
    myDom.on('mousedown', (e) => {
      // console.log(e)
      props.node.setData({ brush: true });
    });
    myDom.on('mouseup', (e) => {
      // console.log(e)
      props.node.setData({ brush: false });
    });
  }, [JSON.stringify(props.node.getData().item)]);

  useEffect(() => {
    if (!container.clientHeight) return;
    if (!myChart) return;
    myChart.resize({
      width: container.clientWidth - 20,
      height: container.clientHeight - 20,
    });
  }, [container, container.clientWidth, container.clientHeight]);

  const clearSelect = () => {
    myChart.dispatchAction({
      type: 'brush',
      areas: [],
    });
    myChart.dispatchAction({
      type: 'takeGlobalCursor',
      brushOption: {
        // 参见 brush 组件的 brushType。如果设置为 false 则关闭“可刷选状态”。
        brushType: false,
        // 参见 brush 组件的 brushMode。如果不设置，则取 brush 组件的 brushMode 设置。
        brushMode: 'single',
      },
    });
    props.node.setData(
      {
        ...props.node.getData(),
        select: [],
      },
      {
        overwrite: true,
      },
    );
    const children = props.node.getChildren();
    children &&
      children.forEach((item) => {
        const filter = JSON.parse(JSON.stringify(item.getData().filter));
        delete filter[props.node.id];
        item.setData(
          {
            ...item.getData(),
            filter,
          },
          {
            overwrite: true,
          },
        );
      });
  };
  
  return (
    <div className="hetu_basecomponent_wrapper" draggable>
      <TableSidebar setColumn={setColumn} dataFrame={props.node.data.item.dataFrame} data={data} setData={setData} filter={filter} />
      <div className="hetu_basecomponent" id={props.node.getData().item.id}>
        {props.type === 'dataset' && (
          <VirtualTable
            columns={column}
            dataSource={data}
            title={() => `${length}条数据`}
            size="small"
            pagination={false}
            style={{ height: '100%', width: '100%', overFlow: 'hidden', color: 'white' }}
            scroll={{
              x: width,
              y: height - 100,
            }}
          />
        )}
        {
          props.type === 'Filter' && (
            <Filter {...props.node.getData()} />
          )
        }
      </div>
      {props.type !== 'dataset' && (
        <IconFont
          type="hetu-clearselection"
          onClick={clearSelect}
          style={{ position: 'absolute', right: 15, top: 15, zIndex: 15, color: '#bbb', cursor: 'pointer' }}
        />
      )}
    </div>
  );
}

export default BaseComponent;
