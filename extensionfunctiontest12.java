package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests the behavior of ClassFunctions when dealing with methods that accept an ExpressionContext.
 *
 * This test focuses on verifying that JXPath can correctly find and invoke an instance method
 * that has an ExpressionContext as one of its parameters.
 */
@DisplayName("ClassFunctions with ExpressionContext")
class ClassFunctionsWithExpressionContextTest {

    /**
     * A mock implementation of ExpressionContext used to supply a specific
     * context value for the test.
     */
    private static class TestExpressionContext implements ExpressionContext {
        private final Object contextObject;

        public TestExpressionContext(Object contextObject) {
            this.contextObject = contextObject;
        }

        @Override
        public Pointer getContextNodePointer() {
            // The value of the context is extracted from this pointer.
            return NodePointer.newNodePointer(null, contextObject, Locale.getDefault());
        }

        // Unused methods for this test
        @Override
        public JXPathContext getJXPathContext() { return null; }
        @Override
        public List<Pointer> getContextNodeList() { return null; }
        @Override
        public int getPosition() { return 0; }
    }

    /**
     * A helper class containing the target method for this test.
     * In the original test suite, this was a more complex, shared class.
     */
    public static class TestFunctions {
        /**
         * An instance method that takes an ExpressionContext and a String.
         * JXPath's invoke mechanism will extract the value from the context.
         */
        public String pathWithSuffix(ExpressionContext context, String suffix) {
            Object contextValue = context.getContextNodePointer().getValue();
            return contextValue + suffix;
        }
    }

    private ClassFunctions classFunctions;

    @BeforeEach
    void setUp() {
        // The System Under Test (SUT) is ClassFunctions, configured to find
        // methods within the TestFunctions class under the "test" namespace.
        classFunctions = new ClassFunctions(TestFunctions.class, "test");
    }

    @Test
    @DisplayName("should find and invoke an instance method accepting an ExpressionContext")
    void shouldFindAndInvokeMethodAcceptingExpressionContext() {
        // ARRANGE
        // The target method is `TestFunctions.pathWithSuffix(ExpressionContext, String)`.
        // Since this is an instance method, the first element of the arguments array
        // must be the instance on which the method will be called.
        final TestFunctions targetInstance = new TestFunctions();
        final String suffixArgument = "*";
        final Object[] lookupArgs = {targetInstance, suffixArgument};

        // The function will be invoked with a custom ExpressionContext that provides an Integer value.
        final Integer contextValue = 1;
        final ExpressionContext invocationContext = new TestExpressionContext(contextValue);

        // ACT
        // 1. Find the function. JXPath's method lookup is able to find a method that
        // accepts an ExpressionContext, even though one is not provided in the lookup arguments.
        final Function function = classFunctions.getFunction("test", "pathWithSuffix", lookupArgs);
        assertNotNull(function, "Function 'test:pathWithSuffix' should be found.");

        // 2. Invoke the function. The `invoke` implementation correctly passes the `invocationContext`
        // to the method, using `targetInstance` from `lookupArgs[0]` as the target object
        // and `suffixArgument` from `lookupArgs[1]` as the second parameter.
        final Object result = function.invoke(invocationContext, lookupArgs);

        // ASSERT
        // The method is expected to concatenate the context value (1) and the suffix ("*").
        final String expectedResult = "1*";
        assertEquals(expectedResult, result, "Method should combine the context value and the string argument.");
    }
}