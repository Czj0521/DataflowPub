import { FC } from 'react';
import { Checkbox, Form, Input, Divider} from 'antd';

const Inputs: FC = () => {
    return <div style={{ padding: 10 }}>
        <Divider style={{ color: '#eee', borderColor: '#eee', fontSize: 12 }}>
            prefixes inputs
        </Divider>
        <Form
            name="basic"
            initialValues={{
                include_prefixes: true,
                prefixs_all: true,
                left_prefix: 'left_',
                right_prefix: 'right_'
            }}
        >
            <Form.Item name="include_prefixes" >
                <Checkbox>include prefixes</Checkbox>
            </Form.Item>
            <Form.Item name="prefixs_all" >
                <Checkbox>prefixs all?</Checkbox>
            </Form.Item>
            <Form.Item label="left prefix" name="left_prefix">
                <Input />
            </Form.Item>
            <Form.Item label="right prefix" name="right_prefix">
                <Input />
            </Form.Item>
        </Form>
    </div>
}

export default Inputs;