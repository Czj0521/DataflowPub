import { useState ,useEffect } from 'react';
import { Form, Row, Col, Input, Button ,Select } from 'antd';
import { DownOutlined, UpOutlined } from '@ant-design/icons';
import './form.scss';
import { getTableColumn,getAllOperation,getTable } from '../../../../../../api/table.js';
import getParenthesesStr from '../utils/getParenthesesStr.js';
import axios from 'axios';

const { Option } = Select;
const AdvancedSearchForm = (props) => {
  const [count,setCount] = useState(1);
  const [column,setColumn] = useState([]);
  const [columnType,setColumnType] = useState(null);
  const [operations,setOperations] = useState(new Array(100));
  const [loading,setLoading] = useState(true);
	
  useEffect(() => {
  	getTableColumn({ datasource: 'databoard_gluttony.test2' }).then((res) => {
      const tempColumn = [];
      Object.keys(res).map((val) => {
        tempColumn.push(val);
      });
      setColumn(tempColumn);
      setColumnType(res);
    });
  }, []);
  
  useEffect(() => {
  	console.log(operations);
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
      <Form.Item
        initialValue={'Where'}
        name={`filter-${i}`}
      >
        <Input defaultValue={'Where'} disabled style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
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
          dropdownStyle={{ color: 'white',background: '#282820' }}
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
        {	operations[i] ?
          <Select dropdownStyle={{ color: 'white',background: '#282820' }} size={'small'}>
            {
			  						Object.keys(operations[i]).map((val) => {
			  							return <Option key={val}>{val}</Option>;
			  						})
			  						}
          </Select> 
			  						:
          <Input style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
			  					}
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
        <Input style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
      </Form.Item>
    </Col>
  </Row>,
        );
	  } else {
		 children.push(
  <Row gutter={24}>
    <Col span={6} key={`filter-${i}`}>
      <Form.Item
        name={`filter-${i}`}
      >
        <Select dropdownStyle={{ background: '#282820' }} size={'small'}>
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
		 						message: 'Input something!',
		 					  },
		 					]}
      >
        <Select
          onChange={onChange}
          dropdownStyle={{ color: 'white',background: '#282820' }}
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
        {	operations[i] ?
          <Select dropdownStyle={{ color: 'white',background: '#282820' }} size={'small'}>
            {
								Object.keys(operations[i]).map((val) => {
								  return <Option key={val}>{val}</Option>;
								})
								}
          </Select> 
		 			  		  :
          <Input style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
							}
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
        <Input style={{ color: 'white',background: '#282820',width: '100%',borderColor: '#2e3c51' }} size={'small'} />
      </Form.Item>
    </Col>
  </Row>,
        );
	  }
    }
    return children;
  };
  // selectNum:第几个值
  const onChange = (val,selectNum) => {
	  const { i } = selectNum;
	  let type = '';
	  let operationType = ''; 
	  Object.keys(columnType).map((item) => {
		  if (val == item) {
			  type = columnType[item];
		  }
	  });
	   console.log(type);
	  switch (type) {
		  case 'String':
		  {
			  operationType = 'string';
			  break;
		  }
		  case 'Int8':
		  {
			  operationType = 'numeric';
			  break;
		  }
		  case 'Date':
		  {
		  	  operationType = 'date';
		  	  break;
		  }
	  }
	  // 拿操作符
	  getAllOperation().then((res) => {
		  const temp = operations;
		  Object.keys(res.payload).map((val) => {
			  if (val == operationType) {
				  console.log(temp);
				  temp[i] = res.payload[val];
				  console.log(temp);
				  setOperations(temp);
				  setLoading(!loading);
			  }
		  });
	  });
  };
  
  const save = () => {
	  // axios({
		 //  method:'GET',
		 //  url:'http://192.168.0.53:8080/api/v1/tablejob/filter?filterString=(%20%5Btime%5D.before(%222014-02-01%22)%20and%20%5Btime%5D.after(%222014-01-01%22)%20and%20%5Bcity%5D.containsAny(%22%E5%8D%97%2C%E9%97%A8%22)%20and%20%5BSO2%5D%20%3E%2010%20)&limit=2000&tableName=air.csv'
	  // }).then(res =>{
		 //  console.log(form.getFieldsValue())
		 //  //console.log(res.data.payload)
		 //  props.setData(res.data.payload)
	  // })
  };
  const onFinish = (values) => {
	  const filterStr = '';
	  // 记录了所有的数组的操作类型
	  console.log(operations);
	  const arr = [];
    for (let i = 0; i < count; i++) {
      Object.keys(operations[i]).map((val) => {
        if (values[`condition-${i}`] == val) {
          // console.log(val)
          console.log(operations[i][val]);
          arr.push(operations[i][val]);
          arr[i] = arr[i].replace('&*&',values[`column-${i}`]);
          arr[i] = arr[i].replace('#$#',values[`value-${i}`]);
          console.log(arr);
        }
      });
      // console.log(values[`filter-${i}`])
      // console.log(values[`column-${i}`])
      // console.log(values[`condition-${i}`])
      // console.log(values[`value-${i}`])
    }
    let str = arr[0];
    for (let i = 1; i < count; i++) {
      str = `${str } ${ values[`filter-${i}`] } ${ arr[i]}`;
    }
    console.log(str);
    const tableData = {
		  dataSource: 'databoard_gluttony.test2',
		  filter: str,
		  limit: 2000,
		  project: [
		    '*',
		  ],
    };
    getTable(tableData).then((res) => {
      props.setData(res);
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
          <Button type="primary" htmlType="submit" onClick={save}>
            确认
          </Button>
          <Button
            style={{
              margin: '0 8px',
            }}
            onClick={() => {
              form.resetFields();
            }}
          >
            重置
          </Button>
          <Button
            style={{
              margin: '0 8px',
            }}
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
