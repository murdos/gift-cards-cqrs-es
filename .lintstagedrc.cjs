module.exports = {
  '{src/**/,}*.{ts,vue}': ['eslint --fix', 'prettier --write'],
  '*.{md,json*,yml,html,css,scss,java,xml,feature}': ['prettier --write'],
};
