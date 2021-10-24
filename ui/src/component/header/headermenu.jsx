import {Menu} from 'antd'
import {Link} from 'react-router-dom'
import menuConfig from './menuconfig'

const {SubMenu, Item} = Menu 

export default function(props){

    const getMenu = () =>{
        return menuConfig.map(item=>{
            if(item.children.length !== 0){
                return (<SubMenu key={item.key}>
                    {getMenu(item.children)}
                </SubMenu>)
            }else{
                return (
                    <Item key={item.key}>
                        <Link to={item.index}>{item.title}</Link>
                    </Item>
                )
            }
        })
    }

    return (
        <Menu mode='horizontal' theme='dark' id='head'>
            {getMenu()}
        </Menu>
    )
}