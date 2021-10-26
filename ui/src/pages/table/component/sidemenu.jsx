import {Menu, Row, Col} from 'antd'
import NodeItem from './nodeitem'
import './index.scss'


export default function (props){

    return(    
        <Menu mode='inline' theme='light' id='sidemenu' style={{height:'calc(100vh - 64px)', backgroundColor:'white'}}>
            <Menu.SubMenu key='start' title='分栏'>
                <Menu.Item key='start-1'>
                    <Row gutter={[8,8]}>
                        <Col span={8}>
                            <NodeItem></NodeItem>
                        </Col>
                    </Row>
                </Menu.Item>
            </Menu.SubMenu>
        </Menu>
    )
}