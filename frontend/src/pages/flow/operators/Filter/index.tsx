import FilterForm from './filterForm';

function Filter(props) {
  console.log('In Filter Component', props);
  const { item = {} } = props;
  const { dataFrame = '' } = item;
  return (
    <div style={{ width: '100%', height: '600px', position: 'absolute', left: '0', top: 0 }}>
      <FilterForm dataFrame={dataFrame} />
    </div>
  );
}

export default Filter;
