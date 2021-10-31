import { useEffect, useState } from 'react';

import axios from 'axios';

import { Addon, Markup } from '@antv/x6';
import { ReactShape } from '@antv/x6-react-shape';

import { getTable, getTableColumn } from '../../api/table';
import IconFont from '../../font';
import data from './data';
import BaseComponent from './operators/dataset/baseComponent';
import Transpose from './operators/transpose';
import Filter from './operators/Filter';
import store from '@/store';
import { Join } from './operators';


const { Dnd } = Addon;

function DatasetSide(props) {
  const [dnd, setDnd] = useState(null);
  const [openKey, setOpenKey] = useState('1');
  const [isOpen, setIsOpen] = useState(false);
  const [size, setNodeSize] = useState({});
  // const [jobId, setJobId] = useState('');
  // const [barData, setBarData] = useState({});
  // const [loading, setLoading] = useState(true);
  const [filterState, filterDIspatch] = store.useModel('filter');
  console.log('side filterState', filterState);
  useEffect(() => {
    filterDIspatch.getAllOperationFn();
  }, []);
  useEffect(() => {
    console.log('effect执行了');
    const d = new Dnd({
      target: props.graph,
      scaled: false,
      animation: true,
      getDropNode(draggingNode, options) {
        console.log(options);
        return draggingNode.getData().dropNode;
      },
      // validateNode(droppingNode, options) {
      //   console.log(droppingNode, options);
      // },
    });
    console.log(d);
    setDnd(d);
  }, [props.graph]);

  const chooseOperator = (type) => {
    switch (type) {
      case 'Table':
        return <BaseComponent type={type} size={size} />;
      case 'Join':
        return <Join />;
      case 'Filter':
        return <Filter type={type} size={size} />;
      case 'dataset':
        return <BaseComponent type={type} size={size} />;
      case 'Transpose':
        return <Transpose type={type} size={size} />;
      default:
        return <div />;
    }
  };
  
  const drop = (e) => {
    console.log('drop', e, props);
    const { graph } = props;
    const target = e.currentTarget;
    const type = target.getAttribute('data-type');
    const dataSource = target.getAttribute('data-datasource');
    const parent = target.getAttribute('parent');
    // console.log(parent)
    console.log('type', type);
    const item = {};
    const node = graph.createNode({
      width: 400,
      height: 300,
      shape: 'react-shape',
      data: {
        brush: false,
        dataset: {},
        item: {
          dataFrame: dataSource, // 输入槽数据帧
          id: `${new Date().getTime()}`,
          option: {
            data: [],
            column: [],
          },
        },
        move: true,
      },
      // component: <BaseComponent  type={type} size={size}/>,
      component: chooseOperator(type),
      portMarkup: [
        Markup.getForeignObjectMarkup(),
        {
          tagName: 'circle',
          selector: 'portBody',
        },
      ],
      attrs: {
        body: {
          fill: 'rgb(40,40,40)',
          // fill: 'transparent',
          stroke: '#000',
        },
      },
      ports: {
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
                width: 24,
                height: 24,
                fill: 'tranaparent',
                y: -30,
              },
            },
            zIndex: 10,
          },
          out: {
            position: {
              name: 'absolute',
              args: { x: '105%', y: '98%' },
            },
            attrs: {
              portBody: {
                magnet: true,
                r: 10,
                fill: 'rgb(40,40,40)',
                // y: 40
              },
            },
          },
        },
      },
    });
    const dragNode = graph.createNode({
      width: 24,
      height: 24,
      shape: 'react-shape',
      component: (
        <span draggable>
          <IconFont type="hetu-ODIyuanshujuji" style={{ fontSize: 18 }} />
        </span>
      ),
      data: {
        dropNode: node,
      },
    });
    console.log('e.nativeEvent', e);
    dnd.start(dragNode, e.nativeEvent);
    // type这里是div的属性，我们要取到属性值data-type判断是哪类operator
    if (type === 'dataset') {
      const tableDescription = {
        dataSource: 'dataflow.airuuid',
        limit: 2000,
        project: ['*'],
      };
      axios
        .all([
          getTable(tableDescription),
          getTableColumn({ datasource: 'dataflow.airuuid' }),
        ])
        .then(
          axios.spread((res1, res2) => {
            console.log('表数据：', res1);
            console.log('列数据：', res2);
            const column = [];
            Object.keys(res2).forEach((val) => {
              column.push({
                key: val,
                dataIndex: val,
                title: val,
              });
            });
            console.log(column);
            const nodeData = node.getData();
            nodeData.item.option.data = res1;
            nodeData.item.option.column = column;
            node.setData(nodeData);
          }),
        );
    } else if (type === 'table') {
      // const promiseData = {
      //   job: "start_job",
      //   jobType: "PivotChart",
      //   jobDescription: {
      //     input: "airuuid", //后面要变成动态的
      //     axisDimension: "1",
      //     mark: "bar",
      //     operations: [
      //       {
      //         type: "x-axis",
      //         operation: {
      //           attribute: parent,
      //           binning: "none",
      //           aggregation: type,
      //           sort: "none",
      //         },
      //       },
      //     ],
      //     output: "createView",
      //   },
      //   jobId: "1212",
      //   workspaceId: "4f5s4f25s4g8z5eg",
      // };
      // test(promiseData).then((res) => {
      //   console.log(fieldType[type]);
      //   if (fieldType[type] === "number") {
      //     item = {
      //       id: new Date().getTime() + "",
      //       option: barOption({
      //         xAxisData: data.map((item) => item[type]),
      //         data: data.map((item) => item[type]),
      //         xAxis: type,
      //         title: parent + "-" + type,
      //         xAxisType: "value",
      //       }),
      //     };
      //   } else {
      //     const s = new Set(data.map((item) => item[type]));
      //     const obj = {};
      //     const arr = [];
      //     for (
      //       var i = 0;
      //       i < res.responseJobInfor.responseValues[0].axisCalibration.length;
      //       i++
      //     ) {
      //       arr.push(
      //         res.responseJobInfor.responseValues[0].axisCalibration[
      //           i
      //         ].toString()
      //       );
      //     }
      //     item = {
      //       id: new Date().getTime() + "",
      //       option: barOption({
      //         xAxisData: arr,
      //         data: res.responseJobInfor.responseValues[0].height,
      //         xAxis: type,
      //         title: parent + "-" + type,
      //         xAxisType: "category",
      //       }),
      //     };
      //   }
      // });
    }
  };
  const openMenu = (key) => {
    setIsOpen(true);
    setOpenKey(key);
  };

  const handleDragStart = (event, dataSource, iconName) => {
    event.dataTransfer.setData('dragItem', JSON.stringify({ dataSource, iconName }));
  };

  return (
    <div className="hetu_side">
      <div className="hetu_side_title">
        <span />
        <span />
      </div>
      <div>
        <div key={'1'} className="hetu_side_dataset">
          <span
            className="hetu_side_item"
            draggable
            onMouseDown={drop}
            data-type="dataset"
            data-datasource={'dataflow.airuuid'}
          >
            airuuid.csv
          </span>
          <span className="hetu_side_item_logo" draggable onDragStart={(e) => handleDragStart(e, 'dataflow.airuuid', 'hetu-ODIyuanshujuji')}>
            <IconFont type="hetu-ODIyuanshujuji" style={{ fontSize: 18 }} />
          </span>
          <span
            className="hetu_side_item_dropdown"
            onClick={() => openMenu('1')}
          >
            <IconFont type="hetu-xiala" />
          </span>
        </div>
        {isOpen && openKey === '1' && (
          <div className="hetu_side_item_attrs">
            {data.map((item) => {
              if (item !== 'name' && item !== 'key') {
                return (
                  <div key={item} className="hetu_side_item_attrwrapper">
                    <span
                      className="hetu_side_item_attr"
                      draggable
                      data-type={item}
                      onMouseDown={drop}
                    >
                      {item}
                    </span>
                    <br />
                  </div>
                );
              }
              return null;
            })}
          </div>
        )}
        <button onClick={(e) => console.log(props.graph.toJSON())}>get Graph data </button>
      </div>
    </div>
  );
}

class BaseNode extends ReactShape {
  // eslint-disable-next-line class-methods-use-this
  isGroup() {
    return false;
  }
}

export default DatasetSide;
