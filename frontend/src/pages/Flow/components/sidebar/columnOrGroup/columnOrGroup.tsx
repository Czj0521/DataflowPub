import { Radio, Input, Space, Divider } from 'antd';
import { useEffect, useState } from 'react';
import style from'./index.module.scss';

const { Search } = Input;

function ColumnOrGroupForm(props) {
  const { column, labelName } = props;
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
      <Divider style={{ color: '#eee', borderColor: '#eee', fontSize: 12 }}>
        {labelName}
      </Divider>
      <Search onSearch={onSearch} />
      <Radio.Group style={{ color: '#fff' }} onChange={(e) => setSelected(e.target.value)} value={selected}>
        <Space direction="vertical">
          {/* {
            radioList.map((item) => {
              return <Radio value={item.key}>{item.title}</Radio>;
            })
          } */}
        </Space>
      </Radio.Group>
    </div>
  );
}

export default ColumnOrGroupForm;
