import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;
import java.io.*;

/**
 * Some code to help you test Project 4.
 */
public class ExpressionParserTester {
	private ExpressionParser _parser;

	@BeforeEach
	/**
	 * Instantiates the actors and movies graphs
	 */
	public void setUp() throws IOException {
		_parser = new SimpleExpressionParser();
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpressionAdditive() throws ExpressionParseException {
		final String expressionStr = "x+x";
		final String parseTreeStr = "+\n\tx\n\tx\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpressionMultiplicative() throws ExpressionParseException {
		final String expressionStr = "13*x";
		final String parseTreeStr = "*\n\t13.0\n\tx\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExpressionAdditiveParentheses() throws ExpressionParseException {
		final String expressionStr = "4+(x+5+x)";
		final String parseTreeStr = "+\n" + "\t4.0\n" + "\t()\n" + "\t\t+\n" + "\t\t\t+\n" + "\t\t\t\tx\n" + "\t\t\t\t5.0\n"
				+ "\t\t\tx\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testExpressionPolynomial() throws ExpressionParseException {
		final String expressionStr = "10*x^3 + 2*(15+x)";
		final String parseTreeStr = "+\n\t*\n\t\t10.0\n\t\t^\n\t\t\tx\n\t\t\t3.0\n\t*\n\t\t2.0\n\t\t()\n\t\t\t+\n\t\t\t\t15.0\n\t\t\t\tx\n";
		assertEquals(
				parseTreeStr,
				_parser.parse(expressionStr).convertToString(0));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testExpressionDoubleParentheses() throws ExpressionParseException {
		final String expressionStr = "((x))";
		final String parseTreeStr = "()\n" + "\t()\n" + "\t\tx\n";
		_parser.parse(expressionStr);
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}
	
	
	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testExpressionExponentiation() throws ExpressionParseException {
		final String expressionStr = "2^x^3";
		final String parseTreeStr = "^\n" + "\t2.0\n" + "\t^\n" + "\t\tx\n" + "\t\t3.0\n";
		assertEquals(parseTreeStr, _parser.parse(expressionStr).convertToString(0));
	}
	
	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExceptionAdditive() {
		try {
			final String expressionStr = "1+2+";
			_parser.parse(expressionStr);
		} catch (ExpressionParseException epe) {
		}
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExceptionNestedParentheses() {
		try {
			final String expressionStr = "((()))";
			_parser.parse(expressionStr);
		} catch (ExpressionParseException epe) {
		}
	}

	@Test
	/**
	 * Verifies that a specific expression is parsed into the correct parse tree.
	 */
	public void testExceptionSequentialParentheses() {
		try {
			final String expressionStr = "()()";
			_parser.parse(expressionStr);
		} catch (ExpressionParseException epe) {
		}
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateAdditive() throws ExpressionParseException {
		final String expressionStr = "x+x";
		assertEquals(156, (int) _parser.parse(expressionStr).evaluate(78));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateAdditiveTwoSymbolsParentheses() throws ExpressionParseException {
		final String expressionStr = "(x+x)";
		assertEquals(156, (int) _parser.parse(expressionStr).evaluate(78));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateAdditiveParentheses() throws ExpressionParseException {
		final String expressionStr = "4+(x+5+x)";
		assertEquals(15, (int) _parser.parse(expressionStr).evaluate(3));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateAdditiveMultiplicative() throws ExpressionParseException {
		final String expressionStr = "4*(x+5*x)";
		assertEquals(72, (int) _parser.parse(expressionStr).evaluate(3));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateSingleLiteral() throws ExpressionParseException {
		final String expressionStr = "x";
		assertEquals(2, (int) _parser.parse(expressionStr).evaluate(2));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateMultiplicationDivision() throws ExpressionParseException {
		final String expressionStr = "9/3*3";
System.out.println(_parser.parse(expressionStr).convertToString(0));
		assertEquals(9, (int) _parser.parse(expressionStr).evaluate(333));
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateSubtractionMultiplication() throws ExpressionParseException {
		final String expressionStr = "4-3*x";
		assertEquals(-3.5, _parser.parse(expressionStr).evaluate(2.5), 0.01);
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateRationalExpression() throws ExpressionParseException {
		final String expressionStr = "1./(1. + 5^(-1*x))";
		assertEquals(0.8333333333333334, _parser.parse(expressionStr).evaluate(1), 0.05);
	}

	@Test
	/**
	 * Verifies that a specific expression is evaluated correctly.
	 */
	public void testEvaluateExponetiation() throws ExpressionParseException {
		final String expressionStr = "4^3^2";
		assertEquals(262144, (int) _parser.parse(expressionStr).evaluate(0));
	}
	
	@Test
	/**
	 * Tests differentiation for linear expression
	 */
	public void testDifferentiateLinear() throws ExpressionParseException {
		final String expressionStr = "2*x";
		final String expressionTree = "+" + "\n\t*" + "\n\t\t2.0" + "\n\t\t1.0" + "\n\t*" + "\n\t\t0.0" + "\n\t\tx\n";
		assertEquals(expressionTree, _parser.parse(expressionStr).differentiate().convertToString(0));
	}
	
	@Test
	/**
	 * Tests differentiation for exponential function
	 */
	public void testDifferentiateExponential() throws ExpressionParseException {
		final String expressionStr = "2^x";
		final String expressionTree = "*" + "\n\t()" + "\n\t\tlog" + "\n\t\t\t2.0" + "\n\t^" + "\n\t\t2.0" + "\n\t\tx\n";
		assertEquals(expressionTree, _parser.parse(expressionStr).differentiate().convertToString(0));
	}
	
	
}
