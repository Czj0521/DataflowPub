import '../../components/style.scss';

import { useEffect, useState } from 'react';

import ColumnOrGroupForm from '../../../../components/sidebar/columnOrGroup/columnOrGroup';
import InputsForm from '../../../../components/sidebar/inputs/inputs';
import Attributes from '../dataset/sidebar/attributes/attribute';
import LabelComponent from '@/components/sidebar/labelComponent';

function Transpose(props) {
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
      <div className="operators-container">
        <div className="operators-display">Transpose</div>
      </div>
      <div className="hetu_table_sidebar_wrapper">
        <LabelComponent HandleForm={ColumnOrGroupForm} column={column} labelName="column" />
        <LabelComponent HandleForm={ColumnOrGroupForm} column={column} labelName="group by" />
        <Attributes setColumn={props.setColumn} data={props.data} setData={props.setData} filter={props.filter} />
        <LabelComponent HandleForm={InputsForm} labelName="inputs" />
      </div>
    </div>
  );
}

export default Transpose;
