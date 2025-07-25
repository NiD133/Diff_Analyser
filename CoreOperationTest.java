package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for verifying the basic functionality of JXPath operations and infoset types.
 */
class CoreOperationTest extends AbstractJXPathTest {

    private JXPathContext context;

    /**
     * Sets up the JXPathContext and declares necessary variables for testing.
     */
    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            context = JXPathContext.newContext(null);
            Variables vars = context.getVariables();
            vars.declareVariable("integer", 1);
            vars.declareVariable("array", new double[] { 0.25, 0.5, 0.75 });
            vars.declareVariable("nan", Double.NaN);
        }
    }

    /**
     * Tests operations on empty node sets.
     */
    @Test
    void testEmptyNodeSetOperations() {
        String nonExistentPath = "/idonotexist";
        assertXPathValue(context, nonExistentPath + " = 0", false, Boolean.class);
        assertXPathValue(context, nonExistentPath + " != 0", false, Boolean.class);
        assertXPathValue(context, nonExistentPath + " < 0", false, Boolean.class);
        assertXPathValue(context, nonExistentPath + " > 0", false, Boolean.class);
        assertXPathValue(context, nonExistentPath + " >= 0", false, Boolean.class);
        assertXPathValue(context, nonExistentPath + " <= 0", false, Boolean.class);

        String arrayPositionPath = "$array[position() < 1]";
        assertXPathValue(context, arrayPositionPath + " = 0", false, Boolean.class);
        assertXPathValue(context, arrayPositionPath + " != 0", false, Boolean.class);
        assertXPathValue(context, arrayPositionPath + " < 0", false, Boolean.class);
        assertXPathValue(context, arrayPositionPath + " > 0", false, Boolean.class);
        assertXPathValue(context, arrayPositionPath + " >= 0", false, Boolean.class);
        assertXPathValue(context, arrayPositionPath + " <= 0", false, Boolean.class);
    }

    /**
     * Tests various infoset types and operations.
     */
    @Test
    void testInfoSetTypes() {
        // Test numeric operations
        assertXPathValue(context, "1", 1.0);
        assertXPathPointer(context, "1", "1");
        assertXPathValueIterator(context, "1", list(1.0));
        assertXPathPointerIterator(context, "1", list("1"));
        assertXPathValue(context, "-1", -1.0);
        assertXPathValue(context, "2 + 2", 4.0);
        assertXPathValue(context, "3 - 2", 1.0);
        assertXPathValue(context, "1 + 2 + 3 - 4 + 5", 7.0);
        assertXPathValue(context, "3 * 2", 6.0);
        assertXPathValue(context, "3 div 2", 1.5);
        assertXPathValue(context, "5 mod 2", 1.0);
        assertXPathValue(context, "5.9 mod 2.1", 1.0);
        assertXPathValue(context, "5 mod -2", 1.0);
        assertXPathValue(context, "-5 mod 2", -1.0);
        assertXPathValue(context, "-5 mod -2", -1.0);

        // Test comparison operations
        assertXPathValue(context, "1 < 2", true);
        assertXPathValue(context, "1 > 2", false);
        assertXPathValue(context, "1 <= 1", true);
        assertXPathValue(context, "1 >= 2", false);
        assertXPathValue(context, "3 > 2 > 1", false);
        assertXPathValue(context, "3 > 2 and 2 > 1", true);
        assertXPathValue(context, "3 > 2 and 2 < 1", false);
        assertXPathValue(context, "3 < 2 or 2 > 1", true);
        assertXPathValue(context, "3 < 2 or 2 < 1", false);

        // Test equality operations
        assertXPathValue(context, "1 = 1", true);
        assertXPathValue(context, "1 = '1'", true);
        assertXPathValue(context, "1 > 2 = 2 > 3", true);
        assertXPathValue(context, "1 > 2 = 0", true);
        assertXPathValue(context, "1 = 2", false);

        // Test variable access and type casting
        assertXPathValue(context, "$integer", 1.0, Double.class);
        assertXPathValue(context, "2 + 3", "5.0", String.class);
        assertXPathValue(context, "2 + 3", true, boolean.class);
        assertXPathValue(context, "'true'", true, Boolean.class);
    }

    /**
     * Tests operations involving NaN (Not a Number).
     */
    @Test
    void testNan() {
        String nanVar = "$nan";
        assertXPathValue(context, nanVar + " > " + nanVar, false, Boolean.class);
        assertXPathValue(context, nanVar + " < " + nanVar, false, Boolean.class);
        assertXPathValue(context, nanVar + " >= " + nanVar, false, Boolean.class);
        assertXPathValue(context, nanVar + " <= " + nanVar, false, Boolean.class);
        assertXPathValue(context, nanVar + " >= " + nanVar + " and " + nanVar + " <= " + nanVar, false, Boolean.class);
        assertXPathValue(context, nanVar + " = " + nanVar, false, Boolean.class);
        assertXPathValue(context, nanVar + " != " + nanVar, false, Boolean.class);

        // Test NaN comparisons with numbers
        assertXPathValue(context, nanVar + " > 0", false, Boolean.class);
        assertXPathValue(context, nanVar + " < 0", false, Boolean.class);
        assertXPathValue(context, nanVar + " >= 0", false, Boolean.class);
        assertXPathValue(context, nanVar + " <= 0", false, Boolean.class);
        assertXPathValue(context, nanVar + " >= 0 and " + nanVar + " <= 0", false, Boolean.class);
        assertXPathValue(context, nanVar + " = 0", false, Boolean.class);
        assertXPathValue(context, nanVar + " != 0", false, Boolean.class);

        assertXPathValue(context, nanVar + " > 1", false, Boolean.class);
        assertXPathValue(context, nanVar + " < 1", false, Boolean.class);
        assertXPathValue(context, nanVar + " >= 1", false, Boolean.class);
        assertXPathValue(context, nanVar + " <= 1", false, Boolean.class);
        assertXPathValue(context, nanVar + " >= 1 and " + nanVar + " <= 1", false, Boolean.class);
        assertXPathValue(context, nanVar + " = 1", false, Boolean.class);
        assertXPathValue(context, nanVar + " != 1", false, Boolean.class);
    }

    /**
     * Tests operations on node sets.
     */
    @Test
    void testNodeSetOperations() {
        String arrayVar = "$array";
        assertXPathValue(context, arrayVar + " > 0", true, Boolean.class);
        assertXPathValue(context, arrayVar + " >= 0", true, Boolean.class);
        assertXPathValue(context, arrayVar + " = 0", false, Boolean.class);
        assertXPathValue(context, arrayVar + " = 0.25", true, Boolean.class);
        assertXPathValue(context, arrayVar + " = 0.5", true, Boolean.class);
        assertXPathValue(context, arrayVar + " = 0.50000", true, Boolean.class);
        assertXPathValue(context, arrayVar + " = 0.75", true, Boolean.class);
        assertXPathValue(context, arrayVar + " < 1", true, Boolean.class);
        assertXPathValue(context, arrayVar + " <= 1", true, Boolean.class);
        assertXPathValue(context, arrayVar + " = 1", false, Boolean.class);
        assertXPathValue(context, arrayVar + " > 1", false, Boolean.class);
        assertXPathValue(context, arrayVar + " < 0", false, Boolean.class);
    }
}