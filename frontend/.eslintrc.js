const { getESLintConfig } = require('@iceworks/spec');

// https://www.npmjs.com/package/@iceworks/spec
module.exports = getESLintConfig('react-ts');

module.exports = getESLintConfig('rax', {
  // custom config it will merge into main config
  
  rules: {
    '@iceworks/best-practices/rule-name': 'off',
    'no-console': 'off',
    'comma-spacing': 'off',
    'no-trailing-spaces': 'off',
    'no-mixed-spaces-and-tabs': 'off',

  },
});
