
import Filters from './sidebar/filter/filters'
import Attribute from './sidebar/attributes/attribute'

function TableSidebar(props){
    return (
        <div  className='hetu_table_sidebar_wrapper'>
      <Filters  data={props.data} setData={props.setData} filter={props.filter}/>
			<Attribute setColumn={props.setColumn} data={props.data} setData={props.setData} filter={props.filter}/>
        </div>
    )
}
export default TableSidebar
