import { useState, useEffect } from 'react';
import {} from 'antd';

import IconFont from '../../font';
import SidebarModal from './sidebarModal';

function LabelComponent(props) {
  const [visible, setVisible] = useState(false);
  const { labelName, HandleForm, column } = props;

  return (
    <div className="hetu_sidebar_wrapper">
      <div className="hetu_sidebar_item2" onClick={() => setVisible(true)}>
        <IconFont type="hetu-shuxing" className="hetu_sidebar_item_icon" />
        {labelName}
      </div>
      {visible && (
        <SidebarModal
          setColumn={props.setColumn}
          data={props.data}
          setData={props.setData}
          filter={props.filter}
          setVisible={setVisible}
          HandleForm={HandleForm}
          column={column}
        />
      )}
    </div>
  );
}

export default LabelComponent;
