import { useState, useEffect } from 'react';
import { Form, Row, Col, Input, Button, Select } from 'antd';
import './form.scss';
import {
  getTableColumn,
  getAllOperation,
  getTable,
} from '../../../../../../api/table';

const { Option } = Select;
const AdvancedSearchForm = (props) => {
  const [count, setCount] = useState(1);
  const [column, setColumn] = useState([]);
  const [columnType, setColumnType] = useState(null);
  const [operations, setOperations] = useState(new Array(100));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getTableColumn({ datasource: 'dataflow.airuuid' }).then((res) => {
      const tempColumn = [];
      Object.keys(res).map((val) => {
        tempColumn.push(val);
        return 1
      });
      setColumn(tempColumn);
      setColumnType(res);
    });
  }, []);

  useEffect(() => {
    // 把operation总体类型与应取operationType进行对比
  }, [operations]);

  const [form] = Form.useForm();

  const getFields = () => {
    const children = [];

    for (let i = 0; i < count; i++) {
      if (i == 0) {
        children.push(
          <Row gutter={24}>
            <Col span={6} key={`filter-${i}`}>
              <Form.Item initialValue={'Where'} name={`filter-${i}`}>
                <Input
                  defaultValue={'Where'}
                  disabled={true}
                  style={{
                    color: 'white',
                    background: '#282820',
                    width: '100%',
                    borderColor: '#2e3c51',
                  }}
                  size={'small'}
                />
              </Form.Item>
            </Col>
            <Col span={6} key={`column-${i}`}>
              <Form.Item
                name={`column-${i}`}
                rules={[
                  {
                    required: true,
                    message: 'Input something!',
                  },
                ]}
              >
                {/* <Input   style={{color:'white',background:'#282820',width:'100%',borderColor:'#2e3c51'}} size={'small'}/> */}
                <Select
                  onChange={onChange}
                  dropdownStyle={{ color: 'white', background: '#282820' }}
                  size={'small'}
                >
                  {column
                    ? column.map((val) => {
                      return (
                        <Option i={i} key={val}>
                          {val}
                        </Option>
                      );
                    })
                    : null}
                </Select>
              </Form.Item>
            </Col>
            <Col span={6} key={`condition-${i}`}>
              <Form.Item
                name={`condition-${i}`}
                rules={[
                  {
                    required: true,
                    message: 'Input something!',
                  },
                ]}
              >
                {operations[i] ? (
                  <Select
                    dropdownStyle={{ color: 'white', background: '#282820' }}
                    size={'small'}
                  >
                    {Object.keys(operations[i]).map((val) => {
                      return <Option key={val}>{val}</Option>;
                    })}
                  </Select>
                ) : (
                  <Input
                    style={{
                      color: 'white',
                      background: '#282820',
                      width: '100%',
                      borderColor: '#2e3c51',
                    }}
                    size={'small'}
                  />
                )}
              </Form.Item>
            </Col>
            <Col span={6} key={`value-${i}`}>
              <Form.Item
                name={`value-${i}`}
                rules={[
                  {
                    required: true,
                    message: 'Input something!',
                  },
                ]}
              >
                <Input
                  style={{
                    color: 'white',
                    background: '#282820',
                    width: '100%',
                    borderColor: '#2e3c51',
                  }}
                  size={'small'}
                />
              </Form.Item>
            </Col>
          </Row>,
        );
      } else {
        children.push(
          <Row gutter={24}>
            <Col span={6} key={`filter-${i}`}>
              <Form.Item name={`filter-${i}`}>
                <Select
                  dropdownStyle={{ background: '#282820' }}
                  size={'small'}
                >
                  <Option key="And">{'AND'}</Option>
                  <Option key="Or">{'OR'}</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={6} key={`column-${i}`}>
              <Form.Item
                name={`column-${i}`}
                rules={[
                  {
                    required: true,
                    message: '请输入值...',
                  },
                ]}
              >
                <Select
                  onChange={onChange}
                  dropdownStyle={{ color: 'white', background: '#282820' }}
                  size={'small'}
                >
                  {column
                    ? column.map((val) => {
                      return (
                        <Option i={i} key={val}>
                          {val}
                        </Option>
                      );
                    })
                    : null}
                </Select>
              </Form.Item>
            </Col>
            <Col span={6} key={`condition-${i}`}>
              <Form.Item
                name={`condition-${i}`}
                rules={[
                  {
                    required: true,
                    message: '请输入值...',
                  },
                ]}
              >
                {operations[i] ? (
                  <Select
                    dropdownStyle={{ color: 'white', background: '#282820' }}
                    size={'small'}
                  >
                    {Object.keys(operations[i]).map((val) => {
                      return <Option key={val} value={val}>{val}</Option>;
                    })}
                  </Select>
                ) : (
                  <Input
                    style={{
                      color: 'white',
                      background: '#282820',
                      width: '100%',
                      borderColor: '#2e3c51',
                    }}
                    size={'small'}
                  />
                )}
              </Form.Item>
            </Col>
            <Col span={6} key={`value-${i}`}>
              <Form.Item
                name={`value-${i}`}
                rules={[
                  {
                    required: true,
                    message: '请输入值...',
                  },
                ]}
              >
                <Input
                  style={{
                    color: 'white',
                    background: '#282820',
                    width: '100%',
                    borderColor: '#2e3c51',
                  }}
                  size={'small'}
                />
              </Form.Item>
            </Col>
          </Row>,
        );
      }
    }
    return children;
  };
  // selectNum:第几个值
  const onChange = (val, selectNum) => {
    const i = selectNum.i;
    let type = '';
    let operationType = '';
    Object.keys(columnType).map((item) => {
      if (val == item) {
        type = columnType[item];
        return 1;
      }
      return 1;
    });
    console.log(type);
    if (type.includes('Int') || type.includes('Float') || type.includes('Nullable')) {
      operationType = 'numeric';
    } else if (type.includes('String')) {
      operationType = 'string';
    } else if (type.includes('Date')) {
      operationType = 'date';
    }
    // 拿操作符
    getAllOperation().then((res) => {
      const temp = operations;
      console.log(res)
      Object.keys(res).map((el) => {
        if (el == operationType) {
          console.log(temp);
          temp[i] = res[el];
          console.log(temp);
          setOperations(temp);
          setLoading(!loading);
        }
        return 1
      });
    });
  };

  const onFinish = (values) => {
    // 记录了所有的数组的操作类型
    console.log(operations);
    const arr = [];
    console.log(operations)
    for (let i = 0; i < count; i++) {
      Object.keys(operations[i]).map((val) => {
        if (values[`condition-${i}`] == val) {
          if (val == 'contains all') {
            const containsAllArr = values[`value-${i}`].split('，')
            // 拼接contains all的字符串
            let containsAllStr = ''
            console.log(containsAllArr)
            arr.push(operations[i][val]);
            for (let j = 0; j < containsAllArr.length; j++) {
              if (j < containsAllArr.length - 1) {
                containsAllStr = containsAllStr + arr[i].replace('&*&', values[`column-${i}`]).replace('#$#', containsAllArr[j]) + ' and '
              } else {
                containsAllStr = containsAllStr + arr[i].replace('&*&', values[`column-${i}`]).replace('#$#', containsAllArr[j])
              }
            }
            arr[i] = containsAllStr
          } else if (val == 'contains any') {
            const containsAllArr = values[`value-${i}`].split('，')
            // 拼接contains all的字符串
            let containsAllStr = ''
            console.log(containsAllArr)
            arr.push(operations[i][val]);
            for (let j = 0; j < containsAllArr.length; j++) {
              if (j < containsAllArr.length - 1) containsAllStr = containsAllStr + arr[i].replace('&*&', values[`column-${i}`]).replace('#$#', containsAllArr[j]) + ' or '
              else containsAllStr = containsAllStr + arr[i].replace('&*&', values[`column-${i}`]).replace('#$#', containsAllArr[j])
            }
            arr[i] = containsAllStr
          } else {
            console.log(operations[i][val]);
            arr.push(operations[i][val]);
            arr[i] = arr[i].replace('&*&', values[`column-${i}`]);
            arr[i] = arr[i].replace('#$#', values[`value-${i}`]);
            console.log(arr);
          }
        }
        return 1
      });
    }
    let str = arr[0];
    for (let i = 1; i < count; i++) {
      str = str + ' ' + values[`filter-${i}`] + ' ' + arr[i];
    }
    console.log(str);
    const tableData = {
      job: 'table_start_job',
      requestId: 'ac6wa2ds6c62',
      operatorType: 'table',
      tableDescription: {
        dataSource: 'dataflow.airuuid',
        filter: str,
        jobType: 'table',
        limit: 2000,
        project: ['*'],
      },
      workspaceId: 'string',
    }
    getTable(tableData).then((res) => {
      props.setData(res.outputs);
      console.log(res);
    });
    console.log('Received values of form: ', values);
  };

  return (
    <Form
      style={{ padding: 10 }}
      form={form}
      name="advanced_search"
      className="ant-advanced-search-form"
      onFinish={onFinish}
    >
      {getFields()}
      <Row>
        <Col
          span={24}
          style={{
            textAlign: 'right',
          }}
        >
          <Button type="primary" htmlType="submit" >
            确认
          </Button>
          <Button
            style={{ margin: '0 8px' }}
            onClick={() => {
              form.resetFields();
            }}
          >
            重置
          </Button>
          <Button
            style={{ margin: '0 8px' }}
            onClick={() => {
              setCount(count + 1);
            }}
          >
            添加一行
          </Button>
        </Col>
      </Row>
    </Form>
  );
};

export default AdvancedSearchForm;
