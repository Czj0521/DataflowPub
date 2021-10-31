import { Radio, Input, Space } from 'antd';
import { useEffect, useState } from 'react';
import './index.scss';

const { Search } = Input;

function ColumnOrGroupForm(props) {
  const { column } = props;
  const [radioList, setRadioList] = useState<any>([]);
  const [selected, setSelected] = useState<string | null>(null);

  useEffect(() => {
    setRadioList(column);
  },[]);

  const onSearch = (value) => {
    const result = column.filter((item) => item.title.includes(value));
    setRadioList(result);
  };

  return (
    <div className="labelForm">
      <Search onSearch={onSearch} />
      <Radio.Group style={{ color: '#fff' }} onChange={(e) => setSelected(e.target.value)} value={selected}>
        <Space direction="vertical">
          {
            radioList.map((item) => {
              return <Radio value={item.key}>{item.title}</Radio>;
            })
          }
        </Space>
      </Radio.Group>
    </div>
  );
}

export default ColumnOrGroupForm;
