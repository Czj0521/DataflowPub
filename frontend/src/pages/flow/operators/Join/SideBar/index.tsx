<<<<<<< HEAD
import React, { FC } from 'react';
=======
import { FC } from 'react';
>>>>>>> 88f9208ca51d7f857d9b712f603ae51521b43a52
import SideItem from '../../../components/SideItem';
import JoinType from './JoinType';
import JoinKeys from './JoinKeys';
import Inputs from './Inputs';

<<<<<<< HEAD
const SideBar: FC = (props) => {
=======
const SideBar: FC = () => {
>>>>>>> 88f9208ca51d7f857d9b712f603ae51521b43a52
    return <div className="hetu_table_sidebar_wrapper">
        <SideItem name="join type" width={180} height={200}>
            <JoinType />
        </SideItem>
        <SideItem name="join keys" width={300} height={300}>
            <JoinKeys />
        </SideItem>
        <SideItem name="inputs" width={300} height={300}>
            <Inputs />
        </SideItem>
    </div>
}

export default SideBar;