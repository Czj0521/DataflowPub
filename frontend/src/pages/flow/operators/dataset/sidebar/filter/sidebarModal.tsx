import AdvancedSearchForm from '../../../Filter/filterForm';
import IconFont from '../../../../../../font';

function SidebarModal(props) {
  return (
    <div className="hetu_sidebar_modal_wrapper">
      <div className="hetu_sidebar_connect" />
      <div className="hetu_sidebar_modal">
        <AdvancedSearchForm
          data={props.data}
          dataFrame={props.dataFrame}
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
