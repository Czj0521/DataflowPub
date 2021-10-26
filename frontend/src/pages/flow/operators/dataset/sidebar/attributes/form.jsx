import { useState ,useEffect} from 'react';
import { Form, Row, Col, Input, Button ,Select} from 'antd';
import { Checkbox, Divider } from 'antd';
import { DownOutlined, UpOutlined } from '@ant-design/icons';
import {getTableColumn,getAllOperation,getTable} from '../../../../../../api/table.js'
import getParenthesesStr from '../utils/getParenthesesStr.js'
import './form.scss'

const CheckboxGroup = Checkbox.Group;
const { Option } = Select;
const AdvancedSearchForm = (props) => {
  
		console.log(props)
	  const [checkedList, setCheckedList] = useState([]);
	  const [indeterminate, setIndeterminate] = useState(true);
	  const [checkAll, setCheckAll] = useState(false);
	  const [column,setColumn]=useState([])
	  const [columnType,setColumnType]=useState(null)

	  const onChange = list => {
	    setCheckedList(list);
	    setIndeterminate(!!list.length && list.length < column.length);
	    setCheckAll(list.length === column.length);
		console.log(list)
	  };
	
	  const onCheckAllChange = e => {
	    setCheckedList(e.target.checked ? column : []);
	    setIndeterminate(false);
	    setCheckAll(e.target.checked);
	  };
	
  useEffect(() => {
  	getTableColumn({datasource:'databoard_gluttony.test2'}).then(res=>{
		const tempColumn = []
		Object.keys(res).map(val => {
			tempColumn.push(val)
		})
		setColumn(tempColumn)
		setColumnType(res)
	})
  }, [])
  

  
  const [form] = Form.useForm();

  //selectNum:第几个值


  const onFinish = (values) => {
	console.log(checkedList)
	const list = checkedList.map(val =>{
		return (		{
						  key:val,
						  dataIndex:val,
						  title:val
						}
				)
	})
	const data = 
		{
		  dataSource: "databoard_gluttony.test2",
		  limit: 2000,
		  project: checkedList
		}
		console.log(data)
		getTable(data).then(res => {
			props.setData(res)
			props.setColumn(list)
		})
	// props.setData([{name:'lxz'}])
	// props.setColumn(list)
	
  };

  return (
    <Form
	  style={{padding:10}}
      form={form}
      name="advanced_search"
      className="ant-advanced-search-form"
      onFinish={onFinish}
    >
	<Row>
      <Col>
		  <Checkbox indeterminate={indeterminate} onChange={onCheckAllChange} checked={checkAll}>
		          全选
		        </Checkbox>
		        <Divider />
		        <CheckboxGroup options={column}  value={checkedList} onChange={onChange} >
					
				</CheckboxGroup>
        </Col>
      </Row>
	  <Row>
	          <Col span={24} style={{ textAlign: 'right' }}>
	            <Button  type="primary" htmlType="submit">
	              Search
	            </Button>
	          </Col>
	        </Row>
    </Form>
  );
};


export default  AdvancedSearchForm