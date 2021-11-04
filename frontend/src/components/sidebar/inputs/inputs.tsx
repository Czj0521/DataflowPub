import { Form, Input, Button, Select } from 'antd';

function InputsForm(props) {
  const [form] = Form.useForm();
  
  return (
    <div className="labelForm">
      <Form form={form} name="inputs">
        <Form.Item name="values" label="top transpose values">
          <Input />
        </Form.Item>
        <Form.Item name="rows" label="rows to analyze">
          <Input />
        </Form.Item>
      </Form>
    </div>
  );
}

export default InputsForm;
