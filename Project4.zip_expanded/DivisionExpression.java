
public class DivisionExpression extends BinaryExpression {

	public DivisionExpression(Expression left, Expression right) {
		_symbol = "/";
		_evalFcn = (leftVal, rightVal) -> leftVal / rightVal;
		_left = left;
		_right = right;
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
