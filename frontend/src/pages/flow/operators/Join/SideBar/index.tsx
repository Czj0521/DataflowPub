import { FC } from 'react';
import SideItem from '../../../components/SideItem';
import JoinType from './JoinType';

const SideBar: FC = () => {
    return <div className="hetu_table_sidebar_wrapper">
        <SideItem name="join type" width={180} height={200}>
            <JoinType />
        </SideItem>
        <SideItem name="join keys" width={300} height={300}>join type</SideItem>
        <SideItem name="inputs" width={300} height={300}>join type</SideItem>
    </div>
}

export default SideBar;