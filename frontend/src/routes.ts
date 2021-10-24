import { IRouterConfig } from 'ice';
import Layout from '@/Layouts/BasicLayout';
import Dashboard from '@/pages/Dashboard';
import Home from '@/pages/Home';
import Flow from '@/pages/Flow';
import NotFound from '@/components/NotFound';

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
