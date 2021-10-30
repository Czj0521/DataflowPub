import FilterForm from './filterForm';

function Filter(props) {
  console.log('In Filter Component', props);
  const { item = {} } = props;
  const { dataFrame = '' } = item;
  return (
    <div style={{ width: '100%', height: '600px' }}>
      <FilterForm dataFrame={dataFrame} />
    </div>
  );
}

export default Filter;
