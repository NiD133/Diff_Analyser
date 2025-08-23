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
import org.junit.jupiter.api.Test;

/**
 * CoreOperation behavior: literals, arithmetic, comparisons, node-set ops, and NaN handling.
 */
@DisplayName("CoreOperation: arithmetic, comparison, node-set, and NaN semantics")
class CoreOperationTest extends AbstractJXPathTest {

    private JXPathContext context;

    @BeforeEach
    void setUp() {
        // Fresh context per test for isolation and clarity
        context = JXPathContext.newContext(null);
        final Variables vars = context.getVariables();
        vars.declareVariable("integer", Integer.valueOf(1));
        vars.declareVariable("array", new double[] { 0.25, 0.5, 0.75 });
        vars.declareVariable("nan", Double.valueOf(Double.NaN));
    }

    // ----------------------------
    // Empty node-set comparisons
    // ----------------------------

    @Test
    @DisplayName("Empty node-set compared to numbers yields false for all comparison operators")
    void emptyNodeSetComparedToNumbersAlwaysFalse() {
        // Non-existent path -> empty node-set
        assertAllFalse(
            "/idonotexist = 0",
            "/idonotexist != 0",
            "/idonotexist < 0",
            "/idonotexist > 0",
            "/idonotexist >= 0",
            "/idonotexist <= 0"
        );

        // Filtered array produces empty node-set
        assertAllFalse(
            "$array[position() < 1] = 0",
            "$array[position() < 1] != 0",
            "$array[position() < 1] < 0",
            "$array[position() < 1] > 0",
            "$array[position() < 1] >= 0",
            "$array[position() < 1] <= 0"
        );
    }

    // ----------------------------
    // Numeric literals and arithmetic
    // ----------------------------

    @Test
    @DisplayName("Numeric literals, arithmetic, and pointers")
    void numericLiteralsArithmeticAndPointers() {
        // Literal value and its pointer/iteration views
        assertXPathValue(context, "1", Double.valueOf(1.0));
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(Double.valueOf(1.0)));
        assertXPathPointerIterator(context, "1", list("1"));

        // Unary minus and arithmetic
        assertXPathValue(context, "-1", Double.valueOf(-1.0));
        assertXPathValue(context, "2 + 2", Double.valueOf(4.0));
        assertXPathValue(context, "3 - 2", Double.valueOf(1.0));
        assertXPathValue(context, "1 + 2 + 3 - 4 + 5", Double.valueOf(7.0));
        assertXPathValue(context, "3 * 2", Double.valueOf(6.0));
        assertXPathValue(context, "3 div 2", Double.valueOf(1.5));

        // mod behavior with positive and negative operands
        assertXPathValue(context, "5 mod 2", Double.valueOf(1.0));
        // This test produces a different result with Xalan.
        assertXPathValue(context, "5.9 mod 2.1", Double.valueOf(1.0));
        assertXPathValue(context, "5 mod -2", Double.valueOf(1.0));
        assertXPathValue(context, "-5 mod 2", Double.valueOf(-1.0));
        assertXPathValue(context, "-5 mod -2", Double.valueOf(-1.0));
    }

    // ----------------------------
    // Boolean logic and comparisons
    // ----------------------------

    @Test
    @DisplayName("Boolean comparisons and logical operations")
    void booleanComparisonsAndLogic() {
        // Relational comparisons
        assertXPathValue(context, "1 < 2", Boolean.TRUE);
        assertXPathValue(context, "1 > 2", Boolean.FALSE);
        assertXPathValue(context, "1 <= 1", Boolean.TRUE);
        assertXPathValue(context, "1 >= 2", Boolean.FALSE);

        // Chained comparison is evaluated left-to-right (not mathematical chaining)
        assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);

        // Logical operators
        assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
        assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);

        // Equality with coercion
        assertXPathValue(context, "1 = 1", Boolean.TRUE);
        assertXPathValue(context, "1 = '1'", Boolean.TRUE);

        // Equality of booleans (results of comparisons)
        assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE);
        assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE);

        assertXPathValue(context, "1 = 2", Boolean.FALSE);
    }

    // ----------------------------
    // Type coercion
    // ----------------------------

    @Test
    @DisplayName("Variable and result type coercion")
    void variableAndResultTypeCoercion() {
        // Variable is coerced to Double when requested
        assertXPathValue(context, "$integer", Double.valueOf(1), Double.class);

        // Expression results can be coerced to String and boolean
        assertXPathValue(context, "2 + 3", "5.0", String.class);
        assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);

        // String 'true' coerces to Boolean.TRUE
        assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
    }

    // ----------------------------
    // NaN handling
    // ----------------------------

    @Test
    @DisplayName("NaN compared with anything yields false for all comparison and equality ops")
    void nanComparisonsAlwaysFalse() {
        // With NaN on both sides
        assertAllFalse(
            "$nan > $nan", "$nan < $nan", "$nan >= $nan", "$nan <= $nan",
            "$nan >= $nan and $nan <= $nan",
            "$nan = $nan",
            "$nan != $nan"
        );

        // With NaN and 0
        assertAllFalse(
            "$nan > 0", "$nan < 0", "$nan >= 0", "$nan <= 0",
            "$nan >= 0 and $nan <= 0",
            "$nan = 0", "$nan != 0"
        );

        // With NaN and 1
        assertAllFalse(
            "$nan > 1", "$nan < 1", "$nan >= 1", "$nan <= 1",
            "$nan >= 1 and $nan <= 1",
            "$nan = 1", "$nan != 1"
        );
    }

    // ----------------------------
    // Node-set vs numbers
    // ----------------------------

    @Test
    @DisplayName("Node-set comparisons against numbers")
    void nodeSetOperationsAgainstNumbers() {
        // Any of the array values [0.25, 0.5, 0.75] vs scalar
        assertAllTrue(
            "$array > 0",
            "$array >= 0",
            "$array = 0.25",
            "$array = 0.5",
            "$array = 0.50000",
            "$array = 0.75",
            "$array < 1",
            "$array <= 1"
        );

        assertAllFalse(
            "$array = 1",
            "$array > 1",
            "$array < 0"
        );
    }

    // ----------------------------
    // Small assertion helpers
    // ----------------------------

    private void assertAllFalse(final String... expressions) {
        for (final String expr : expressions) {
            assertXPathValue(context, expr, Boolean.FALSE, Boolean.class);
        }
    }

    private void assertAllTrue(final String... expressions) {
        for (final String expr : expressions) {
            assertXPathValue(context, expr, Boolean.TRUE, Boolean.class);
        }
    }
}