
public class SubtractionExpression extends BinaryExpression {

	public SubtractionExpression(Expression left, Expression right) {
		_symbol = "-";
		_evalFcn = (leftVal, rightVal) -> leftVal - rightVal;
		_left = left;
		_right = right;
	}

	@Override
	public Expression deepCopy() {
		return new SubtractionExpression(_left.deepCopy(), _right.deepCopy());
	}

	@Override
	public Expression differentiate() {
		return new SubtractionExpression(_left.deepCopy(), _right.deepCopy());
	}

}
