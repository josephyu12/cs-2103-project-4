
public class ExponentialExpression extends BinaryExpression {

	public ExponentialExpression(Expression left, Expression right) {
		_symbol = "^";
		_evalFcn = (leftVal, rightVal) -> Math.pow(leftVal, rightVal);
		_left = left;
		_right = right;
	}

	@Override
	public Expression deepCopy() {
		return new ExponentialExpression(_left.deepCopy(), _right.deepCopy());
	}

	@Override
	public Expression differentiate() {
		if (_right instanceof LiteralExpression) {
			// Use the power rule and the chain rule
			Expression exponent = new SubtractionExpression(_right.deepCopy(), new LiteralExpression(1));
			Expression powerRuleResult = new ExponentialExpression(_left.deepCopy(), exponent);
			return new MultiplicationExpression(_left.differentiate(), powerRuleResult);
		} else if (_left instanceof LiteralExpression) {
			// Derivative of a^x 
			Expression logExpression = new LogarithmicExpression(_left.deepCopy());
			Expression expExpression = new ExponentialExpression(_left.deepCopy(), _right.deepCopy());
			return new MultiplicationExpression(logExpression, expExpression);
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
