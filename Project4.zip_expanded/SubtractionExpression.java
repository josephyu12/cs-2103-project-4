/**
 * Represents the 
 */
public class SubtractionExpression extends BinaryExpression {

	/**
	 * The expression will be interpreted as minuend minus subtrahend
	 * @param minuend 
	 * @param subtrahend
	 */
	public SubtractionExpression(Expression minuend, Expression subtrahend) {
		_symbol = "-";
		_evalFcn = (leftVal, rightVal) -> leftVal - rightVal;
		_left = minuend;
		_right = subtrahend;
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
