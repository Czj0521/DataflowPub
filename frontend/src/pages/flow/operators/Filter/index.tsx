import { memo, useCallback } from 'react';
import FilterForm from './filterForm';

function Filter(props) {
  console.log('In Filter Component', props);
  const { item = {} } = props.node.data;
  const { dataFrame = '' } = item;
  const handleSave = (data) => {
    // 保存数据
    console.log('handleSave', data)
    props.node.setData(data)
  }
  const memoziredFilterForm = useCallback(
    () => {
      return <FilterForm dataFrame={dataFrame} setData={handleSave} />
    },
    [props.node.data.item.dataFrame],
  )
  return (
    <div style={{ width: '300', height: '600px', position: 'absolute', left: 0, top: 15 }}>
      {memoziredFilterForm()}
      {/* <FilterForm dataFrame={dataFrame} setData={handleSave} /> */}
    </div>
  );
}

export default Filter;
// export default memo(Filter, ((prevProps, nextProps) => {
//   if(JSON.stringify(prevProps.node.data) !== JSON.stringify(nextProps.node.data)) return false  //返回false更新
//   return true
// }));
