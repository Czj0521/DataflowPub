import { useState } from 'react';
import Attribute from './sidebar/attributes/attribute';
import Filters from './sidebar/filter/filters';
import Group from  './sidebar/group/group'

function TableSidebar(props) {
  // 在这里组织table请求体，发送请求重新渲染图表
  const [tableInputJson,setTableInputJson] = useState(props.tableDescription)
  // console.log(tableInputJson)
  return (
    <div className="hetu_table_sidebar_wrapper">
      <Filters tableInputJson={tableInputJson} setTableInputJson={setTableInputJson} data={props.data} setData={props.setData} filter={props.filter} />
      <Attribute tableInputJson={tableInputJson} setTableInputJson={setTableInputJson} column={props.column} setColumn={props.setColumn} data={props.data} setData={props.setData} filter={props.filter} />
      <Group tableInputJson={tableInputJson} setTableInputJson={setTableInputJson} column={props.column} setColumn={props.setColumn} data={props.data} setData={props.setData} filter={props.filter} />
    </div>
  );
}
export default TableSidebar;
