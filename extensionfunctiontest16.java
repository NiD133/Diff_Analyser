package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Locale;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the method discovery mechanism of the {@link org.apache.commons.jxpath.ClassFunctions} class.
 */
@DisplayName("ClassFunctions Method Lookup")
class ClassFunctionsGetFunctionTest {

    private TypeConverter originalTypeConverter;

    /**
     * A helper class containing a static method to be discovered by the test.
     * The test verifies that JXPath can find this method even when an argument
     * (Integer) needs to be converted to a primitive (int).
     */
    public static class StaticFunctionProvider {
        public static String build(final String foo, final int bar) {
            return "foo=" + foo + "; bar=" + bar;
        }
    }

    @BeforeEach
    void setUp() {
        // JXPath's type conversion relies on a global static TypeConverter.
        // We save the original converter to ensure our test doesn't have side effects.
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    void tearDown() {
        // Restore the original TypeConverter to avoid side effects on other tests.
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    @Test
    @DisplayName("getFunction() should find a static method when arguments require type conversion")
    void getFunction_withConvertibleArguments_findsMatchingStaticMethod() {
        // Arrange
        final Functions classFunctions = new ClassFunctions(StaticFunctionProvider.class, "test");
        final String namespace = "test";
        final String methodName = "build";

        // Arguments: A String and an Integer. The target method expects a String and an int.
        // The test verifies that the type conversion from Integer to int is handled correctly.
        final Object[] arguments = {"7", 1};
        final String expectedResult = "foo=7; bar=1";

        // Act
        final Function foundFunction = classFunctions.getFunction(namespace, methodName, arguments);

        // Assert
        assertNotNull(foundFunction, "A function should have been found for the given name and arguments.");

        final Object result = foundFunction.invoke(new MockExpressionContext(), arguments);
        assertEquals(expectedResult, result.toString(), "The invoked function should produce the correct string output.");
    }

    /**
     * A minimal mock implementation of ExpressionContext required by the Function.invoke() signature.
     * It is not used by the actual method invocation for static functions.
     */
    private static class MockExpressionContext implements ExpressionContext {
        @Override
        public JXPathContext getJXPathContext() {
            return null;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, this, Locale.getDefault());
        }

        @Override
        public List<Pointer> getContextNodeList() {
            return null;
        }

        @Override
        public int getPosition() {
            return 0;
        }
    }
}