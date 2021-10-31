import { FC } from 'react'
import style from './index.module.scss'
<<<<<<< 7ff9462e30d12722a64d89abe5f3aa45cc8441c2
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
=======
import SideBar from './SideBar'

const Join: FC = (props) => {
    return <div className="hetu_basecomponent_wrapper">
        <SideBar />
        <div className={style.wrapper}>join</div>
    </div>
>>>>>>> refactor:SideItem
}

export default Join;