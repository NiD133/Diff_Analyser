package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Locale;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestFunctions;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.Test;

/**
 * Tests the constructor resolution mechanism of {@link ClassFunctions}.
 *
 * This test verifies a specific feature: when resolving a constructor (using the "new" function name),
 * ClassFunctions can use the context node from the {@link ExpressionContext} as the first argument,
 * combining it with other explicitly provided arguments.
 */
class ClassFunctionsConstructorResolutionTest {

    /**
     * A mock {@link ExpressionContext} that provides a specific object as the context node.
     * This is used to simulate the JXPath evaluation environment for function resolution.
     */
    private static class TestExpressionContext implements ExpressionContext {
        private final Pointer contextNodePointer;

        /**
         * Creates a context with the given object as the context node.
         * @param contextNode the object to be wrapped in a NodePointer.
         */
        public TestExpressionContext(final Object contextNode) {
            this.contextNodePointer = NodePointer.newNodePointer(null, contextNode, Locale.getDefault());
        }

        @Override
        public Pointer getContextNodePointer() {
            return contextNodePointer;
        }

        // Methods below are not used in this test and can return default values.
        @Override
        public List<Pointer> getContextNodeList() {
            return null;
        }

        @Override
        public JXPathContext getJXPathContext() {
            return null;
        }

        @Override
        public int getPosition() {
            return 0;
        }
    }

    @Test
    void getFunction_shouldFindConstructor_whenUsingContextNodeAsFirstArgument() {
        // ARRANGE
        // We are testing the ability to find a constructor like 'new TestFunctions(int, String)'.
        // The 'int' will come from the ExpressionContext, and the 'String' from the explicit arguments.
        final ClassFunctions functions = new ClassFunctions(TestFunctions.class, "test");
        final String constructorName = "new";
        final Object[] explicitArgs = { "baz" }; // This will be the second argument.

        // The ExpressionContext provides the value for the first constructor argument.
        final Integer firstArgumentFromContext = 1;
        final ExpressionContext context = new TestExpressionContext(firstArgumentFromContext);

        final String expectedToString = "foo=1; bar=baz";

        // ACT
        // Find the constructor. ClassFunctions should match the call signature (context_node, "baz")
        // to the actual constructor 'TestFunctions(int, String)'.
        final Function constructor = functions.getFunction("test", constructorName, explicitArgs);

        // Invoke the found constructor function.
        final Object newInstance = constructor.invoke(context, explicitArgs);

        // ASSERT
        assertNotNull(constructor, "A constructor function should have been found.");
        assertInstanceOf(TestFunctions.class, newInstance, "The created object should be an instance of TestFunctions.");
        assertEquals(expectedToString, newInstance.toString(), "The new instance should be correctly initialized.");
    }
}