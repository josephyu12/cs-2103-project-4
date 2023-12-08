
public class ParentheticalExpression extends UnaryExpression {
	public ParentheticalExpression(Expression next) {
		_symbol = "()";
		_evalFcn = nextVal -> nextVal;
		_next = next;
	}
	@Override
	public Expression deepCopy() {
		return new ParentheticalExpression(_next);
	}

	@Override
	public Expression differentiate() {
		return _next.differentiate();
	}

}
