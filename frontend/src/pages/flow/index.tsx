import './index.scss';

import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';

import { Col, Row } from 'antd';

import { Addon, Graph, Shape } from '@antv/x6';

import CanvasContent from './canvasContent';
import barOption from './components/barOption';
import MyPorts from './components/myPorts';
import data from './data';
import DatasetSide from './side';


const fieldType = {
  Age: 'number',
  Married: 'string',
  EducationLevel: 'string',
  Employed: 'string',
  CreditScore: 'number',
};

export default function FlowComponent(props) {
  const [graph, setGraph] = useState(null);
  const [brush, setBrush] = useState(false);

  useEffect(() => {
    const g = new Graph({
      container: document.getElementById('hetu_canvas'),
      // grid: false,
      autoResize: true,
      scroller: {
        enabled: true,
      },
      minimap: {
        enabled: true,
        container: document.getElementById('miniMap'),
      },
      resizing: {
        enabled: true,
        minWidth: 200,
        minHeight: 200,
      },
      panning: true,
      mousewheel: {
        enabled: true,
        modifiers: ['ctrl', 'meta'],
      },
      onPortRendered(args) {
        // console.log(args)
        const selectors = args.contentSelectors;
        const container = selectors && selectors.foContent;
        if (container) {
          if (args.port.group === 'in') {
            ReactDOM.render(<MyPorts node={args.node} port={args.port} graph={graph} />, container);
          } else {
            // ReactDOM.render(
            //   <div node={args.node} port={args.port}>123</div>,
            //   container,
            // )
          }
        }
      },
      connecting: {
        snap: true,
        allowBlank: false,
        allowLoop: false,
        allowNode: false,
        highlight: true,
        connector: 'rounded',
        connectionPoint: 'boundary',
        router: {
          name: 'er',
          args: {
            direction: 'V',
          },
        },
        createEdge() {
          return new Shape.Edge({
            attrs: {
              line: {
                stroke: '#a0a0a0',
                strokeWidth: 1,
                targetMarker: {
                  name: 'classic',
                  size: 7,
                },
              },
            },
          });
        },
        validateEdge({ edge, type, previous }) {
          // console.log(edge, type, previous)
          const target = g.getCellById(edge.target.cell);
          const source = g.getCellById(edge.source.cell);
          const children = source.getChildren();
          const current = target.getData();
          children
            ? source.setChildren([...children, target])
            : source.setChildren([target]);

          // target.setData({
          //   ...current,
          //   parent: current.parent ? [...current.parent,source] : [source]
          // })
          if (
            source.getData().select &&
            JSON.stringify(source.getData().select) !== '{}'
          ) {
            // const current = target.getData()
            target.setData({
              ...current,
              filter: current.filter ? {
                ...current.filter,
                [source.id]: {
                  type: source.getData().type,
                  select: source.getData().select,
                },
              } : {
                  [source.id]: {
                    type: source.getData().type,
                    select: source.getData().select,
                  },
                },
            });
          }
          // console.log(target,source)
          return true;
        },
        validateConnection({ sourceView, targetView, targetMagnet }) {
          return true;
        },
      },
      interacting(cellView) {
        if (
          cellView.cell.getData() &&
          (cellView.cell.getData().brush || !cellView.cell.getData().move)
        ) {
          // console.log(cellView.cell.getData().move)
          return false;
        }
        return true;
      },
      allowPanning: (e) => {
        // console.log(e)
        if (
          e.target.classList.length === 0 ||
          e.target.classList[0] === 'react-grid-layout' ||
          e.target.className === 'hetu_sidebar_item'
        ) {
          return false;
        }
        return true;
      },
    });
    setGraph(g);
  }, []);

  useEffect(() => {
    if (graph) {
      graph.on('edge:mouseenter', ({ edge }) => {
        edge.addTools([
          'source-arrowhead',
          'target-arrowhead',
          {
            name: 'button-remove',
            args: {
              distance: -30,
            },
          },
        ]);
      });

      graph.on('edge:mouseleave', ({ edge }) => {
        edge.removeTools();
      });
      graph.on('node:mouseenter', ({ e, node }) => {
        // if (node === target) {
        // console.log(node.size())
        node.addTools({
          name: 'button-remove',
          args: {
            x: node.size().width - 20,
            y: 30,
            offset: { x: 10, y: 10 },
          },
        });
        // console.log(e.target.offsetParent.className)
        // }
      });
      graph.on('node:resizing', ({ node }) => {
        console.log(node.size());
      });
      // 鼠标移开时删除删除按钮
      graph.on('node:mouseleave', ({ node }) => {
        // if (node === target) {
        node.removeTools();
        // }
      });

      graph.on('node:mousedown', ({ e, node }) => {
        // console.log(e)
        if (
          e.target instanceof SVGElement ||
          e.target.offsetParent.className === 'myports_wrapper' ||
          e.target.className === 'hetu_sidebar_item'
        ) {
          node.setData({
            move: false,
          });
        } else {
          node.setData({
            move: true,
          });
        }
      });

      graph.on('edge:removed', ({ cell, edge, options }) => {
        const target = graph.getCellById(edge.target.cell);
        const source = graph.getCellById(edge.source.cell);
        if (target && source) {
          const targetData = target.getData();
          const filter = JSON.parse(JSON.stringify(targetData.filter));
          delete filter[source.id];
          console.log(filter);
          target.setData(
            {
              ...target.getData(),
              filter,
            },
            {
              overwrite: true,
            },
          );
          // target.setData({
          //   filter:{ 1:{type:'1',data:[]}}
          // })
          let children = source.getChildren();
          children = children.filter((el) => el.id !== target.id);
          if (children.length === 0) {
            source.setChildren(null);
          } else {
            source.setChildren(children);
          }
          console.log(target, source);
        }
      });

      graph.on('cell:change:data', (args) => {
        console.log(args);
        const children = args.cell.getChildren();
        const d = args.cell.getData();
        const { select } = d;

        if (
          children &&
          select &&
          select.length > 0 &&
          JSON.stringify(args.current.select) !==
          JSON.stringify(args.previous.select)
        ) {
          console.log(args.current, args.previous);
          children.forEach((item) => {
            const childrenData = item.getData();
            item.setData({
              ...childrenData,
              filter: childrenData.filter ? {
                ...childrenData.filter,
                [args.cell.id]: {
                  type: d.type,
                  select,
                },
              } : {
                  [args.cell.id]: {
                    type: d.type,
                    select,
                  },
                },
            });
          });
        }
        if (
          args.current.filter &&
          JSON.stringify(args.current.filter) !==
          JSON.stringify(args.previous.filter)
        ) {
          childrenFilter(args.current, args.cell);
        }
        console.log(args);
      });

      // 边连接
      graph.on('edge:connected', ({ isNew, edge }) => {
        if (isNew) {
          const source = edge.getSourceCell();
          const target = edge.getTargetCell();
          const tarData = target.getData();
          // Filter为target时将source的dataFrame赋给filter的对应字段
          // 处理不完整，缺少对source,target具体是什么类型标识(Filter, Table等)
          tarData.item.dataFrame = source.data.item.dataFrame;
          target.setData(tarData);
          console.log('new edge', edge, source, target);
          // 对新创建的边进行插入数据库等持久化操作
        }
      });
      // graph.on('node:moved',({ e, x, y, node, view })=>{
      //   console.log( e, x, y, node, view,)

      // })
    }
  });

  const childrenFilter = (current, children) => {
    let item = {};
    console.log(current);
    if (JSON.stringify(current.filter) === '{}') {
      if (fieldType[current.type] === 'number') {
        item = {
          id: current.item.id,
          option: barOption({
            xAxisData: data.map((item) => item[current.type]),
            data: data.map((item) => item[current.type]),
            xAxis: current.type,
            title: current.type,
            xAxisType: 'value',
          }),
        };
      } else {
        const s = new Set(data.map((item) => item[current.type]));
        const obj = {};
        const arr = [];
        data
          .map((i) => i[current.type])
          .forEach((item, i) => {
            if (obj[item] === undefined) {
              obj[item] = 1;
            } else {
              obj[item] += 1;
            }
          });
        for (const i of s) {
          arr.push(obj[i]);
        }
        console.log(obj, arr);
        item = {
          id: current.item.id,
          option: barOption({
            xAxisData: Array.from(s),
            data: arr,
            xAxis: current.type,
            title: current.type,
            xAxisType: 'category',
          }),
        };
      }
      children.setData({
        ...current,
        item,
      });
    } else {
      let filterData = [];

      if (fieldType[current.type] === 'number') {
        filterData = data.filter((el) => {
          // el[d.type] >=
        });
        item = {
          id: current.item.id,
          option: barOption({
            xAxisData: data.map((item) => item[current.type]),
            data: data.map((item) => item[current.type]),
            xAxis: current.type,
            title: current.type,
            xAxisType: 'value',
          }),
        };
      } else {
        const s = new Set(data.map((item) => item[current.type]));
        const obj = {};
        const arr = [];
        filterData = data.filter((el) => {
          for (const f of Object.keys(current.filter)) {
            const t = current.filter[f].type;
            const d = current.filter[f].select;
            if (
              fieldType[t] === 'number' &&
              (el[t] < d[0] || el[t] > d[d.length - 1])
            ) {
              return false;
            } else if (fieldType[t] === 'string' && d.indexOf(el[t]) === -1) {
              return false;
            }
          }
          return true;
        });
        // filterData = filterData.filter(el=>{
        //   for(let f of Object.keys(current.filter)){
        //     if(current.filter[f].filter){
        //       for(let n of Object.keys(current.filter[f])){
        //         const t = current.filter[f].filter[n].type
        //         const d = current.filter[f].filter[n].select
        //         if(fieldType[t] === 'number' && (el[t]<d[0] || el[t]>d[d.length-1])){
        //           return false
        //         }else if(fieldType[t] === 'string' && d.indexOf(el[t]) === -1 ){
        //           return false
        //         }
        //       }
        //     }
        //   }
        //   return true
        // })
        for (const i of s) {
          obj[i] = 0;
        }
        filterData
          .map((i) => i[current.type])
          .forEach((item, i) => {
            obj[item] += 1;
          });

        for (const i of s) {
          arr.push(obj[i]);
        }
        item = {
          id: current.item.id,
          option: barOption({
            xAxisData: Array.from(s),
            data: arr,
            xAxis: current.type,
            title: current.type,
            xAxisType: 'category',
          }),
        };
        console.log(item, filterData);
        children.setData({
          ...current,
          item,
        });
      }
    }
  };
  console.log(graph);
  return (
    <div>
      <Row className="hetu_analysis">
        <Col className="hetu_sideMenu" span={5}>
          <DatasetSide graph={graph} />
        </Col>
        <Col className="hetu_content" span={19}>
          <CanvasContent graph={graph} />
          <div className="hetu_minimap" id="miniMap" />
        </Col>
      </Row>
      <div />
    </div>
  );
}
