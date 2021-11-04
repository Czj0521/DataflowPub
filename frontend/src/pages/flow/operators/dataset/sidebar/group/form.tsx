import { useState, useEffect } from 'react'
import { Input, List } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import './form.scss';
import {
  getTableColumn,
  getTable,
} from '@/api/table';

export default function temp(props) {

  const [metadataList, setMetadataList] = useState([])

  useEffect(() => {
    getTableColumn({ datasource: 'dataflow.airuuid' }).then((res) => {
      const arr = Object.keys(res).map(val => {
        return val
      })
      setMetadataList(arr)
    });
  }, [])

  const addGroup = (item) => {
    if (props.tableInputJson.tableDescription.project[0] !== '*') {
      let arr = props.tableInputJson.tableDescription
      if (!arr.group) {
        const temp = []
        temp.push(item)
        arr['group'] = temp
      } else {
        arr.group.push(item)
      }
    }
    getTable(props.tableInputJson).then(res => {
      props.setData(res.outputs)
    })
  }

  return (
    <div className="hetu_sidebar_attributes_modal_div">
      <Input size="small" placeholder="Search ..." prefix={<SearchOutlined />} />
      <List
        style={{ color: '#eee' }}
        size="small"
        dataSource={metadataList}
        renderItem={(item, index) => <List.Item onMouseEnter={() => { }} onMouseLeave={() => { }} >
          <span><a onClick={() => addGroup(item)}>{item}</a></span>
        </List.Item>}
      />
    </div>
  );
}
