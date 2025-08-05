/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

/**
 * Tests the evaluation of core XPath operations like arithmetic, comparisons, and logical expressions.
 */
@DisplayName("Core XPath Operations")
class CoreOperationTest extends AbstractJXPathTest {

    private JXPathContext context;

    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            context = JXPathContext.newContext(null);
            final Variables vars = context.getVariables();
            // Set up variables used across multiple tests
            vars.declareVariable("integer", 1);
            vars.declareVariable("array", new double[]{0.25, 0.5, 0.75});
            vars.declareVariable("nan", Double.NaN);
        }
    }

    private static Stream<String> emptyNodeSetComparisonExpressions() {
        return Stream.of(
                "/idonotexist = 0", "/idonotexist != 0", "/idonotexist < 0",
                "/idonotexist > 0", "/idonotexist >= 0", "/idonotexist <= 0",
                "$array[position() < 1] = 0", "$array[position() < 1] != 0",
                "$array[position() < 1] < 0", "$array[position() < 1] > 0",
                "$array[position() < 1] >= 0", "$array[position() < 1] <= 0");
    }

    @DisplayName("A comparison with an empty node-set should always be false")
    @ParameterizedTest(name = "evaluating ''{0}'' should be false")
    @MethodSource("emptyNodeSetComparisonExpressions")
    void testEmptyNodeSetComparisonIsAlwaysFalse(final String xpath) {
        // According to XPath 1.0 spec (section 3.4), if one operand is a node-set
        // and the other is a number, the comparison is true only if there is ANY node
        // in the node-set for which the comparison is true.
        // If the node-set is empty, the comparison is therefore always false.
        assertXPathValue(context, xpath, Boolean.FALSE, Boolean.class);
    }

    @DisplayName("A comparison involving NaN should always be false")
    @ParameterizedTest(name = "evaluating ''{0}'' should be false")
    @ValueSource(strings = {
            "$nan > $nan", "$nan < $nan", "$nan >= $nan", "$nan <= $nan",
            "$nan = $nan", "$nan != $nan", "$nan > 0", "$nan < 0",
            "$nan >= 0", "$nan <= 0", "$nan = 0", "$nan != 0",
            "$nan > 1", "$nan < 1", "$nan >= 1", "$nan <= 1",
            "$nan = 1", "$nan != 1"
    })
    void testNaNComparisonIsAlwaysFalse(final String xpath) {
        // According to the XPath 1.0 spec (and IEEE 754), any ordered or
        // unordered comparison involving NaN returns false.
        assertXPathValue(context, xpath, Boolean.FALSE, Boolean.class);
    }

    /**
     * Tests comparisons where one operand is a node-set (an array in this case).
     * According to XPath 1.0, such a comparison is existential: it returns true if the
     * comparison is true for *at least one* of the nodes in the set.
     * The test uses the variable $array = {0.25, 0.5, 0.75}.
     */
    @Test
    @DisplayName("Node-set comparisons should be existential")
    void testNodeSetComparisonIsExistential() {
        // TRUE because at least one element is > 0
        assertXPathValue(context, "$array > 0", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array >= 0", Boolean.TRUE, Boolean.class);

        // TRUE because at least one element matches the value
        assertXPathValue(context, "$array = 0.25", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.5", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.50000", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.75", Boolean.TRUE, Boolean.class);

        // TRUE because all elements are < 1 (and thus at least one is)
        assertXPathValue(context, "$array < 1", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array <= 1", Boolean.TRUE, Boolean.class);

        // FALSE because no element matches the value
        assertXPathValue(context, "$array = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array = 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array > 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array < 0", Boolean.FALSE, Boolean.class);
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticOperationTests {
        @Test
        @DisplayName("should handle addition and subtraction")
        void testAdditionAndSubtraction() {
            assertXPathValue(context, "2 + 2", 4.0);
            assertXPathValue(context, "3 - 2", 1.0);
            assertXPathValue(context, "1 + 2 + 3 - 4 + 5", 7.0);
        }

        @Test
        @DisplayName("should handle multiplication, division, and modulo")
        void testMultiplicationDivisionModulo() {
            assertXPathValue(context, "3 * 2", 6.0);
            assertXPathValue(context, "3 div 2", 1.5);
            assertXPathValue(context, "5 mod 2", 1.0);

            // The result of 'mod' takes the sign of the dividend.
            assertXPathValue(context, "5 mod -2", 1.0);
            assertXPathValue(context, "-5 mod 2", -1.0);
            assertXPathValue(context, "-5 mod -2", -1.0);
        }

        @Test
        @DisplayName("should handle modulo with floating-point numbers")
        void testFloatingPointModulo() {
            // This test asserts a value of 1.0, which may be specific to JXPath's
            // implementation and could differ from other engines like Xalan.
            // The XPath 1.0 spec defines 'a mod b' as 'a - floor(a div b) * b'.
            // For '5.9 mod 2.1', this would be: 5.9 - floor(2.809...) * 2.1 = 1.7.
            // The test's expectation of 1.0 is preserved to maintain original test behavior.
            assertXPathValue(context, "5.9 mod 2.1", 1.0);
        }
    }

    @Nested
    @DisplayName("Boolean Logic")
    class BooleanLogicTests {
        @Test
        @DisplayName("should handle relational comparisons")
        void testRelationalComparisons() {
            assertXPathValue(context, "1 < 2", Boolean.TRUE);
            assertXPathValue(context, "1 > 2", Boolean.FALSE);
            assertXPathValue(context, "1 <= 1", Boolean.TRUE);
            assertXPathValue(context, "1 >= 2", Boolean.FALSE);
        }

        @Test
        @DisplayName("should handle logical AND and OR")
        void testLogicalAndOr() {
            assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
            assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
            assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
            assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);
        }

        @Test
        @DisplayName("should evaluate chained comparisons from left to right")
        void testChainedComparisons() {
            // XPath evaluates expressions from left to right.
            // "3 > 2 > 1" is evaluated as "(3 > 2) > 1".
            // (true) > 1  -->  number(true) > 1  -->  1 > 1  -->  false.
            assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);
        }

        @Test
        @DisplayName("should handle equality between booleans and numbers")
        void testBooleanEquality() {
            // "(1 > 2)" results in false. "(2 > 3)" also results in false.
            // The expression becomes "false = false", which is true.
            assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE);

            // "(1 > 2)" results in false. The expression becomes "false = 0".
            // When comparing a boolean to a number, the boolean is converted:
            // number(false) is 0. So "0 = 0" is true.
            assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE);
        }

        @Test
        @DisplayName("should handle equality between numbers and strings")
        void testCrossTypeEquality() {
            assertXPathValue(context, "1 = 1", Boolean.TRUE);
            // The string '1' is converted to the number 1 for comparison.
            assertXPathValue(context, "1 = '1'", Boolean.TRUE);
            assertXPathValue(context, "1 = 2", Boolean.FALSE);
        }
    }

    @Nested
    @DisplayName("Evaluation and Type Conversion")
    class EvaluationTests {
        @Test
        @DisplayName("should evaluate literals and return correct value types")
        void testLiteralEvaluation() {
            assertXPathValue(context, "1", 1.0);
            assertXPathPointer(context, "1", "1");
            assertXPathValueIterator(context, "1", list(1.0));
            assertXPathPointerIterator(context, "1", list("1"));
        }

        @Test
        @DisplayName("should evaluate variables")
        void testVariableEvaluation() {
            assertXPathValue(context, "$integer", 1.0, Double.class);
        }

        @Test
        @DisplayName("should convert expression results to requested types")
        void testResultTypeConversion() {
            // number -> String
            assertXPathValue(context, "2 + 3", "5.0", String.class);
            // number -> boolean (non-zero is true)
            assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);
            // string -> boolean ('true' is true)
            assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
        }
    }
}