package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.Test;

/**
 * Tests the ability of ClassFunctions to resolve and invoke a static method
 * that accepts an ExpressionContext as an argument.
 */
// The original class name 'ExtensionFunctionTestTest17' was uninformative.
// This new name better reflects the specific functionality being tested.
class ClassFunctionsStaticMethodTest {

    /**
     * A simple, self-contained implementation of ExpressionContext for this test.
     * It wraps a single object and provides it as the context node.
     */
    private static class TestExpressionContext implements ExpressionContext {
        private final Pointer contextNodePointer;

        public TestExpressionContext(final Object contextObject) {
            // A NodePointer is used to represent a node in a JXPath graph.
            // Here, it wraps our simple Integer object.
            this.contextNodePointer = NodePointer.newNodePointer(null, contextObject, Locale.getDefault());
        }

        @Override
        public Pointer getContextNodePointer() {
            return contextNodePointer;
        }

        // The following methods are not used in this test and return default values.
        @Override
        public List<Pointer> getContextNodeList() { return Collections.singletonList(contextNodePointer); }
        @Override
        public JXPathContext getJXPathContext() { return null; }
        @Override
        public int getPosition() { return 0; }
    }

    /**
     * A local, static inner class containing the function to be tested.
     * This makes the test self-contained and easy to understand without
     * needing to look at other files. The method signature `path(ExpressionContext)`
     * is the key element being tested.
     */
    public static class TestFunctions {
        public static String path(final ExpressionContext context) {
            // The function is expected to extract the value from the context node.
            return String.valueOf(context.getContextNodePointer().getValue());
        }
    }

    /**
     * Verifies that ClassFunctions can find a static method that takes an
     * ExpressionContext and that the context's node value is correctly passed
     * during invocation.
     */
    @Test
    void staticFunctionWithExpressionContextIsInvokedWithContextNodeValue() {
        // ARRANGE
        // ClassFunctions provides access to static methods of a class as XPath functions.
        final ClassFunctions classFunctions = new ClassFunctions(TestFunctions.class, "test");
        final Object[] functionArgs = {}; // The XPath function test:path() takes no arguments.

        // The ExpressionContext provides the runtime context for the function.
        // We create a context where the current node is an Integer with the value 1.
        final ExpressionContext expressionContext = new TestExpressionContext(1);

        // ACT
        // 1. Find the function 'test:path'. JXPath should discover the static method
        //    'TestFunctions.path(ExpressionContext)'.
        final Function pathFunction = classFunctions.getFunction("test", "path", functionArgs);
        assertNotNull(pathFunction, "Function 'test:path' should be found in TestFunctions.");

        // 2. Invoke the function. The implementation of path() is expected to extract
        //    the value from our expressionContext (the Integer 1) and convert it to a String.
        final Object result = pathFunction.invoke(expressionContext, functionArgs);

        // ASSERT
        assertEquals("1", result, "The function should return the string representation of the context node's value.");
    }
}