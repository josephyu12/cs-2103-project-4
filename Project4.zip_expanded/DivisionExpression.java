/**
 * A class used to represent the division of two Expressions
 */
public class DivisionExpression extends BinaryExpression {

	/**
	 * 
	 * @param dividend Expression in the numerator
	 * @param divisor  Expression in the denominator
	 */
	public DivisionExpression(Expression dividend, Expression divisor) {
		_symbol = "/";
		_evalFcn = (leftVal, rightVal) -> leftVal / rightVal;
		_left = dividend;
		_right = divisor;
	}

	@Override
	public Expression deepCopy() {
		return new DivisionExpression(_left.deepCopy(), _right.deepCopy());
	}

	@Override
	public Expression differentiate() {
		Expression numeratorLeft = new MultiplicationExpression(_left.differentiate(), _right.deepCopy());
		Expression numeratorRight = new MultiplicationExpression(_left.deepCopy(), _right.deepCopy());
		Expression denominator = new ExponentialExpression(_right.deepCopy(), new LiteralExpression(2));
		return new DivisionExpression(new SubtractionExpression(numeratorLeft, numeratorRight), denominator);
	}

}
