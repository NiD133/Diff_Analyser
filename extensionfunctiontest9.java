package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.Variables;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

/**
 * Tests the invocation of methods on context objects as extension functions.
 */
public class ExtensionFunctionMethodCallTest extends AbstractJXPathTest {

    private JXPathContext context;

    /**
     * Sets up the JXPath context for the tests.
     * This context is configured with custom functions and variables to test
     * various method call scenarios.
     */
    @Override
    @BeforeEach
    public void setUp() {
        TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // Declare a variable '$test' holding an object with methods to be called.
        Variables vars = context.getVariables();
        vars.declareVariable("test", new TestFunctions(4, "test"));

        // Set up a function library to test different ways of calling methods.
        FunctionLibrary lib = new FunctionLibrary();
        // Registers methods from TestFunctions and TestFunctions2 under the "test" namespace.
        lib.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        lib.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
        // Registers static methods from the default package under the "call" namespace.
        lib.addFunctions(new PackageFunctions("", "call"));
        // Registers static methods from a specific package under the "jxpathtest" namespace.
        lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
        // Allows calling methods on objects without any namespace prefix.
        lib.addFunctions(new PackageFunctions("", null));
        context.setFunctions(lib);
    }

    /**
     * This private inner class appears to be a helper for creating a custom
     * ExpressionContext. It is not used by the tests in this file but is
     * likely used by other tests in the original, larger test suite.
     */
    private static final class Context implements ExpressionContext {
        private final Object object;

        public Context(final Object object) {
            this.object = object;
        }

        @Override
        public List<Pointer> getContextNodeList() {
            return null;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, object, Locale.getDefault());
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
    void standardXPathFunction_shouldBeEvaluatedCorrectly() {
        assertXPathValue(context, "length('foo')", 3);
    }

    @Test
    void methodOnLiteral_shouldBeInvokedWithNamespace() {
        // JXPath can invoke a method on a literal value. In this case, 'substring'
        // is a method of the String class. The "call" prefix is specified but
        // is effectively ignored when the target is an object instance.
        assertXPathValue(context, "call:substring('foo', 1, 2)", "o");
    }

    @Test
    void methodOnVariable_shouldBeInvokedUsingRegisteredPrefix() {
        // The '$test' variable holds a TestFunctions object.
        // 'test:getFoo($test)' is treated as a method call on the $test object.
        assertXPathValue(context, "string(test:getFoo($test))", "4");
    }

    @Test
    void methodOnVariable_shouldBeInvokedWithoutPrefix() {
        // Because we registered `new PackageFunctions("", null)`, JXPath can
        // resolve 'getFoo' as a method on the '$test' object without a namespace prefix.
        assertXPathValue(context, "string(getFoo($test))", "4");
    }

    @Test
    void methodOnVariable_shouldBeInvokedIgnoringUnrelatedPrefix() {
        // Even with an unrelated prefix, JXPath resolves 'getFoo' as a method
        // on the '$test' object because it's the first argument.
        assertXPathValue(context, "string(call:getFoo($test))", "4");
    }

    @Test
    void methodOnVariable_shouldHandleMultipleArguments() {
        // Verifies that a method with multiple arguments can be called correctly.
        String expected = "foo=7; bar=biz";
        assertXPathValue(context, "string(test:setFooAndBar($test, 7, 'biz'))", expected);
    }
}