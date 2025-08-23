package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests the method resolution capabilities of the {@link org.apache.commons.jxpath.ClassFunctions} class.
 * This test focuses on verifying that methods can be correctly identified based on their argument types.
 */
class ClassFunctionsMethodLookupTest {

    /**
     * A mock ExpressionContext needed to invoke a found Function.
     * The actual context is not relevant to this test's objective.
     */
    private static class MockExpressionContext implements ExpressionContext {
        private final Object object;

        public MockExpressionContext(Object object) {
            this.object = object;
        }

        @Override
        public JXPathContext getJXPathContext() { return null; }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, object, Locale.getDefault());
        }

        @Override
        public List<Pointer> getContextNodeList() { return null; }

        @Override
        public int getPosition() { return 0; }
    }

    /**
     * This test verifies that {@link org.apache.commons.jxpath.ClassFunctions} can find a method
     * when the argument is a custom object type. It specifically looks for a method
     * with the signature `getFoo(TestFunctions arg)`.
     */
    @Test
    void shouldFindMethodWithObjectTypeArgument() {
        // ARRANGE
        // The ClassFunctions instance is configured to find public methods in TestFunctions.class
        // under the "test" namespace.
        Functions classFunctions = new ClassFunctions(TestFunctions.class, "test");

        // The arguments array contains an instance of TestFunctions, which should match
        // a method signature like `getFoo(TestFunctions)`.
        Object[] arguments = {new TestFunctions()};

        // ACT
        // Attempt to find a function named "getFoo" in the "test" namespace
        // that can accept the provided arguments.
        Function foundFunction = classFunctions.getFunction("test", "getFoo", arguments);

        // ASSERT
        assertNotNull(foundFunction, "A function named 'getFoo' with a matching signature should have been found.");

        // The TestFunctions class is expected to have a method:
        // public String getFoo(TestFunctions arg) { return "0"; }
        Object result = foundFunction.invoke(new MockExpressionContext(null), arguments);
        assertEquals("0", result.toString(), "The result of invoking the found function should be '0'.");
    }
}