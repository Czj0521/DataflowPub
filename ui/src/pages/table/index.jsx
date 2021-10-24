import {PureComponent,useState,useEffect} from 'react';
import {Layout} from 'antd'
import HeaderMenu from '../../component/header/headermenu'
import SideMenu from './component/sidemenu'

const {Header,Sider,Content} = Layout

export default function TableCom(props){

    return(
        <Layout>
            <Header>
                <HeaderMenu/>
            </Header>
            <Layout>
                <Sider>
                    <SideMenu/>
                </Sider>
                <Content>

                </Content>
            </Layout>
        </Layout>
    )
}

