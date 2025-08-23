package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Locale;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestFunctions; // Assuming TestFunctions is in this package or imported
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of looking up and invoking class constructors as extension functions.
 */
public class ExtensionFunctionTestTest5 extends AbstractJXPathTest {

    /**
     * A minimal ExpressionContext implementation needed for invoking the function.
     */
    private static final class MinimalExpressionContext implements ExpressionContext {
        private final Object object;

        public MinimalExpressionContext(final Object object) {
            this.object = object;
        }

        @Override
        public Pointer getContextNodePointer() {
            // The context node is not relevant for this test, but a pointer is required.
            return NodePointer.newNodePointer(null, object, Locale.getDefault());
        }

        @Override
        public List<Pointer> getContextNodeList() {
            return null; // Not used
        }

        @Override
        public JXPathContext getJXPathContext() {
            return null; // Not used
        }

        @Override
        public int getPosition() {
            return 0; // Not used
        }
    }

    /**
     * Verifies that ClassFunctions can find a constructor using the special "new"
     * function name and that the resulting Function object can be invoked to
     * create and initialize a new instance of the target class.
     */
    @Test
    void getFunction_withNewKeyword_shouldFindAndInvokeConstructor() {
        // Arrange: Set up ClassFunctions to expose constructors of TestFunctions
        // under the "test" namespace.
        final Functions classFunctions = new ClassFunctions(TestFunctions.class, "test");
        final Object[] constructorArgs = { 1, "x" };

        // Act: Look up the constructor function using "new" as the function name.
        final Function constructorAsFunction = classFunctions.getFunction("test", "new", constructorArgs);

        // Assert: Verify that a valid function was found and that it correctly
        // creates and initializes a TestFunctions object when invoked.
        assertNotNull(constructorAsFunction, "A constructor function should have been found.");

        final Object result = constructorAsFunction.invoke(new MinimalExpressionContext(null), constructorArgs);

        assertInstanceOf(TestFunctions.class, result, "The result should be an instance of TestFunctions.");
        final TestFunctions newInstance = (TestFunctions) result;

        assertEquals(1, newInstance.getFoo(), "The 'foo' property should be initialized by the constructor.");
        assertEquals("x", newInstance.getBar(), "The 'bar' property should be initialized by the constructor.");
    }
}