
import java.util.function.Function;

public abstract class UnaryExpression implements Expression {
	// superclass for unary operators like logarithm and parentheses
	protected String _symbol;
	protected Function<Double, Double> _evalFcn;
	protected Expression _next;

	@Override
	public String convertToString(int indentLevel) {
		String tabs = "\t".repeat(indentLevel);
		String firstLine = tabs + _symbol + "\n";
		String secondLine = _next.convertToString(indentLevel + 1);
		return firstLine + secondLine;
	}

	@Override
	public double evaluate(double x) {
		return _evalFcn.apply(_next.evaluate(x));
	}
}
