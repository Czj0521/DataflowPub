import { FC, useEffect, useState } from 'react';
import SideBar from './SideBar';

const Transpose: FC = (props) => {
  console.log(props);
  const [column, setColumn] = useState([]);

  useEffect(() => {
    // 因为接口不同，所以每一个数据操作的数据返回值都要变
    if (props.node.getData().item.option.data) {
      const { column: columns } = props.node.getData().item.option;
      console.log('column', column);
      setColumn(columns ? columns.map(item => ({ ...item, width: 300, align: 'center' })) : []);
    }
  }, [props.node]);
  
  return (
    <div className="hetu_basecomponent_wrapper" draggable>
      <SideBar column={column} />
      <div className="wrapper">Transpose</div>
    </div>
  );
};

export default Transpose;
