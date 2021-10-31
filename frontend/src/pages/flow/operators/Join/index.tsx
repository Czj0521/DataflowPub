import { FC } from 'react'
import style from './index.module.scss'
<<<<<<< 01e4cd086270a5ceb36261011fd9f3728335e619
import SideBar from './SideBar'
import '../../components/style.scss';

const Join: FC = (props) => {
    return <div className="hetu_basecomponent_wrapper">
        <SideBar />
        <div className={style.wrapper}>join</div>
    </div>
=======

const Join: FC = (props) => {
    return <div className={style.wrapper}>join</div>
>>>>>>> 'feat:join-init'
}

export default Join;