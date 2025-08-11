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
 * Tests for core XPath operations including arithmetic, comparison, logical operations,
 * and their behavior with different data types (numbers, booleans, node sets, NaN values).
 */
class CoreOperationTest extends AbstractJXPathTest {

    private JXPathContext context;
    
    // Test data constants for better readability
    private static final Integer TEST_INTEGER = 1;
    private static final double[] TEST_ARRAY = {0.25, 0.5, 0.75};
    private static final Double NAN_VALUE = Double.NaN;

    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            context = JXPathContext.newContext(null);
            setupTestVariables();
        }
    }
    
    private void setupTestVariables() {
        final Variables vars = context.getVariables();
        vars.declareVariable("integer", TEST_INTEGER);
        vars.declareVariable("array", TEST_ARRAY);
        vars.declareVariable("nan", NAN_VALUE);
    }

    /**
     * Tests that all comparison operations with empty node sets return false.
     * In XPath, empty node sets evaluate to false in all comparison operations.
     */
    @Test
    void testEmptyNodeSetOperations() {
        // Test with non-existent path - should create empty node set
        testAllComparisonOperationsReturnFalse("/idonotexist", "0");
        
        // Test with array filter that returns empty set
        testAllComparisonOperationsReturnFalse("$array[position() < 1]", "0");
    }
    
    private void testAllComparisonOperationsReturnFalse(String leftOperand, String rightOperand) {
        assertXPathValue(context, leftOperand + " = " + rightOperand, Boolean.FALSE, Boolean.class);
        assertXPathValue(context, leftOperand + " != " + rightOperand, Boolean.FALSE, Boolean.class);
        assertXPathValue(context, leftOperand + " < " + rightOperand, Boolean.FALSE, Boolean.class);
        assertXPathValue(context, leftOperand + " > " + rightOperand, Boolean.FALSE, Boolean.class);
        assertXPathValue(context, leftOperand + " >= " + rightOperand, Boolean.FALSE, Boolean.class);
        assertXPathValue(context, leftOperand + " <= " + rightOperand, Boolean.FALSE, Boolean.class);
    }

    /**
     * Tests various XPath data types and operations including:
     * - Number literals and arithmetic operations
     * - Comparison operations
     * - Logical operations (and, or)
     * - Type conversions
     */
    @Test
    void testInfoSetTypes() {
        testNumberLiteralsAndBasicOperations();
        testArithmeticOperations();
        testComparisonOperations();
        testLogicalOperations();
        testEqualityOperations();
        testTypeConversions();
    }
    
    private void testNumberLiteralsAndBasicOperations() {
        // Basic number literal
        assertXPathValue(context, "1", Double.valueOf(1.0));
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(Double.valueOf(1.0)));
        assertXPathPointerIterator(context, "1", list("1"));
        
        // Negative numbers
        assertXPathValue(context, "-1", Double.valueOf(-1.0));
    }
    
    private void testArithmeticOperations() {
        // Addition and subtraction
        assertXPathValue(context, "2 + 2", Double.valueOf(4.0));
        assertXPathValue(context, "3 - 2", Double.valueOf(1.0));
        assertXPathValue(context, "1 + 2 + 3 - 4 + 5", Double.valueOf(7.0));
        
        // Multiplication and division
        assertXPathValue(context, "3 * 2", Double.valueOf(6.0));
        assertXPathValue(context, "3 div 2", Double.valueOf(1.5));
        
        // Modulo operations with various sign combinations
        assertXPathValue(context, "5 mod 2", Double.valueOf(1.0));
        assertXPathValue(context, "5.9 mod 2.1", Double.valueOf(1.0)); // Note: May differ with other XPath processors
        assertXPathValue(context, "5 mod -2", Double.valueOf(1.0));
        assertXPathValue(context, "-5 mod 2", Double.valueOf(-1.0));
        assertXPathValue(context, "-5 mod -2", Double.valueOf(-1.0));
    }
    
    private void testComparisonOperations() {
        // Basic comparisons
        assertXPathValue(context, "1 < 2", Boolean.TRUE);
        assertXPathValue(context, "1 > 2", Boolean.FALSE);
        assertXPathValue(context, "1 <= 1", Boolean.TRUE);
        assertXPathValue(context, "1 >= 2", Boolean.FALSE);
        
        // Chained comparisons (note: 3 > 2 > 1 is parsed as (3 > 2) > 1, which is true > 1, which is false)
        assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);
    }
    
    private void testLogicalOperations() {
        // AND operations
        assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
        
        // OR operations
        assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);
    }
    
    private void testEqualityOperations() {
        // Basic equality
        assertXPathValue(context, "1 = 1", Boolean.TRUE);
        assertXPathValue(context, "1 = '1'", Boolean.TRUE); // Type coercion
        assertXPathValue(context, "1 = 2", Boolean.FALSE);
        
        // Complex equality expressions
        assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE); // false = false
        assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE); // false = false (0 is false)
    }
    
    private void testTypeConversions() {
        // Variable access and type conversion
        assertXPathValue(context, "$integer", Double.valueOf(1), Double.class);
        assertXPathValue(context, "2 + 3", "5.0", String.class);
        assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);
        assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
    }

    /**
     * Tests NaN (Not a Number) behavior in XPath operations.
     * According to XPath specification, NaN comparisons always return false,
     * including NaN compared to itself.
     */
    @Test
    void testNan() {
        testNanSelfComparisons();
        testNanComparisonWithZero();
        testNanComparisonWithOne();
    }
    
    private void testNanSelfComparisons() {
        // NaN compared to itself - all should be false
        assertXPathValue(context, "$nan > $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= $nan and $nan <= $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = $nan", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != $nan", Boolean.FALSE, Boolean.class);
    }
    
    private void testNanComparisonWithZero() {
        // NaN compared to 0 - all should be false
        assertXPathValue(context, "$nan > 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 0 and $nan <= 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != 0", Boolean.FALSE, Boolean.class);
    }
    
    private void testNanComparisonWithOne() {
        // NaN compared to 1 - all should be false
        assertXPathValue(context, "$nan > 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan < 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan <= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan >= 1 and $nan <= 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan = 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$nan != 1", Boolean.FALSE, Boolean.class);
    }

    /**
     * Tests comparison operations with node sets (arrays).
     * Node set comparisons return true if ANY element in the set satisfies the condition.
     * Test array contains: [0.25, 0.5, 0.75]
     */
    @Test
    void testNodeSetOperations() {
        testNodeSetComparisonWithZero();
        testNodeSetComparisonWithSpecificValues();
        testNodeSetComparisonWithOne();
    }
    
    private void testNodeSetComparisonWithZero() {
        // All array elements are > 0, so these should be true
        assertXPathValue(context, "$array > 0", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array >= 0", Boolean.TRUE, Boolean.class);
        
        // No array element equals 0
        assertXPathValue(context, "$array = 0", Boolean.FALSE, Boolean.class);
    }
    
    private void testNodeSetComparisonWithSpecificValues() {
        // Test equality with actual array values
        assertXPathValue(context, "$array = 0.25", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.5", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.50000", Boolean.TRUE, Boolean.class); // Floating point precision
        assertXPathValue(context, "$array = 0.75", Boolean.TRUE, Boolean.class);
    }
    
    private void testNodeSetComparisonWithOne() {
        // All array elements are < 1, so these should be true
        assertXPathValue(context, "$array < 1", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array <= 1", Boolean.TRUE, Boolean.class);
        
        // No array element equals or is greater than 1
        assertXPathValue(context, "$array = 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array > 1", Boolean.FALSE, Boolean.class);
        
        // No array element is less than 0
        assertXPathValue(context, "$array < 0", Boolean.FALSE, Boolean.class);
    }
}