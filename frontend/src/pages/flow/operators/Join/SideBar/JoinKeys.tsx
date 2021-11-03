import { Radio, Space, Divider } from 'antd';
import React from 'react';

class JoinType extends React.Component {
    state = {
        value: 1,
    };

    onChange = e => {
        this.setState({
            value: e.target.value,
        });
    };

    render() {
        const { value } = this.state;
        return (
            <div style={{padding: 10, width: '100%', height: '100%'}}>
                <Divider style={{ color: '#eee', borderColor: '#eee', fontSize: 12 }}>
                    join type
                </Divider>
                <Radio.Group onChange={this.onChange} value={value} >
                    <Space direction="vertical">
                        <Radio value="inner" style={{color: '#eee'}}>inner</Radio>
                        <Radio value="left outer" style={{color: '#eee'}}>left outer</Radio>
                        <Radio value="right outer" style={{color: '#eee'}}>right outer</Radio>
                        <Radio value="full outer" style={{color: '#eee'}}>full outer</Radio>
                    </Space>
                </Radio.Group>
            </div>
        );
    }
}

export default JoinType;