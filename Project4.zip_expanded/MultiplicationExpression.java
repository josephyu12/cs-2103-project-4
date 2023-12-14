/**
 * Represents the multiplication of two Expressions
 */
public class MultiplicationExpression extends BinaryExpression {

	public MultiplicationExpression(Expression left, Expression right) {
		_symbol = "*";
		_evalFcn = (leftVal, rightVal) -> leftVal * rightVal;
		_left = left;
		_right = right;

	}

	@Override
	public Expression deepCopy() {
		return new MultiplicationExpression(_left.deepCopy(), _right.deepCopy());
	}

	@Override
	public Expression differentiate() {
		Expression newLeft = new MultiplicationExpression(_left.deepCopy(), _right.differentiate());
		Expression newRight = new MultiplicationExpression(_left.differentiate(), _right.deepCopy());
		return new AdditionExpression(newLeft, newRight);
	}
}
