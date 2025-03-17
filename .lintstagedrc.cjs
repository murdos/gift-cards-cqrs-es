module.exports = {
  '{src/**/,}*.{ts,vue}': ['eslint --fix', 'prettier --write'],
  '*.{md,json*,yml,yaml,html,css,scss,java,xml,feature}': ['prettier --write'],
};
