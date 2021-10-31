import { useState, useEffect, useReducer } from 'react';
import { Form, Row, Col, Input, Button ,Select } from 'antd';
import { getTableColumn, getAllOperation, getTable } from '@/api/table';
import { PlusOutlined, CloseOutlined } from '@ant-design/icons';
import './index.less';

const { Option } = Select;
const rules = [{
  required: true,
  message: 'Input something!',
}];
const cusDropDownStyle = {
  color: '#282828',
  position: 'relative',
  background: '#f4f5f7',
};
const rowData = { filter: 'Where', column: '', condition: '', value: '' };
const initialState = {
  filterRules: [{ filter: 'Where', column: '', condition: '', value: '', key: 0 }],
  uniqueKey: 0,
};
const mapTypeToOperateType = { String: 'string', Date: 'date', UInt8: 'numeric', Float64: 'numeric', Int: 'numeric' };

function reducer(state, action) {
  const len = state.filterRules.length;
  const str = len >= 1 ? 'Where' : 'Or';
  const { filterRules } = state;
  switch (action.type) {
    case 'deleteRow':
      console.log('delete', action.index);
      filterRules.splice(action.index, 1);
      return { ...state, filterRules };
    case 'addRow':
      return { filterRules: state.filterRules.concat({ ...rowData, filter: str, key: ++state.uniqueKey }),
        uniqueKey: state.uniqueKey + 1 };
    case 'changeRowData':
      filterRules[action.index][action.name] = action.value;
      return { ...state, filterRules };
    default:
      return state;
  }
}
function FilterForm({ dataFrame, setData, ...props }) {
  const [form] = Form.useForm();
  const [column, setColumn] = useState([]);
  const [columnType, setColumnType] = useState({});
  const [operations, setOperations] = useState(new Array(100));
  const [state, dispatch] = useReducer(reducer, initialState);
  console.log('in filterForm', dataFrame);
  useEffect(() => {
    // 'dataflow.airuuid'
    dataFrame && getTableColumn({ datasource: dataFrame || 'dataflow.airuuid' }).then((res) => {
      setColumn(Object.keys(res));
      setColumnType(res);
    });
    // 拿操作符
    getAllOperation().then((res) => {
      console.log('res operate', res.payload);
      setOperations(res.payload);
    });
  }, [dataFrame]);
  const onFinish = (values) => {
    console.log(state.filterRules, values);
    const formData: Array<any> = [];
    const { filterRules } = state;
    let i = -1;
    let str = '';
    while (filterRules[++i]) {
      console.log('i', i);
      const { key: uKey } = filterRules[i];
      formData.push({ key: uKey,
        condition: values[`condition-${uKey}`],
        column: values[`column-${uKey}`],
        value: values[`value-${uKey}`],
        filter: values[`filter-${uKey}`] });
      let colType = columnType[values[`column-${uKey}`]]; // 列类型
      colType = mapTypeToOperateType[colType];
      const finalCondition = operations[colType][values[`condition-${uKey}`]].replace('&*&', values[`column-${uKey}`]).replace('#$#', values[`value-${uKey}`]);
      str += `${ i > 0 ? values[`filter-${i}`] : '' } ${finalCondition} `;
    }
    console.log('formData', formData, str);
    const tableData = {
      dataSource: dataFrame,
      filter: str,
      limit: 2000,
      project: ['*'],
    };
    // 存储 表单值和
    getTable(tableData).then((res) => {
      setData(res);
      // 待处理
      console.log(res);
    });
    console.log('Received values of form: ', values);
  };

  const getOperations = (colName) => {
    let colType = columnType[colName]; // 列类型
    colType = mapTypeToOperateType[colType]; // 转为操作符里面的类型
    return operations[colType];
  };
  return (
    <Form
      style={{ padding: 10 }}
      form={form}
      name="advanced_search"
      className="filter-form"
      onFinish={onFinish}
    >
      {
        state.filterRules.map((item, i) => (
          <Row gutter={16}>
            <Col span={1} key={`del-${item.key}`}>
              <CloseOutlined className="delIcon" onClick={() => dispatch({ type: 'deleteRow', index: i })} />
            </Col>
            <Col span={5} key={`filter-${item.key}`}>
              <Form.Item
                initialValue={i >= 1 ? 'Or' : 'Where'}
                name={`filter-${item.key}`}
              >{
                i === 1 ? 
                  (<Select dropdownStyle={cusDropDownStyle} size={'small'}>
                    <Option key="And" value={'And'}>{'AND'}</Option>
                    <Option key="Or" value={'Or'}>{'OR'}</Option>
                   </Select>) :
                  <Input 
                    style={{
                      color: 'white',
                      background: '#282820',
                      width: '100%',
                      borderColor: '#2e3c51',
                    }}
                    size={'small'}
                    disabled
                    // defaultValue={i > 1 ? 'Or' : 'Where'}
                  />
              }
              </Form.Item>
            </Col>
            <Col span={6} key={`column-${item.key}`}>
              <Form.Item
                name={`column-${item.key}`}
                rules={rules}
              >
                <Select
                  onChange={(val) => dispatch({ type: 'changeRowData', index: i, name: 'column', value: val })}
                  // dropdownStyle={cusDropDownStyle}
                  size={'small'}
                >
                  {
                  column ? 
                    column.map((val) => {
                      return <Option i={i} key={val}>{val}</Option>;
                    })
                    : null
                }
                </Select>
              </Form.Item>
            </Col>
            <Col span={6} key={`condition-${item.key}`}>
              <Form.Item
                name={`condition-${item.key}`}
                rules={rules}
              >
                {form.getFieldValue(`column-${item.key}`) ?
                  <Select dropdownStyle={cusDropDownStyle} size={'small'}>
                    {
                      Object.keys(getOperations(item.column)).map((val) => {
                        return <Option key={val}>{val}</Option>;
                      })
                }
                  </Select> 
                  : <Input style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
            }
              </Form.Item>
            </Col>
            <Col span={6} key={`value-${item.key}`}>
              <Form.Item
                name={`value-${item.key}`}
                rules={rules}
              >
                <Input style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
              </Form.Item>
            </Col>
          </Row>
        ))
      }
      <Row>
        <PlusOutlined style={{ fontSize: '18px', color: 'white' }} onClick={() => dispatch({ type: 'addRow' })} />
        <Button type="primary" htmlType="submit" size="small" >
          Save
        </Button>
      </Row>
    </Form>
  );
}

export default FilterForm;
