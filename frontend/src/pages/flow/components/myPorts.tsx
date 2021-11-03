import { useState } from 'react';
import { WidthProvider, Responsive } from 'react-grid-layout';

import IconFont from '@/font';
import './style.scss';

const ResponsiveReactGridLayout = WidthProvider(Responsive);

function MyPorts(props) {
  const [count, setCount] = useState(0);

  /* 在onDrop的地方需要做的内容是：
    1. 拖入数据帧
    2. 根据拖入不同的数据帧确认不同的数据源，再根据操作符类型发送request请求
    3. 拿到请求后渲染节点（目前渲染还需要完善内容）
  */
  const onDrop = (layout, layoutItem, _event) => {
    console.log('myport drop', props.node, props, event.dataTransfer.getData('dragItem'));

    // 读取side里面dragStart事件里，dataTransfer里保存的 拖动并放下（drag and drop）过程中的数据
    const { iconName = 'hetu-ODIyuanshujuji', dataSource = '' } = JSON.parse(event.dataTransfer.getData('dragItem'));
    // console.log(123)
    const data = props.node.getData();
    console.log(data);
    data.dataset = {
      ...data.dataset,
      [props.port.id]: [
        {
          i: `${count}`,
          id: `${new Date().getTime()}`,
          x: layoutItem.x,
          y: layoutItem.y, // puts it at the bottom
          w: 2,
          h: 1,
          isDraggable: true,
          isResizable: true,
          isBounded: true,
          iconName,
          dataSource,
        },
      ],
    };
    data.item.dataFrame = dataSource;
    // data.item.option.xAxis.data =
    // 	[
    // 		"0-10",
    // 		"10-20",
    // 		"20-30"
    // 	]
    // console.log(data.item.option.xAxis.data)
    props.node.setData(data);
    console.log('myport end', data);
    // setItems([

    // ])
    setCount(count + 1);
  };

  const createItems = (el, index) => {
    console.log('createEl', el);
    return (
      <div className="hetu_ports_dataset" data-grid={el} key={el.i}>
        <IconFont type={el.iconName} style={{ fontSize: 19 }} />
      </div>
    );
  };

  return (
    <div className="myports_wrapper" id="myports_wrapper">
      <ResponsiveReactGridLayout
        className="layout"
        cols={{ lg: 12, md: 10, sm: 6, xs: 4, xxs: 2 }}
        rowHeight={24}
        useCSSTransforms
        isResizable={false}
        isDroppable
        isDraggable={false}
        onDrop={onDrop}
        measureBeforeMount={false}
        style={{ height: '100%' }}
      // draggableCancel='.noDraggable'
      >
        {props.node.getData().dataset[props.port.id] &&
          props.node
            .getData()
            .dataset[props.port.id].map((el, index) => createItems(el, index))}
      </ResponsiveReactGridLayout>
      <div className="myports_after" />
    </div>
  );
}

export default MyPorts;
