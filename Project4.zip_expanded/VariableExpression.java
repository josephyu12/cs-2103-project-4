
public class VariableExpression implements Expression {

	@Override
	public Expression deepCopy() {
		return new VariableExpression();
	}

	@Override
	public String convertToString(int indentLevel) {
		String tabs = "\t".repeat(indentLevel);
		return tabs + "x";
	}

	@Override
	public double evaluate(double x) {
		return x;
	}

	@Override
	public Expression differentiate() {
		return new LiteralExpression(1);
	}
}
