import { IRouterConfig } from 'ice';

import NotFound from '@/components/NotFound';
import Layout from '@/Layouts/BasicLayout';
import Dashboard from '@/pages/Dashboard';
<<<<<<< HEAD
import Flow from '@/pages/Flow';
=======
import Flow from '@/pages/flow';
>>>>>>> 88f9208ca51d7f857d9b712f603ae51521b43a52
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
