import { useState, useEffect, Component } from 'react'
import { Divider, Input, List } from 'antd';
import { PlusCircleOutlined, RightOutlined } from '@ant-design/icons';
import getParenthesesStr from '../../../../../../utils/getParenthesesStr';
import {
  getTableColumn,
  getAllOperation,
  getTable,
  getAllGroup,
} from '../../../../../../api/table';

const { Search } = Input;
export default function Form(props) {
  console.log(props)
  const [selectList, setSelectList] = useState([])
  const [visableList, setVisableList] = useState([])
  const [columnType, setColumnType] = useState(null);
  const [loading, setLoading] = useState(false)
  const [groupList, setGroupList] = useState([])
  const [metadataList, setMetadataList] = useState([])
  const [currentColumnType, setCurrentColumnType] = useState(null)
  const [row, setRow] = useState(0)

  useEffect(() => {
    getTableColumn({ datasource: 'dataflow.airuuid' }).then((res) => {
      setColumnType(res);
      const arr = Object.keys(res).map(val => {
        return val
      })
      setMetadataList(arr)
      setVisableList(metadataList.map(val => {
        return false
      }))
    });
  }, [])

  const slideLeft = () => {
    document.getElementById('box').scrollLeft = 250;
  }

  const onSearch = value => console.log(value);

  const addVisableList = (item) => {
    let selectArr = selectList
    selectArr = selectArr.concat([item])
    setSelectList(selectArr)
    const project = props.tableInputJson
    project.tableDescription.project = selectArr
    // props.setTableInputJson(project)
    getTable(project).then(res => {
      const newColumn = selectArr.map(val => {
        return {
          title: val,
          dataIndex: val,
          key: val
        }
      })
      props.setColumn(newColumn)
      props.setData(res.outputs)
    })
  }

  const aggregationFun = (item, index) => {
    const type = columnType[item]
    let operationType = null
    console.log(columnType)
    if (type.includes('Int') || type.includes('Float') || type.includes('Nullable')) {
      operationType = "numeric";
    } else if (type.includes('String')) {
      operationType = "string";
    } else if (type.includes('Date')) {
      operationType = "date";
    }
    getAllGroup().then(res => {
      if (operationType == 'numeric') {
        const aggregationList = []
        Object.keys(res.payload[operationType]).map(val => {
          aggregationList.push(val)
        })
        setGroupList(aggregationList)
        setCurrentColumnType('numeric')
        setRow(index)
      }
      else {
        const aggregationList = []
        Object.keys(res.payload['others']).map(val => {
          aggregationList.push(val)
        })
        setGroupList(aggregationList)
        setCurrentColumnType('others')
        setRow(index)
      }
    })
  }

  const addAggregation = (e) => {
    getAllGroup().then(res => {
      console.log(res.payload[currentColumnType][e.target.text])
      const newTableInputJson = props.tableInputJson
      let arr = []
      arr = newTableInputJson.tableDescription.project.map(item => {
        if (item.includes('(')) {
          return getParenthesesStr(item)[1]
        } else {
          return item
        }
      })
      newTableInputJson.tableDescription.project[row] = res.payload[currentColumnType][e.target.text].replace('&*&', arr[row])
      props.setTableInputJson(newTableInputJson)
      getTable(newTableInputJson).then(response => {
        const newColumn = newTableInputJson.tableDescription.project.map(val => {
          return {
            key: val,
            dataIndex: val,
            title: val,
          }
        })
        props.setColumn(newColumn)
        props.setData(response.outputs)
        setSelectList(arr)
      })
    })
  }
  return (
    <div className="hetu_sidebar_attributes_modal_div">
      <div className="hetu_sidebar_attributes_modal_box" id="box">
        <div className="hetu_sidebar_attributes_modal_left" >
          <Divider style={{ color: '#eee', borderTopColor: '#eee' }}>available</Divider>
          <div>
            <Search placeholder="input search text" onSearch={onSearch} style={{ width: 220 }} />
          </div>
          <div style={{ marginTop: '10px' }}>
            <a style={{ color: '#eee' }}>use all</a>
            <List
              style={{ color: '#eee' }}
              size="small"
              dataSource={metadataList}
              renderItem={(item, index) =>
                <List.Item onMouseEnter={() => {
                  const arr = visableList
                  arr[index] = true
                  setLoading(!loading)
                  setVisableList(arr)
                }}
                  onMouseLeave={() => {
                    const arr = visableList
                    arr[index] = false
                    setLoading(!loading)
                    setVisableList(arr)
                  }}
                >
                  {visableList[index] == true ? <a onClick={() => addVisableList(item)}>
                    <PlusCircleOutlined />
                  </a> : null}
                  {item}
                </List.Item>}
            />
          </div>
        </div>
        <div class="hetu_sidebar_attributes_modal_mid" >
          <Divider style={{ color: '#eee', borderTopColor: '#eee' }}>used</Divider>
          <div>
            <Search placeholder="input search text" onSearch={onSearch} style={{ width: 220 }} />
          </div>
          <div style={{ marginTop: '10px', overFlow: 'auto' }}>
            <a style={{ color: '#eee' }}>clear all</a>
            <List
              style={{ color: '#eee' }}
              size="small"
              dataSource={selectList}
              renderItem={(item, index) => <List.Item onMouseEnter={() => { }} onMouseLeave={() => { }} >
                <span>{item}</span>
                <a onClick={() => aggregationFun(item, index)}>
                  <RightOutlined onClick={slideLeft} />
                </a>
              </List.Item>}
            />
          </div>
        </div>
        <div class="hetu_sidebar_attributes_modal_right" >
          <List
            style={{ color: '#eee' }}
            size="small"
            dataSource={groupList}
            renderItem={(item, index) => <List.Item onMouseEnter={() => { }} onMouseLeave={() => { }} >
              <span><a onClick={(e) => addAggregation(e)}>{item}</a></span>
              <RightOutlined />
            </List.Item>}
          />
        </div>
      </div>
    </div>
  );
}
