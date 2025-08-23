package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests the automatic type conversion of a NodeSet when passed as an argument to an extension function.
 */
public class ExtensionFunctionNodeSetArgumentTest extends AbstractJXPathTest {

    private JXPathContext context;

    @BeforeEach
    public void setUp() {
        // Arrange: Set up a JXPathContext with a bean and a custom function library.
        TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // Register the "test" namespace with functions from the TestFunctions class.
        // This makes methods like TestFunctions.isInstance() available as "test:isInstance()".
        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        context.setFunctions(functionLibrary);

        // Declare variables to be used in the XPath expressions.
        Variables variables = context.getVariables();
        variables.declareVariable("List.class", List.class);
        variables.declareVariable("NodeSet.class", NodeSet.class);
    }

    /**
     * This test verifies that when a NodeSet is passed as an argument to an extension function,
     * JXPath automatically converts it into a List of the underlying objects.
     */
    @Test
    public void whenNodeSetIsPassedToExtensionFunction_itIsConvertedToList() {
        // The XPath expression "//strings" resolves to a NodeSet. This test checks
        // how JXPath handles this NodeSet when passing it to the "test:isInstance" function.

        // Act & Assert 1: The NodeSet should be converted to a List before the function is called.
        // Therefore, the function should report that the argument is an instance of List.
        final String xpathIsList = "test:isInstance(//strings, $List.class)";
        assertXPathValue(context, xpathIsList, Boolean.TRUE);

        // Act & Assert 2: Because the NodeSet was converted, the function never receives a NodeSet object.
        // Therefore, it should report that the argument is NOT an instance of NodeSet.
        final String xpathIsNodeSet = "test:isInstance(//strings, $NodeSet.class)";
        assertXPathValue(context, xpathIsNodeSet, Boolean.FALSE);
    }
}