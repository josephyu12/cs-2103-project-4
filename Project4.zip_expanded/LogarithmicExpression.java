/**
 * Represents a logarithm with base e. If another base is desired, use the change of base formula. 
 * https://en.wikipedia.org/wiki/List_of_logarithmic_identities#Changing_the_base. 
 */
public class LogarithmicExpression extends UnaryExpression {
	
	public LogarithmicExpression(Expression next) {
		_symbol = "log";
		_evalFcn = nextVal -> Math.log(nextVal);
		_next = next;
	}

	@Override
	public Expression deepCopy() {
		return new LogarithmicExpression(_next.deepCopy());
	}

	@Override
	public Expression differentiate() {
		// Using the chain rule
		Expression dOutside = new DivisionExpression(new LiteralExpression(1), _next.deepCopy());
		Expression dInside = _next.differentiate();
		return new MultiplicationExpression(dOutside, dInside);
	}
}
