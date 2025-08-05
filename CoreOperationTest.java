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
import org.junit.jupiter.api.Test;

/**
 * Test core operations and functionality of JXPath - infoset types, operations.
 */
class CoreOperationTest extends AbstractJXPathTest {

    private JXPathContext context;

    @Override
    @BeforeEach
    public void setUp() {
        context = JXPathContext.newContext(null);
        final Variables vars = context.getVariables();
        vars.declareVariable("integer", 1); // Autoboxed to Integer
        vars.declareVariable("array", new double[]{0.25, 0.5, 0.75});
        vars.declareVariable("nan", Double.NaN); // Autoboxed to Double
    }

    @Test
    void testEmptyNodeSetOperations() {
        // Verify operations on non-existent paths
        assertXPathValue(context, "/idonotexist = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "/idonotexist != 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "/idonotexist < 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "/idonotexist > 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "/idonotexist >= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "/idonotexist <= 0", Boolean.FALSE, Boolean.class);

        // Verify operations on empty array positions
        assertXPathValue(context, "$array[position() < 1] = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array[position() < 1] != 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array[position() < 1] < 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array[position() < 1] > 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array[position() < 1] >= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array[position() < 1] <= 0", Boolean.FALSE, Boolean.class);
    }

    @Test
    void testNumberLiterals() {
        // Verify numeric literal parsing and representation
        assertXPathValue(context, "1", Double.valueOf(1.0));
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(Double.valueOf(1.0)));
        assertXPathPointerIterator(context, "1", list("1"));
        assertXPathValue(context, "-1", Double.valueOf(-1.0));
    }

    @Test
    void testArithmeticOperations() {
        // Test basic arithmetic operations
        assertXPathValue(context, "2 + 2", Double.valueOf(4.0));
        assertXPathValue(context, "3 - 2", Double.valueOf(1.0));
        assertXPathValue(context, "1 + 2 + 3 - 4 + 5", Double.valueOf(7.0));
        assertXPathValue(context, "3 * 2", Double.valueOf(3.0 * 2.0));
        assertXPathValue(context, "3 div 2", Double.valueOf(3.0 / 2.0));
        
        // Test modulus operations with various inputs
        assertXPathValue(context, "5 mod 2", Double.valueOf(1.0));
        assertXPathValue(context, "5.9 mod 2.1", Double.valueOf(1.0)); // Note: Different from Xalan
        assertXPathValue(context, "5 mod -2", Double.valueOf(1.0));
        assertXPathValue(context, "-5 mod 2", Double.valueOf(-1.0));
        assertXPathValue(context, "-5 mod -2", Double.valueOf(-1.0));
    }

    @Test
    void testComparisonOperations() {
        // Test basic comparison operators
        assertXPathValue(context, "1 < 2", Boolean.TRUE);
        assertXPathValue(context, "1 > 2", Boolean.FALSE);
        assertXPathValue(context, "1 <= 1", Boolean.TRUE);
        assertXPathValue(context, "1 >= 2", Boolean.FALSE);
        
        // Test chained comparisons (evaluated as (3>2)>1 -> true>1 -> false)
        assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);
    }

    @Test
    void testLogicalOperations() {
        // Test AND/OR logical operations
        assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
        assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);
    }

    @Test
    void testEqualityOperations() {
        // Test equality with same/different types
        assertXPathValue(context, "1 = 1", Boolean.TRUE);
        assertXPathValue(context, "1 = '1'", Boolean.TRUE); // String-to-number conversion
        assertXPathValue(context, "1 = 2", Boolean.FALSE);
        
        // Test equality with boolean results
        assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE);  // false = false
        assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE);      // false = false (0 is false)
    }

    @Test
    void testVariableResolution() {
        // Test variable access and type conversion
        assertXPathValue(context, "$integer", Double.valueOf(1), Double.class);
    }

    @Test
    void testTypeConversions() {
        // Test number-to-string conversion
        assertXPathValue(context, "2 + 3", "5.0", String.class);
        
        // Test number-to-boolean conversion (non-zero is true)
        assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);
        
        // Test string-to-boolean conversion
        assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
    }

    @Test
    void testNaNComparisons() {
        // All comparisons with NaN should return false
        assertXPathValue(context, "$nan > $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != $nan", Boolean.FALSE, Boolean.class);
        
        // Combined comparison
        assertXPathValue(context, "$nan >= $nan and $nan <= $nan", Boolean.FALSE, Boolean.class);
        
        // Test comparisons with numbers
        assertXPathValue(context, "$nan > 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 0 and $nan <= 0", Boolean.FALSE, Boolean.class);
        
        // Test comparisons with non-zero numbers
        assertXPathValue(context, "$nan > 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 1 and $nan <= 1", Boolean.FALSE, Boolean.class);
    }

    @Test
    void testNodeSetOperations() {
        // Test comparisons with node sets (arrays)
        assertXPathValue(context, "$array > 0", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array >= 0", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array < 1", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array <= 1", Boolean.TRUE, Boolean.class);
        
        // Test equality checks with array values
        assertXPathValue(context, "$array = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array = 0.25", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.5", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.50000", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.75", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 1", Boolean.FALSE, Boolean.class);
        
        // Test boundary cases
        assertXPathValue(context, "$array > 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array < 0", Boolean.FALSE, Boolean.class);
    }
}