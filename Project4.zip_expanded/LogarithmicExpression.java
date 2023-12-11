
public class LogarithmicExpression extends UnaryExpression {

	public LogarithmicExpression(Expression _next) {
		
	}

	@Override
	public Expression deepCopy() {
		return new LogarithmicExpression(_next);
	}

	@Override
	public Expression differentiate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
