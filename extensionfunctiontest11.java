package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Locale;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the invocation of extension functions, specifically how an ExpressionContext
 * can provide the context node as an argument to the underlying method.
 */
public class ExtensionFunctionInvocationTest extends AbstractJXPathTest {

    private TypeConverter originalTypeConverter;

    @BeforeEach
    public void setUp() {
        // Back up the original type converter to ensure test isolation.
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        // Restore the original type converter to prevent side effects on other tests.
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    /**
     * A simple stub for ExpressionContext used to provide a specific
     * context node for testing function invocation.
     */
    private static final class StubExpressionContext implements ExpressionContext {
        private final Object object;

        public StubExpressionContext(final Object object) {
            this.object = object;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, object, Locale.getDefault());
        }

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
    void invoke_usesContextNodeFromExpressionContextAsArgument() {
        // Arrange
        // We are testing the mechanism that finds and invokes a Java method via JXPath.
        // The 'ClassFunctions' object represents a library of functions based on the
        // public methods of a given class (TestFunctions in this case).
        final Functions functionLibrary = new ClassFunctions(TestFunctions.class, "test");

        // For an instance method, the arguments for the 'getFunction' lookup must include
        // the target object instance.
        final Object[] lookupArgs = { new TestFunctions() };

        // The 'invoke' method takes an ExpressionContext. We create a stub that provides
        // an Integer as the "context node". This node is expected to be passed as an
        // argument to the resolved Java method.
        // (This test assumes TestFunctions has a method like 'public String instancePath(Integer arg)')
        final ExpressionContext invocationContext = new StubExpressionContext(1);
        final String expectedResult = "1";

        // Act
        // 1. Resolve the function based on its name ("instancePath") and argument types.
        final Function resolvedFunction = functionLibrary.getFunction("test", "instancePath", lookupArgs);

        // 2. Invoke the function. The JXPath runtime should extract the context node (Integer 1)
        // from our stub context and use it as the method argument.
        final Object actualResult = resolvedFunction.invoke(invocationContext, lookupArgs);

        // Assert
        assertEquals(expectedResult, actualResult,
                "The result of the function should be the string representation of the context node.");
    }
}