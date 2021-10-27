import IconFont from '../../../../../../font';
import AdvancedSearchForm from './form';

function SidebarModal(props) {
  return (
    <div className="hetu_sidebar_modal_wrapper">
      <div className="hetu_sidebar_connect" />
      <div className="hetu_sidebar_attributes_modal">
        <AdvancedSearchForm
          setColumn={props.setColumn}
          data={props.data}
          setData={props.setData}
          filter={props.filter}
        />
      </div>
      <IconFont
        type="hetu-chahao"
        onClick={() => {
          props.setVisible(false);
        }}
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
  );
}

export default SidebarModal;
