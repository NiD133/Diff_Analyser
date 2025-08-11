package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, intention-revealing tests for CoreOperation’s most visible behavior:
 * - how toString() renders an operation using its symbol and child expressions
 * - a simple example of computeValue() implemented by a concrete CoreOperation subclass
 *
 * Notes:
 * - We introduce small, local concrete subclasses of CoreOperation to make behavior explicit and
 *   avoid coupling to unrelated classes (e.g., JXPath contexts, axes, functions).
 * - We use Constant as simple child expressions to keep trees easy to read.
 * - Assertions are resilient: we verify essential aspects (presence of symbol/operands, parentheses existence)
 *   instead of brittle full-string matches that depend on internal formatting details.
 */
public class CoreOperationTest {

    /**
     * A minimal, infix-style CoreOperation that:
     * - uses the provided symbol (e.g., "+", "*")
     * - has a configurable precedence
     * - is symmetric (order of arguments does not affect semantics)
     * - computes a simple numeric result by summing child numeric values
     *
     * This class is used by the tests to exercise CoreOperation’s toString() and the delegation
     * to subclass-provided behavior (symbol/precedence/computeValue).
     */
    private static final class InfixSumOperation extends CoreOperation {
        private final String symbol;
        private final int precedence;

        InfixSumOperation(String symbol, int precedence, Expression... args) {
            super(args);
            this.symbol = symbol;
            this.precedence = precedence;
        }

        @Override
        protected int getPrecedence() {
            return precedence;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }

        @Override
        protected boolean isSymmetric() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            double sum = 0.0;
            if (args != null) {
                for (Expression e : args) {
                    if (e == null) {
                        continue;
                    }
                    Object v = e.computeValue(context);
                    if (v instanceof Number) {
                        sum += ((Number) v).doubleValue();
                    } else if (v != null) {
                        // Best-effort numeric coercion for the test’s sake
                        sum += Double.parseDouble(String.valueOf(v));
                    }
                }
            }
            // Return an Integer when the sum is a whole number to make assertions predictable
            return (sum % 1 == 0) ? (int) sum : sum;
        }
    }

    /**
     * Helper factory for readability.
     */
    private static InfixSumOperation add(Expression... args) {
        return new InfixSumOperation("+", CoreOperation.ADD_PRECEDENCE, args);
    }

    /**
     * Helper factory for readability.
     */
    private static InfixSumOperation mul(Expression... args) {
        return new InfixSumOperation("*", CoreOperation.MULTIPLY_PRECEDENCE, args);
    }

    @Test
    public void toString_includesSymbolAndChildExpressions_forTwoArguments() {
        // Arrange
        Expression left = new Constant(1);
        Expression right = new Constant(2);
        CoreOperation plus = add(left, right);

        // Act
        String rendered = plus.toString();

        // Assert
        // Avoid brittle full-string matches; validate essential pieces are present.
        assertNotNull(rendered);
        assertTrue("Expected '+' in: " + rendered, rendered.contains("+"));
        assertTrue("Expected '1' in: " + rendered, rendered.contains("1"));
        assertTrue("Expected '2' in: " + rendered, rendered.contains("2"));
    }

    @Test
    public void toString_doesNotThrow_forZeroArguments() {
        // Arrange
        CoreOperation plus = add(); // No children

        // Act
        String rendered = plus.toString();

        // Assert
        // Implementation detail of empty-arity rendering may vary; only assert it's safe and non-null.
        assertNotNull(rendered);
    }

    @Test
    public void computeValue_sumsNumericChildren() {
        // Arrange
        CoreOperation plus = add(new Constant(1), new Constant(2), new Constant(3));

        // Act
        Object value = plus.computeValue(null);

        // Assert
        assertEquals(6, value);
    }

    @Test
    public void toString_addsParentheses_whenChildHasLowerBindingStrength() {
        // Arrange
        // Build: (1 + 2) * 3
        // With typical precedence, '*' binds tighter than '+', so the '+' side is parenthesized.
        CoreOperation addition = add(new Constant(1), new Constant(2));
        CoreOperation product = mul(addition, new Constant(3));

        // Act
        String rendered = product.toString();

        // Assert
        // Don’t assert the full shape – just that parentheses are present somewhere,
        // indicating CoreOperation applied precedence rules when rendering.
        assertNotNull(rendered);
        assertTrue("Expected parentheses due to precedence in: " + rendered, rendered.contains("("));
    }
}