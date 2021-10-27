export default function getParenthesesStr(text) {
  const result = text.match(/\(([^)]*)\)/);

  return result;
}
