package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Core XPath Operations")
class CoreOperationEvaluationTest extends AbstractJXPathTest {

    private JXPathContext context;

    @BeforeEach
    @Override
    public void setUp() {
        context = JXPathContext.newContext(null);
        context.getVariables().declareVariable("integer", 1);
    }

    @Test
    @DisplayName("should evaluate numeric literals")
    void testNumericLiteralEvaluation() {
        assertXPathValue(context, "1", 1.0);
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(1.0));
        assertXPathPointerIterator(context, "1", list("1"));
        assertXPathValue(context, "-1", -1.0);
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticOperations {

        @Test
        @DisplayName("should evaluate addition")
        void testAddition() {
            assertXPathValue(context, "2 + 2", 4.0);
        }

        @Test
        @DisplayName("should evaluate subtraction")
        void testSubtraction() {
            assertXPathValue(context, "3 - 2", 1.0);
        }

        @Test
        @DisplayName("should evaluate multiplication")
        void testMultiplication() {
            assertXPathValue(context, "3 * 2", 6.0);
        }

        @Test
        @DisplayName("should evaluate division")
        void testDivision() {
            assertXPathValue(context, "3 div 2", 1.5);
        }

        @Test
        @DisplayName("should evaluate chained operations with correct precedence")
        void testChainedArithmetic() {
            assertXPathValue(context, "1 + 2 + 3 - 4 + 5", 7.0);
        }
    }

    @Nested
    @DisplayName("Modulo Operator")
    class ModuloOperator {

        @Test
        @DisplayName("should handle positive integers")
        void testModWithPositiveIntegers() {
            assertXPathValue(context, "5 mod 2", 1.0);
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testModWithNegativeNumbers() {
            assertXPathValue(context, "5 mod -2", 1.0);
            assertXPathValue(context, "-5 mod 2", -1.0);
            assertXPathValue(context, "-5 mod -2", -1.0);
        }

        @Test
        @DisplayName("should handle floating-point numbers")
        void testModWithFloatingPointNumbers() {
            // Note: The XPath 1.0 spec for 'mod' with floating-point numbers can
            // lead to results that differ from Java's '%' operator.
            // This behavior may also vary from other XPath engines like Xalan.
            assertXPathValue(context, "5.9 mod 2.1", 1.7); // 5.9 - 2 * 2.1 = 5.9 - 4.2 = 1.7
        }
    }

    @Nested
    @DisplayName("Comparison Operations")
    class ComparisonOperations {

        @Test
        @DisplayName("should evaluate relational operators (<, >, <=, >=)")
        void testRelationalOperators() {
            assertXPathValue(context, "1 < 2", Boolean.TRUE);
            assertXPathValue(context, "1 > 2", Boolean.FALSE);
            assertXPathValue(context, "1 <= 1", Boolean.TRUE);
            assertXPathValue(context, "1 >= 2", Boolean.FALSE);
        }

        @Test
        @DisplayName("should evaluate equality between numbers and strings")
        void testEquality() {
            assertXPathValue(context, "1 = 1", Boolean.TRUE);
            assertXPathValue(context, "1 = '1'", Boolean.TRUE);
            assertXPathValue(context, "1 = 2", Boolean.FALSE);
        }

        @Test
        @DisplayName("should evaluate equality of boolean expressions")
        void testEqualityOfBooleanExpressions() {
            // (1 > 2) is false. number(false) is 0. So, (false = 0) is true.
            assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE);
            // (1 > 2) is false. (2 > 3) is false. So, (false = false) is true.
            assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE);
        }

        @Test
        @DisplayName("should not chain relational operators directly")
        void testInvalidChainedComparison() {
            // XPath does not support chained comparisons like Python.
            // '3 > 2' evaluates to true. 'true > 1' is then evaluated.
            // number(true) is 1. So '1 > 1' is false.
            assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);
        }
    }

    @Nested
    @DisplayName("Logical Operations")
    class LogicalOperations {

        @Test
        @DisplayName("should evaluate 'and' operator")
        void testLogicalAnd() {
            assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
            assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
        }

        @Test
        @DisplayName("should evaluate 'or' operator")
        void testLogicalOr() {
            assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
            assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);
        }
    }

    @Nested
    @DisplayName("Type Conversions")
    class TypeConversions {

        @Test
        @DisplayName("should convert numeric result to string")
        void testNumericResultToString() {
            assertXPathValue(context, "2 + 3", "5.0", String.class);
        }

        @Test
        @DisplayName("should convert numeric result to boolean")
        void testNumericResultToBoolean() {
            // Any non-zero number converts to true
            assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);
        }

        @Test
        @DisplayName("should convert string literal to boolean")
        void testStringLiteralToBoolean() {
            // A non-empty string converts to true
            assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
        }
    }

    @Test
    @DisplayName("should resolve a declared variable")
    void testVariableReference() {
        assertXPathValue(context, "$integer", 1.0, Double.class);
    }
}