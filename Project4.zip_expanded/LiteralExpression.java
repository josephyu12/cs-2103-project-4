
public class LiteralExpression implements Expression {
	double _value;

	public LiteralExpression (String valueAsString) {
		_value = Double.parseDouble(valueAsString);
	}

	public LiteralExpression (double value) {
		_value = value;
	}
	
	@Override
	public Expression deepCopy() {
		// TODO Auto-generated method stub
		return new LiteralExpression(_value);
	}

	@Override
	public String convertToString(int indentLevel) {
		String tabs = "\t".repeat(indentLevel);
		return tabs + _value + "\n";
	}

	@Override
	public double evaluate(double x) {
		return _value;
	}

	@Override
	public Expression differentiate() {
		return new LiteralExpression(0);
	}
}
