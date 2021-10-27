import Attribute from './sidebar/attributes/attribute';
import Filters from './sidebar/filter/filters';

function TableSidebar(props) {
  return (
    <div className="hetu_table_sidebar_wrapper">
      <Filters data={props.data} setData={props.setData} filter={props.filter} />
      <Attribute setColumn={props.setColumn} data={props.data} setData={props.setData} filter={props.filter} />
    </div>
  );
}
export default TableSidebar;
