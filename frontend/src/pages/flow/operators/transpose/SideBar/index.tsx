import { FC } from 'react';
import SideItem from '../../../components/SideItem';
import ColumnOrGroupForm from '../../../components/sidebar/columnOrGroup/columnOrGroup';
import InputsForm from '../../../components/sidebar/inputs';
import AdvancedSearchForm from '../../dataset/sidebar/attributes/form';

type SideProps = {
  column: Array<any>
}

const SideBar: FC<SideProps> = (props) => {
  const { column } = props;
  return (
    <div className="hetu_table_sidebar_wrapper">
      <SideItem name="column" width={200} height={200}>
        <ColumnOrGroupForm column={column} labelName="column" />
      </SideItem>
      <SideItem name="group by" width={200} height={300}>
        <ColumnOrGroupForm column={column} labelName="group by" />
      </SideItem>
      <SideItem name="attribute" width={300} height={300}>
        <AdvancedSearchForm />
      </SideItem>
      <SideItem name="inputs" width={300} height={300}>
        <InputsForm />
      </SideItem>
    </div>
  );
};

export default SideBar;
