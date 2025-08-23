package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.PackageFunctions;
import org.apache.commons.jxpath.TestBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the invocation of methods on collection objects using JXPath extension functions.
 */
public class ExtensionFunctionCollectionTest extends AbstractJXPathTest {

    private JXPathContext context;

    @Override
    @BeforeEach
    public void setUp() {
        // The context is configured with a TestBean as the root object.
        final TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // A FunctionLibrary with PackageFunctions allows JXPath to treat
        // public methods of objects as extension functions (e.g., size(), add()).
        final FunctionLibrary lib = new FunctionLibrary();
        lib.addFunctions(new PackageFunctions("", null));
        context.setFunctions(lib);
    }

    @Test
    public void shouldInvokeMethodOnCollectionVariable() {
        // Arrange
        final List<String> list = new ArrayList<>();
        list.add("foo");
        context.getVariables().declareVariable("myList", list);

        // Act & Assert: Call size() on a collection stored in a JXPath variable.
        assertXPathValue(context, "size($myList)", 1);
    }

    @Test
    public void shouldInvokeMethodOnCollectionProperty() {
        // Arrange: The 'beans' property is part of the root TestBean.
        // By default, it contains two elements.

        // Act & Assert: Call size() on a collection that is a property of the context bean.
        assertXPathValue(context, "size(beans)", 2);
    }

    @Test
    public void shouldInvokeMethodWithSideEffectOnCollection() {
        // Arrange
        final List<String> list = new ArrayList<>();
        list.add("foo");
        context.getVariables().declareVariable("myList", list);

        // Act: The 'add' method is invoked via an XPath expression.
        context.getValue("add($myList, 'hello')");

        // Assert: The invocation should modify the underlying Java list.
        assertEquals(2, list.size(), "The 'add' method should have modified the list size.");
    }

    @Test
    public void shouldInvokeMethodOnRootCollection() {
        // Arrange: Create a new context where the root object itself is a collection.
        final List<String> rootList = new ArrayList<>();
        final JXPathContext collectionContext = JXPathContext.newContext(rootList);

        // Act & Assert: Call size() on the root of the context.
        assertXPathValue(collectionContext, "size(/)", 0);
    }
}