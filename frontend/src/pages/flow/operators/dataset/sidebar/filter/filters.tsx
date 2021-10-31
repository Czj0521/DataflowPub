import { useState, useEffect } from 'react';
<<<<<<< 7ff9462e30d12722a64d89abe5f3aa45cc8441c2
<<<<<<< 887204ba599d754458ccd4ae8195927983bef47d

=======
>>>>>>> refactor:SideItem
=======
>>>>>>> refactor:SideItem
import IconFont from '../../../../../../font';
import SidebarModal from './sidebarModal';

function Filters(props) {
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
      <div className={visible ? 'hetu_sidebar_item active' : 'hetu_sidebar_item'} onClick={expandSidebarItem}>
        <IconFont type="hetu-shuxing" className="hetu_sidebar_item_icon" />{' '}
        filter
      </div>
      {visible && (
        <SidebarModal
          dataFrame={props.dataFrame}
          data={props.data}
          setData={props.setData}
          filter={props.filter}
          setVisible={setVisible}
        />
      )}
    </div>
  );
}

export default Filters;
