import { FC } from 'react'
import style from './index.module.scss'
import SideBar from './SideBar'

const Join: FC = (props) => {
    return <div className="hetu_basecomponent_wrapper">
        <SideBar />
        <div className={style.wrapper}>join</div>
    </div>
}

export default Join;