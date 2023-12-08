public class AdditiveExpression extends BinaryExpression {
	public AdditiveExpression(Expression left, Expression right) {
		_symbol = "+";
		_evalFcn = (leftVal, rightVal) -> leftVal + rightVal;
		_left = left;
		_right = right;
	}

	@Override
	public Expression deepCopy() {
		return new AdditiveExpression(_left.deepCopy(), _right.deepCopy());
	}

	public Expression differentiate() {
		return new AdditiveExpression(_left.deepCopy(), _right.deepCopy());
	}

}
