package org.apache.commons.jxpath.ri.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of JXPath when an extension function returns a NodeSet.
 * It verifies that the returned NodeSet can be further navigated, iterated,
 * and used in other XPath functions.
 */
public class ExtensionFunctionNodeSetReturnTest extends AbstractJXPathTest {

    private JXPathContext context;
    private TestBean testBean;
    private TypeConverter originalTypeConverter;

    @BeforeEach
    @Override
    public void setUp() {
        testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // Register a function library where the "test" namespace is mapped
        // to the static methods of the TestFunctions class.
        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        context.setFunctions(functionLibrary);

        // Preserve the original type converter to restore it after the test.
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        // Restore the original type converter to avoid side effects on other tests.
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    /**
     * Tests that a NodeSet returned by an extension function is correctly handled by the JXPath engine.
     * The test verifies that the NodeSet can be iterated, counted, and that its members' properties
     * and attributes can be accessed via subsequent path expressions.
     */
    @Test
    void extensionFunctionReturningNodeSet_canBeNavigatedAndEvaluated() {
        // Arrange: Define XPath expressions and expected results for clarity.
        final String nodeSetFunction = "test:nodeSet()";
        final Object expectedFirstBean = testBean.getBeans()[0];
        final Object expectedSecondBean = testBean.getBeans()[1];

        // --- Assertions for the NodeSet itself ---

        // 1. Verify that iterating over the function result yields the correct node objects.
        assertXPathValueIterator(context, nodeSetFunction, list(expectedFirstBean, expectedSecondBean));

        // 2. Verify that when evaluated in a scalar context, the function returns the first node's value.
        assertXPathValue(context, nodeSetFunction, expectedFirstBean);

        // 3. Verify that the returned NodeSet can be used as an argument to other XPath functions (e.g., count()).
        final String countXPath = "count(" + nodeSetFunction + ")";
        assertEquals(2, ((Number) context.getValue(countXPath)).intValue(),
                "count() should return the correct size of the NodeSet");

        // --- Assertions for navigating within the NodeSet ---

        // 4. Verify navigation to a child property ('name') within the NodeSet.
        final String namePropertyPath = nodeSetFunction + "/name";
        assertXPathValueIterator(context, namePropertyPath, list("Name 1", "Name 2"));
        assertXPathPointerIterator(context, namePropertyPath, list("/beans[1]/name", "/beans[2]/name"));

        // 5. Verify that accessing a property path in a scalar context returns the value from the first node.
        assertXPathValueAndPointer(context, namePropertyPath, "Name 1", "/beans[1]/name");

        // 6. Verify navigation to an attribute ('@name') within the NodeSet.
        final String nameAttributePath = nodeSetFunction + "/@name";
        assertXPathValueAndPointer(context, nameAttributePath, "Name 1", "/beans[1]/@name");
    }
}