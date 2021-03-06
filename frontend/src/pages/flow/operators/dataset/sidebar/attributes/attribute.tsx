import { useState } from 'react';
import IconFont from '@/font';
import SidebarModal from './sidebarModal';

function Attributes(props) {
  const [visible, setVisible] = useState(false);

  const expandSidebarItem = (e) => {
    setVisible(true);
    console.log(e);
    // e.target
  };

  const closeSidebarModal = (e) => {
    setVisible(false);
  };

  return (
    <div className="hetu_sidebar_wrapper">
      <div className={visible ? "hetu_sidebar_item active" : "hetu_sidebar_item"} onClick={expandSidebarItem}>
        <IconFont type="hetu-shuxing" className="hetu_sidebar_item_icon" />
        Attribute
      </div>
      {visible && (
        <SidebarModal
          setTableInputJson={props.setTableInputJson}
          tableInputJson={props.tableInputJson}
          column={props.column}
          setColumn={props.setColumn}
          data={props.data}
          setData={props.setData}
          filter={props.filter}
          setVisible={setVisible}
        />
      )}
    </div>
  );
}

export default Attributes;
