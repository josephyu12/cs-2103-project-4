
import java.util.function.BiFunction;

public abstract class BinaryExpression implements Expression {
	protected String _symbol;
	protected BiFunction<Double, Double, Double> _evalFcn;
	protected Expression _left;
	protected Expression _right;

	@Override
	public String convertToString(int indentLevel) {
		String tabs = "\t".repeat(indentLevel);
		String firstLine = tabs + _symbol + "\n";
		String secondLine = _left.convertToString(indentLevel + 1);
		String thirdLine = _right.convertToString(indentLevel + 1);
		return firstLine + secondLine + thirdLine;
	}

	@Override
	public double evaluate(double x) {
		return _evalFcn.apply(_left.evaluate(x), _right.evaluate(x));
	}
}
