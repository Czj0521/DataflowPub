import { useState, FC, ReactElement } from "react";
import IconFont from "@/font";

type SideItemProps = {
    name: string;
    width: number;
    height: number;
    icon?: ReactElement;
    onExpand?: () => void;
    onClose?: () => void;
}

const SideItem: FC<SideItemProps> = (props) => {
  const [visible, setVisible] = useState(false);

  const {
    name,
    children,
    width = 300,
    height = 300,
    onClose,
    onExpand,
    icon,
  } = props;

  const expandSidebarItem = (e) => {
    onExpand && onExpand();
    setVisible(true);
  };

  const closeSidebarModal = (e) => {
    onClose && onClose();
    setVisible(false);
  };

  return (
    <div className="hetu_sidebar_wrapper">
      <div
        className={visible ? 'hetu_sidebar_item active' : 'hetu_sidebar_item'}
        onClick={expandSidebarItem}
      >
        {icon || (
          <IconFont type="hetu-shuxing" className="hetu_sidebar_item_icon" />
        )}
        {name}
      </div>
      {visible && (
        <div className="hetu_sidebar_modal_wrapper">
          <div className="hetu_sidebar_connect" />
          <div
            className="hetu_sidebar_modal"
            style={{ width, height, left: -(width + 10) }}
          >
            {children}
          </div>
          <IconFont
            type="hetu-chahao"
            onClick={closeSidebarModal}
            className="hetu_sidebar_modal_close"
            style={{
              position: 'absolute',
              left: -22,
              top: -28,
              fontSize: 20,
              cursor: 'pointer',
            }}
          />
        </div>
      )}
    </div>
  );
};

export default SideItem;
