/**
 * A class used to represent exponentiation with Expression as the base, and the other as the exponent
 */
public class ExponentialExpression extends BinaryExpression {

	public ExponentialExpression(Expression base, Expression exponent) {
		_symbol = "^";
		_evalFcn = (leftVal, rightVal) -> Math.pow(leftVal, rightVal);
		_left = base;
		_right = exponent;
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
			Expression logExpression = new ParentheticalExpression(new LogarithmicExpression(_left.deepCopy()));
			Expression expExpression = new ExponentialExpression(_left.deepCopy(), _right.deepCopy());
			return new MultiplicationExpression(logExpression, expExpression);
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
