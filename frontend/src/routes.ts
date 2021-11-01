import { IRouterConfig } from 'ice';

import NotFound from '@/components/NotFound';
import Layout from '@/Layouts/BasicLayout';
import Dashboard from '@/pages/Dashboard';
import Flow from '@/pages/Flow';
import Home from '@/pages/Home';

const routerConfig: IRouterConfig[] = [
  {
    path: '/flow',
    component: Flow,
  },
  {
    path: '/',
    component: Layout,
    children: [{
      path: '/dashboard',
      component: Dashboard,
    }, {
      path: '/',
      exact: true,
      component: Home,
    }, {
      component: NotFound,
    }],
  },

];

export default routerConfig;
