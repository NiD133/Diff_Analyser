package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.PackageFunctions;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.Variables;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of extension functions that utilize the ExpressionContext
 * to access information about the current node or pointer during XPath evaluation.
 */
public class ExtensionFunctionWithExpressionContextTest extends AbstractJXPathTest {

    private JXPathContext context;
    private TypeConverter originalTypeConverter;

    @BeforeEach
    public void setUp() {
        TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // Set up custom functions for the test
        FunctionLibrary lib = new FunctionLibrary();
        lib.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        lib.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
        lib.addFunctions(new PackageFunctions("", "call"));
        lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
        lib.addFunctions(new PackageFunctions("", null));
        context.setFunctions(lib);

        // Declare variables used in tests
        Variables vars = context.getVariables();
        vars.declareVariable("test", new TestFunctions(4, "test"));
        vars.declareVariable("List.class", List.class);
        vars.declareVariable("NodeSet.class", NodeSet.class);

        // Preserve original type converter to restore it after the test
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        // Restore the original type converter to avoid side effects on other tests
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    @Test
    public void functionCanAccessCurrentNodeToFilterResults() {
        // This test verifies that an extension function used in a predicate
        // can access the current node from the ExpressionContext.
        // The 'test:isMap()' function checks if the current node is a Map.
        String xpath = "//.[test:isMap()]/Key1";
        String expected = "Value 1";
        assertXPathValue(context, xpath, expected);
    }

    @Test
    public void functionCanUseCurrentNodePropertyInPredicate() {
        // This test shows that an extension function in a predicate can evaluate
        // a property of the current node.
        // 'test:count(strings)' counts the elements in the 'strings' property
        // of the current node ('.'). The predicate filters for nodes where this count is 3.
        String xpath = "count(//.[test:count(strings) = 3])";
        double expected = 7.0;
        assertXPathValue(context, xpath, expected);
    }

    @Test
    public void functionReceivesNodeSetAsCollectionOfValues() {
        // This test confirms that when a node-set is passed as an argument to a function,
        // JXPath converts it into a collection of the nodes' values (in this case, Strings).
        String xpath = "test:count(//strings)";
        int expected = 21;
        assertXPathValue(context, xpath, expected);
    }

    @Test
    public void functionCanReceiveNodeSetAsCollectionOfPointers() {
        // In contrast to the previous test, this one verifies that a function can be
        // designed to receive a collection of Pointers for a node-set argument,
        // rather than the nodes' values.
        String xpath = "test:countPointers(//strings)";
        int expected = 21;
        assertXPathValue(context, xpath, expected);
    }

    @Test
    public void functionCanAccessCurrentPointerPath() {
        // This test demonstrates that an extension function can access the current
        // pointer from the ExpressionContext and use its properties, such as its path.
        // 'test:path()' returns the canonical path of the current pointer.
        String xpath = "/beans[contains(test:path(), '[2]')]/name";
        String expected = "Name 2";
        assertXPathValue(context, xpath, expected);
    }
}