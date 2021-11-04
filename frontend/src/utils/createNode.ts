import { Markup } from '@antv/x6';

import {} from '@antv/x6/src/types/';

function createNode(graph, { width = 300, height = 300 } = {}, component, customConfig = {}) {
  return graph.createNode({
    width,
    height,
    shape: 'react-shape',
    data: {
      brush: false,
      dataset: {},
      item: {
        id: `${new Date().getTime()}`,
        option: {
          data: [],
          column: [],
        },
      },
      move: true,
    },
    // component: <BaseComponent  type={type} size={size}/>,
    component,
    portMarkup: [
      Markup.getForeignObjectMarkup(),
      {
        tagName: 'circle',
        selector: 'portBody',
      },
    ],
    attrs: {
      body: {
        fill: 'rgb(40,40,40)',
        // fill: 'transparent',
        stroke: '#000',
      },
    },
    ports: {
      items: [
        {
          group: 'out',
        },
        {
          group: 'in',
        },
        {
          group: 'in',
        },
      ],
      groups: {
        in: {
          position: {
            name: 'top',
          },
          attrs: {
            fo: {
              magnet: 'passive',
              width: 24,
              height: 24,
              fill: 'tranaparent',
              y: -30,
            },
          },
          zIndex: 10,
        },
        out: {
          position: {
            name: 'absolute',
            args: { x: '105%', y: '98%' },
          },
          attrs: {
            portBody: {
              magnet: true,
              r: 10,
              fill: 'rgb(40,40,40)',
              // y: 40
            },
          },
        },
      },   
    },
    ...customConfig,
  });
}


function createDragNode(graph, { width = 24, height = 24 } = {}, component, node) {
  return graph.createNode({
    width,
    height,
    shape: 'react-shape',
    component,
    data: {
      dropNode: node,
    },
  });
}

export { createDragNode, createNode };
