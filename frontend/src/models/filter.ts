import { getAllOperation } from '@/api/table';

export default {
  // 定义model的初始state
  state: {
    operations: { date: {}, boolean: {}, numeric: {}, string: {} },
  },

  // 定义改变该模型状态的纯函数
  reducers: {
    update(prevState, payload) {
      return {
        ...prevState,
        ...payload,
      };
    },
    logInfo() {
      console.log('loginfo');
    },
  },

  // 定义处理该模型副作用的函数
  effects: (dispatch) => ({
    async getAllOperationFn() {
      const res = await getAllOperation();
      dispatch.filter.update({
        operations: res.payload,
      });
    },
  }), 
};
