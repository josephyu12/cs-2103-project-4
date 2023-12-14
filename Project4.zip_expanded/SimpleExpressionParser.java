import java.util.ArrayList;
import java.util.function.*;

public class SimpleExpressionParser implements ExpressionParser {
	private ParseSequence _sequence;
	private ParseSequence _parserS;
	private ParseSequence _parserM;
	private ParseSequence _parserE;
	private ParseSequence _parserP;
	private ParseSequence _parserL;
	private ParseSequence _parserV;

	private class ParseSequence {
		private ArrayList<Function<String, Expression>> _parseFcns;
		private ArrayList<ParseSequence> _parseSequences;

		public ParseSequence() {
			_parseFcns = new ArrayList<Function<String, Expression>>();
		}

		public void addFcn(Function<String, Expression> aParseFcn) {
			_parseFcns.add(aParseFcn);
		}

		public void addSequence(ParseSequence aParseSequence) {
			_parseSequences.add(aParseSequence);
		}

		public Expression parse(String str) {
			for (Function<String, Expression> aParseFcn : _parseFcns) {
				Expression expression = aParseFcn.apply(str);
				if (expression != null) {
					return expression;
				}
			}
			for (ParseSequence aParseSequence : _parseSequences) {
				Expression expression = aParseSequence.parse(str);
				if (expression != null) {
					return expression;
				}
			}
			return null;
		}

		public Function<String, Expression> getParseFcn() {
			return str -> parse(str);
		}
	}

	/*-
	 * Attempts to create an expression tree from the specified String.
	 * Throws a ExpressionParseException if the specified string cannot be parsed.
		 * Grammar:
		 * S -> A | P
		 * A -> A+M | A-M | M
		 * M -> M*E | M/E | E
		 * E -> P^E | P | log(P)
		 * P -> (S) | L | V
		 * L -> <float>
		 * V -> x
	 * @param str the string to parse into an expression tree
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse(String str) throws ExpressionParseException {
		str = str.replaceAll(" ", "");
		Expression expression = parseAdditiveExpression(str);

		if (expression == null) {
			expression = parseParentheticalExpression(str);
		}

		if (expression == null) {
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}

		return expression;
	}

	protected Expression parseAdditiveExpression(String str) {
		Expression expression = null;
		Expression left;
		Expression right;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '+') {
				left = parseAdditiveExpression(str.substring(0, i));
				right = parseMultiplicationExpression(str.substring(i + 1, str.length()));
				if (!(left == null || right == null)) {
					expression = new AdditionExpression(left, right);
				}
			}
		}
		if (expression == null) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '-') {
					left = parseAdditiveExpression(str.substring(0, i));
					right = parseMultiplicationExpression(str.substring(i + 1, str.length()));
					if (!(left == null || right == null)) {
						expression = new SubtractionExpression(left, right);
					}
				}
			}
		}
		if (expression == null) {
			expression = parseMultiplicationExpression(str);
		}

		return expression;

	}

	protected /* Multiplication */ Expression parseMultiplicationExpression(String str) {
		Expression expression = null;
		Expression left;
		Expression right;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '*') {
				left = parseMultiplicationExpression(str.substring(0, i));
				right = parseExponentialExpression(str.substring(i + 1, str.length()));
				if (!(left == null || right == null)) {
					expression = new MultiplicationExpression(left, right);
				}
			}
		}
		if (expression == null) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '/') {
					left = parseMultiplicationExpression(str.substring(0, i));
					right = parseExponentialExpression(str.substring(i + 1, str.length()));
					if (!(left == null || right == null)) {
						expression = new DivisionExpression(left, right);
					}
				}
			}
		}
		if (expression == null) {
			expression = parseExponentialExpression(str);
		}

		return expression;

	}

	protected /* Exponential */ Expression parseExponentialExpression(String str) {
		Expression expression = null;
		Expression left;
		Expression right;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '^') {
				left = parseParentheticalExpression(str.substring(0, i));
				right = parseExponentialExpression(str.substring(i + 1, str.length()));
				if (!(left == null || right == null)) {
					expression = new ExponentialExpression(left, right);
				}
			}
		}
		if (expression == null) {
			expression = parseParentheticalExpression(str);
		}
		if (expression == null) {
			if (str.startsWith("log")) {
				expression = new LogarithmicExpression(parseParentheticalExpression(str.substring(3, str.length())));
			}
		}
		return expression;
	}

	protected /* Parenthetical */ Expression parseParentheticalExpression(String str) {
		Expression expression = null;
		if (str.length() >= 2 && str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
			try {
				// TODO: after implementing parseSum, change parse() to parseSum()
				expression = new ParentheticalExpression(parse(str.substring(1, str.length() - 1)));
			} catch (ExpressionParseException e) {
			}
		}
		if (expression == null) {
			expression = parseVariableExpression(str);
		}
		if (expression == null) {
			expression = parseLiteralExpression(str);
		}
		return expression;
	}

	// TODO: once you implement a VariableExpression class, fix the return-type
	// below.
	protected /* Variable */Expression parseVariableExpression(String str) {
		if (str.equals("x")) {
			return new VariableExpression();
		}
		return null;
	}

	// TODO: once you implement a LiteralExpression class, fix the return-type
	// below.
	protected /* Literal */Expression parseLiteralExpression(String str) {
		// From
		// https://stackoverflow.com/questions/3543729/how-to-check-that-a-string-is-parseable-to-a-double/22936891:
		final String Digits = "(\\p{Digit}+)";
		final String HexDigits = "(\\p{XDigit}+)";
		// an exponent is 'e' or 'E' followed by an optionally
		// signed decimal integer.
		final String Exp = "[eE][+-]?" + Digits;
		final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading "whitespace"
				"[+-]?(" + // Optional sign character
				"NaN|" + // "NaN" string
				"Infinity|" + // "Infinity" string

				// A decimal floating-point string representing a finite positive
				// number without a leading sign has at most five basic pieces:
				// Digits . Digits ExponentPart FloatTypeSuffix
				//
				// Since this method allows integer-only strings as input
				// in addition to strings of floating-point literals, the
				// two sub-patterns below are simplifications of the grammar
				// productions from the Java Language Specification, 2nd
				// edition, section 3.10.2.

				// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
				"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

				// . Digits ExponentPart_opt FloatTypeSuffix_opt
				"(\\.(" + Digits + ")(" + Exp + ")?)|" +

				// Hexadecimal strings
				"((" +
				// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "(\\.)?)|" +

				// 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

				")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional trailing "whitespace"

		if (str.matches(fpRegex)) {
			// return null;
			// TODO: Once you implement LiteralExpression, replace the line above with the
			// line below:
			return new LiteralExpression(str);
		}
		return null;
	}

	public static void main(String[] args) throws ExpressionParseException {
		final ExpressionParser parser = new SimpleExpressionParser();
		Expression e = parser.parse("(x+2)^(2)");
		System.out.println(e.convertToString(0));
		e.evaluate(0);
		System.out.println(e.evaluate(0));
	}
}