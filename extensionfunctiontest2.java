package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.Variables;
import org.apache.commons.jxpath.util.JXPath11CompatibleTypeConverter;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests specific behaviors of extension functions, particularly concerning type conversion.
 */
public class ExtensionFunctionTestTest2 extends AbstractJXPathTest {

    private JXPathContext context;
    private TypeConverter originalTypeConverter;

    @BeforeEach
    public void setUp() {
        // ARRANGE: Set up a JXPath context with a bean and necessary custom functions.
        TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // Register the "test" namespace with functions from the TestFunctions class.
        // The isInstance() function used in the test is defined there.
        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        context.setFunctions(functionLibrary);

        // Declare variables to be used in XPath expressions.
        Variables variables = context.getVariables();
        variables.declareVariable("List.class", List.class);
        variables.declareVariable("NodeSet.class", NodeSet.class);

        // Save the original type converter to restore it after the test.
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        // Restore the original type converter to avoid side effects on other tests.
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    /**
     * Tests the behavior of the JXPath11CompatibleTypeConverter, which was the
     * default in JXPath 1.1.
     *
     * With this converter, a NodeSet (the result of an XPath query like '//strings')
     * should be recognized as an instance of NodeSet, but NOT as an instance of List.
     * This test ensures that this specific backward-compatible type conversion logic is preserved.
     */
    @Test
    void nodeSetIsNotTreatedAsListWithJXPath11CompatibleConverter() {
        // ARRANGE: Set the specific type converter for backward compatibility testing.
        TypeUtils.setTypeConverter(new JXPath11CompatibleTypeConverter());

        // The XPath expression '//strings' evaluates to a NodeSet.
        // We will check if this NodeSet is considered an instance of List or NodeSet.
        final String xpathCheckIsList = "test:isInstance(//strings, $List.class)";
        final String xpathCheckIsNodeSet = "test:isInstance(//strings, $NodeSet.class)";

        // ACT & ASSERT: Verify the type checking behavior.

        // A NodeSet should NOT be treated as a List with the 1.1 compatible converter.
        assertXPathValue(context, xpathCheckIsList, Boolean.FALSE);

        // A NodeSet should, however, be correctly identified as a NodeSet.
        assertXPathValue(context, xpathCheckIsNodeSet, Boolean.TRUE);
    }
}