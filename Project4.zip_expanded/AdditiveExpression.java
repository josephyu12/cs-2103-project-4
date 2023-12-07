
public class AdditiveExpression implements Expression {
	Expression _left;
	Expression _right;

	public AdditiveExpression(Expression left, Expression right) {
		_left = left;
		_right = right;
	}
	
	@Override
	public Expression deepCopy() {
		// TODO Auto-generated method stub
		return new AdditiveExpression(_left.deepCopy(), _right.deepCopy());
	}

	@Override
	public String convertToString(int indentLevel) {
		String tabs = "\t".repeat(indentLevel);
		String firstLine = tabs + "+\n";
		String secondLine = _left.convertToString(indentLevel + 1) + "\n";
		String thirdLine = _right.convertToString(indentLevel + 1) + "\n";
		return firstLine + secondLine + thirdLine;
	}

	@Override
	public double evaluate(double x) {
		return _left.evaluate(x) + _right.evaluate(x);
	}

	@Override
	public Expression differentiate() {
		return new AdditiveExpression(_left.deepCopy(), _right.deepCopy());
	}

}
