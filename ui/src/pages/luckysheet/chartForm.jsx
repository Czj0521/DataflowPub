import {Button, Form,Select,Input} from 'antd'
import {useImperativeHandle, forwardRef, useState} from 'react'

const {Item} = Form
const {Option} = Select

export default forwardRef((props,ref) => {

    useImperativeHandle(ref,()=>({
        values
    }))

    const [values, setValues] = useState({})

    const [form] = Form.useForm();
    const layout = {
        labelCol: { span: 8 },
        wrapperCol: { span: 16 },
      };

    const finish = () => {
        form.validateFields().then(values=>{
            // console.log(values)
            setValues(values)
            form.resetFields()
            props.handleCancel()
        })

    }

    return (
        <Form form={form} {...layout} onFinish={finish}>
            <Item name='type' label='图表类型'>
                <Select>
                    <Option value='pie'>饼图</Option>
                    <Option value='line'>折线图</Option>
                    <Option value='bar'>柱状图</Option>
                    <Option value='scatter'>散点图</Option>
                    <Option value='heat'>热力图</Option>
                </Select>   
            </Item>
            <Item noStyle shouldUpdate={(prevValues, curValues) => prevValues.type !== curValues.type}>
                {
                    ({ getFieldValue })=>
                    getFieldValue('type') === 'pie' &&
                    <div>
                        <Item label='数据列' name='value'>
                            <Input placeholder='请输入数据对应第几列'/>
                        </Item>
                        {/* <Item label='名称列' name='name'>
                            <Input placeholder='请输入名称对应第几列'/>
                        </Item> */}
                    </div> 
                }
            </Item>
            <Item noStyle shouldUpdate={(prevValues, curValues) => prevValues.type !== curValues.type}>
                {
                     ({ getFieldValue })=>
                    getFieldValue('type') === 'line' &&
                    <div>
                        <Item label='x轴数据' name='xData'>
                            <Input placeholder='请输入x轴数据对应第几列'/>
                        </Item>
                        <Item label='y轴数据' name='yData'>
                            <Input placeholder='请输入y轴数据对应第几列'/>
                        </Item>
                    </div>  
                }
            </Item>
            
            <Item>
                <Button type='primary' htmlType='submit'>确定</Button>
                <Button onClick={props.handleCancel}>取消</Button>
            </Item>
        </Form>
    )
}) 