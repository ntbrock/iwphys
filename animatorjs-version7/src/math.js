const { evaluate } = require('mathjs');

function math_sum(a,b) {
	var scope = { a : a, b : b };
	return evaluate('a+b', scope);
}

module.exports = math_sum;

