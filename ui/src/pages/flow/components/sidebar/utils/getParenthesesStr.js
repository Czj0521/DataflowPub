export default function getParenthesesStr(text) {
    var result = text.match(/\(([^)]*)\)/);

    return result
}