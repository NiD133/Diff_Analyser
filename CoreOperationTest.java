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
 * Test basic functionality of JXPath - infoset types, operations.
 */
class CoreOperationTest extends AbstractJXPathTest {

    private JXPathContext context;

    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            context = JXPathContext.newContext(null);
            initializeVariables(context.getVariables());
        }
    }

    private void initializeVariables(Variables vars) {
        vars.declareVariable("integer", Integer.valueOf(1));
        vars.declareVariable("array", new double[] { 0.25, 0.5, 0.75 });
        vars.declareVariable("nan", Double.valueOf(Double.NaN));
    }

    @Test
    void testEmptyNodeSetOperations() {
        assertXPathValueIsFalse("/idonotexist = 0");
        assertXPathValueIsFalse("/idonotexist != 0");
        assertXPathValueIsFalse("/idonotexist < 0");
        assertXPathValueIsFalse("/idonotexist > 0");
        assertXPathValueIsFalse("/idonotexist >= 0");
        assertXPathValueIsFalse("/idonotexist <= 0");
        assertXPathValueIsFalse("$array[position() < 1] = 0");
        assertXPathValueIsFalse("$array[position() < 1] != 0");
        assertXPathValueIsFalse("$array[position() < 1] < 0");
        assertXPathValueIsFalse("$array[position() < 1] > 0");
        assertXPathValueIsFalse("$array[position() < 1] >= 0");
        assertXPathValueIsFalse("$array[position() < 1] <= 0");
    }

    private void assertXPathValueIsFalse(String xpath) {
        assertXPathValue(context, xpath, Boolean.FALSE, Boolean.class);
    }

    @Test
    void testNumberOperations() {
        assertXPathValue(context, "1", Double.valueOf(1.0));
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(Double.valueOf(1.0)));
        assertXPathPointerIterator(context, "1", list("1"));
        assertXPathValue(context, "-1", Double.valueOf(-1.0));
        assertXPathValue(context, "2 + 2", Double.valueOf(4.0));
        assertXPathValue(context, "3 - 2", Double.valueOf(1.0));
        assertXPathValue(context, "1 + 2 + 3 - 4 + 5", Double.valueOf(7.0));
        assertXPathValue(context, "3 * 2", Double.valueOf(6.0));
        assertXPathValue(context, "3 div 2", Double.valueOf(1.5));
        assertXPathValue(context, "5 mod 2", Double.valueOf(1.0));
        assertXPathValue(context, "5.9 mod 2.1", Double.valueOf(1.0));
        assertXPathValue(context, "5 mod -2", Double.valueOf(1.0));
        assertXPathValue(context, "-5 mod 2", Double.valueOf(-1.0));
        assertXPathValue(context, "-5 mod -2", Double.valueOf(-1.0));
    }

    @Test
    void testComparisonOperations() {
        assertXPathValue(context, "1 < 2", Boolean.TRUE);
        assertXPathValue(context, "1 > 2", Boolean.FALSE);
        assertXPathValue(context, "1 <= 1", Boolean.TRUE);
        assertXPathValue(context, "1 >= 2", Boolean.FALSE);
        assertXPathValue(context, "3 > 2 > 1", Boolean.FALSE);
        assertXPathValue(context, "3 > 2 and 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 > 2 and 2 < 1", Boolean.FALSE);
        assertXPathValue(context, "3 < 2 or 2 > 1", Boolean.TRUE);
        assertXPathValue(context, "3 < 2 or 2 < 1", Boolean.FALSE);
        assertXPathValue(context, "1 = 1", Boolean.TRUE);
        assertXPathValue(context, "1 = '1'", Boolean.TRUE);
        assertXPathValue(context, "1 > 2 = 2 > 3", Boolean.TRUE);
        assertXPathValue(context, "1 > 2 = 0", Boolean.TRUE);
        assertXPathValue(context, "1 = 2", Boolean.FALSE);
    }

    @Test
    void testVariableAccess() {
        assertXPathValue(context, "$integer", Double.valueOf(1), Double.class);
        assertXPathValue(context, "2 + 3", "5.0", String.class);
        assertXPathValue(context, "2 + 3", Boolean.TRUE, boolean.class);
        assertXPathValue(context, "'true'", Boolean.TRUE, Boolean.class);
    }

    @Test
    void testNaNOperations() {
        assertNanComparison("$nan > $nan");
        assertNanComparison("$nan < $nan");
        assertNanComparison("$nan >= $nan");
        assertNanComparison("$nan <= $nan");
        assertNanComparison("$nan >= $nan and $nan <= $nan");
        assertNanComparison("$nan = $nan");
        assertNanComparison("$nan != $nan");
        assertNanComparison("$nan > 0");
        assertNanComparison("$nan < 0");
        assertNanComparison("$nan >= 0");
        assertNanComparison("$nan <= 0");
        assertNanComparison("$nan >= 0 and $nan <= 0");
        assertNanComparison("$nan = 0");
        assertNanComparison("$nan != 0");
        assertNanComparison("$nan > 1");
        assertNanComparison("$nan < 1");
        assertNanComparison("$nan >= 1");
        assertNanComparison("$nan <= 1");
        assertNanComparison("$nan >= 1 and $nan <= 1");
        assertNanComparison("$nan = 1");
        assertNanComparison("$nan != 1");
    }

    private void assertNanComparison(String xpath) {
        assertXPathValue(context, xpath, Boolean.FALSE, Boolean.class);
    }

    @Test
    void testNodeSetOperations() {
        assertXPathValue(context, "$array > 0", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array >= 0", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array = 0.25", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.5", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.50000", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 0.75", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array < 1", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array <= 1", Boolean.TRUE, Boolean.class);
        assertXPathValue(context, "$array = 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array > 1", Boolean.FALSE, Boolean.class);
        assertXPathValue(context, "$array < 0", Boolean.FALSE, Boolean.class);
    }
}