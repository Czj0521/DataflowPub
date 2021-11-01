import { FC } from 'react';
import SideItem from '../../../components/SideItem';
import JoinType from './JoinType';

<<<<<<< 778f8c09b24f74f24980864340ace639fa65993d
<<<<<<< 7ff9462e30d12722a64d89abe5f3aa45cc8441c2
const SideBar: FC = () => {
=======
const SideBar: FC = (props) => {
>>>>>>> refactor:SideItem
=======
const SideBar: FC = () => {
>>>>>>> fix:层级遮挡
    return <div className="hetu_table_sidebar_wrapper">
        <SideItem name="join type" width={180} height={200}>
            <JoinType />
        </SideItem>
        <SideItem name="join keys" width={300} height={300}>join type</SideItem>
        <SideItem name="inputs" width={300} height={300}>join type</SideItem>
    </div>
}

export default SideBar;