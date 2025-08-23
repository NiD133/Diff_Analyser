package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import java.util.Locale;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.PackageFunctions;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.Variables;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test suite contains tests for JXPath extension functions.
 * The specific test case in this class focuses on the behavior of the {@link ClassFunctions} utility.
 */
public class ExtensionFunctionTestTest15 extends AbstractJXPathTest {

    private Functions functions;
    private JXPathContext context;
    private TestBean testBean;
    private TypeConverter typeConverter;

    @Override
    @BeforeEach
    public void setUp() {
        // This setup initializes a full JXPathContext with various function libraries.
        // For the test in this class, only the initialization of the 'functions' field is relevant.
        if (context == null) {
            testBean = new TestBean();
            context = JXPathContext.newContext(testBean);
            final Variables vars = context.getVariables();
            vars.declareVariable("test", new TestFunctions(4, "test"));
            final FunctionLibrary lib = new FunctionLibrary();
            lib.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
            lib.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
            lib.addFunctions(new PackageFunctions("", "call"));
            lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
            lib.addFunctions(new PackageFunctions("", null));
            context.setFunctions(lib);
            context.getVariables().declareVariable("List.class", List.class);
            context.getVariables().declareVariable("NodeSet.class", NodeSet.class);
        }
        // This test specifically uses a ClassFunctions object that exposes
        // static methods from TestFunctions.class in the "test" namespace.
        functions = new ClassFunctions(TestFunctions.class, "test");
        typeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        TypeUtils.setTypeConverter(typeConverter);
    }

    /**
     * A mock ExpressionContext needed for invoking a JXPath Function.
     * It provides a minimal, non-functional context for the test.
     */
    private static final class MockExpressionContext implements ExpressionContext {
        private final Object contextObject;

        public MockExpressionContext(final Object contextObject) {
            this.contextObject = contextObject;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, contextObject, Locale.getDefault());
        }

        @Override
        public List<Pointer> getContextNodeList() {
            return null; // Not used in this test
        }

        @Override
        public JXPathContext getJXPathContext() {
            return null; // Not used in this test
        }

        @Override
        public int getPosition() {
            return 0; // Not used in this test
        }
    }

    /**
     * Tests that ClassFunctions can find a static method based on its name and
     * argument types, and that the resulting Function object can be invoked correctly.
     */
    @Test
    void getFunction_findsAndInvokesStaticMethod_withMatchingArguments() {
        // Arrange
        // The 'functions' object, a ClassFunctions instance, is configured in setUp()
        // to expose static methods from TestFunctions.class under the "test" namespace.
        // We will look for a method "build" that matches the argument types (Integer, String).
        final Object[] arguments = { 1, "x" };
        final String expectedResult = "foo=1; bar=x"; // Expected from TestFunctions.build(1, "x")

        // Act
        // Attempt to find the function "test:build" with the specified arguments.
        final Function buildFunction = functions.getFunction("test", "build", arguments);

        // Assert
        // 1. Verify that the function was successfully found.
        assertNotNull(buildFunction, "The 'build' function should be found in TestFunctions.class");

        // 2. Invoke the function and verify its result.
        // A mock context is passed, but it's not used by the static method.
        final Object actualResult = buildFunction.invoke(new MockExpressionContext(null), arguments);
        assertEquals(expectedResult, actualResult.toString(),
                "Invocation of 'test:build(1, x)' should produce the correct string.");
    }
}