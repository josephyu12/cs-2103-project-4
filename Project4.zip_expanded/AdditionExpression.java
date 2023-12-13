public class AdditionExpression extends BinaryExpression {
	public AdditionExpression(Expression left, Expression right) {
		_symbol = "+";
		_evalFcn = (leftVal, rightVal) -> leftVal + rightVal;
		_left = left;
		_right = right;
	}

	@Override
	public Expression deepCopy() {
		return new AdditionExpression(_left.deepCopy(), _right.deepCopy());
	}

	public Expression differentiate() {
		return new AdditionExpression(_left.differentiate(), _right.differentiate());
	}

}
