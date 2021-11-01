import { Form, Input, Divider } from 'antd';
import { FC } from 'react';

const InputsForm: FC = (props) => {
  const [form] = Form.useForm();
  
  return (
    <div className="labelForm">
      <Divider style={{ color: '#eee', borderColor: '#eee', fontSize: 12 }}>
        inputs
      </Divider>
      <Form form={form} name="inputs" layout="vertical">
        <Form.Item name="values" label="top transpose values">
          <Input />
        </Form.Item>
        <Form.Item name="rows" label="rows to analyze">
          <Input />
        </Form.Item>
      </Form>
    </div>
  );
};

export default InputsForm;
