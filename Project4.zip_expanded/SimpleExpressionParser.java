import java.util.ArrayList;
import java.util.function.*;

public class SimpleExpressionParser implements ExpressionParser {

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
		// remove whitespace
		str = str.replaceAll(" ", "");
		// try to parse as additive expression
		Expression expression = parseAdditiveExpression(str);

		// if that doesn't work, parse as parenthetical expression
		if (expression == null) {
			expression = parseParentheticalExpression(str);
		}

		// otherwise, throw exception
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
			// search until finds a + sign, then lefts the left be additive parsing and right be multiplication parsing
			if (str.charAt(i) == '+') {
				left = parseAdditiveExpression(str.substring(0, i));
				right = parseMultiplicationExpression(str.substring(i + 1, str.length()));
				// if both sides cannot be parsed, make a new addition expression
				if (!(left == null || right == null)) {
					expression = new AdditionExpression(left, right);
				}
			}
		}
		// if no addition sign is found, try the same for a subtraction sign
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
		// if nothing is working, parse this as a multiplication expression
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
			// search until a multiplication symbol is found
			if (str.charAt(i) == '*') {
				left = parseMultiplicationExpression(str.substring(0, i));
				right = parseExponentialExpression(str.substring(i + 1, str.length()));
				// if they are not null, make a new multiplication expression
				if (!(left == null || right == null)) {
					expression = new MultiplicationExpression(left, right);
				}
			}
		}
		// if no multiplication symbol found, try the same for division
		if (expression == null) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '/') {
					// if division symbol found try further parsing as multiplication expression and exponential on right
					left = parseMultiplicationExpression(str.substring(0, i));
					right = parseExponentialExpression(str.substring(i + 1, str.length()));
					// if either sides cannot be parsed further, make a new division expression
					if (!(left == null || right == null)) {
						expression = new DivisionExpression(left, right);
					}
				}
			}
		}
		// if neither symbol is found, parse it as an exponential expression
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
			// search for the ^ character, if found parse left as parenthetical and right as exponential expression
			if (str.charAt(i) == '^') {
				left = parseParentheticalExpression(str.substring(0, i));
				right = parseExponentialExpression(str.substring(i + 1, str.length()));
				// if either side cannot be parsed further, make new exponential expression
				if (!(left == null || right == null)) {
					expression = new ExponentialExpression(left, right);
				}
			}
		}
		// if ^ character not found, try parsing as parenthetical
		if (expression == null) {
			expression = parseParentheticalExpression(str);
		}
		// if parenthetical parsing does not work and string starts with log, try making it into a logarithm expression
		if (expression == null) {
			if (str.startsWith("log")) {
				expression = new LogarithmicExpression(parseParentheticalExpression(str.substring(3, str.length())));
			}
		}
		return expression;
	}

	protected /* Parenthetical */ Expression parseParentheticalExpression(String str) {
		Expression expression = null;
		// if the length is at least 2 for two parentheses and both starts and ends in a parenthesis, try making a new parenthetical expression
		// with the top-level "parse" function run again
		if (str.length() >= 2 && str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
			try {
				expression = new ParentheticalExpression(parse(str.substring(1, str.length() - 1)));
			} catch (ExpressionParseException e) {
			}
		}
		// if this is not the case, try parsing as a variable
		if (expression == null) {
			expression = parseVariableExpression(str);
		}
		// if neither options work, try parsing as a literal
		if (expression == null) {
			expression = parseLiteralExpression(str);
		}
		return expression;
	}

	protected /* Variable */Expression parseVariableExpression(String str) {
		// if the string is x, return the variable expression, otherwise do not return anything
		if (str.equals("x")) {
			return new VariableExpression();
		}
		return null;
	}

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