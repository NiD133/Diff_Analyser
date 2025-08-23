package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.CollectionPointer;
import org.apache.commons.jxpath.ri.model.beans.NullElementPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.TestBeanFactory;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.model.dynamic.DynamicPointer;
import org.apache.commons.jxpath.ri.model.dynamic.DynamicPropertyPointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the SimplePathInterpreter when handling predicates
 * on non-existent properties in lenient mode.
 */
@DisplayName("SimplePathInterpreter: Predicates on Missing Properties")
class SimplePathInterpreterPredicatesOnNullPropertyTest {

    private JXPathContext context;

    @BeforeEach
    void setUp() {
        // This setup creates a complex bean structure and a JXPathContext in lenient mode.
        // Lenient mode is crucial because it ensures that paths to non-existent
        // properties result in NullPointer objects rather than exceptions.
        TestBeanWithNode bean = TestBeanWithNode.createTestBeanWithDOM();
        HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());
        bean.getList().add(new int[] { 1, 2 });
        bean.getList().add(bean.getVendor());
        bean.getMap().put("Key3", new Object[] { new NestedTestBean("some"), 2, bean.getVendor(), submap });
        bean.getMap().put("Key4", bean.getVendor());
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);

        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    @Test
    @DisplayName("XPath with an attribute predicate on a missing property should return a NullPointer")
    void xpathWithAttributePredicateOnMissingPropertyReturnsNullPointer() {
        // This path attempts to access a missing property 'foo' and then apply a predicate to it.
        // JXPath in lenient mode should produce a chain of NullPointers.
        final String path = "/foo[@name='foo']";

        // The expected signature "BnNn" represents the pointer chain:
        // B: BeanPointer (the root bean)
        // n: NullPropertyPointer (for the missing property 'foo')
        // N: NullPointer (the result of the predicate on the null property)
        // n: NullPropertyPointer (for the '@name' attribute used in the predicate)
        final String expectedSignature = "BnNn";

        assertXPathResultsInNullPointer(path, path, expectedSignature);
    }

    @Test
    @DisplayName("XPath with an index predicate on a missing property should return a NullPointer")
    void xpathWithIndexPredicateOnMissingPropertyReturnsNullPointer() {
        // This path attempts to access a missing property 'foo' and then an index within it.
        // JXPath in lenient mode should produce a NullPropertyPointer for the indexed element.
        final String path = "/foo[3]";

        // The expected signature "Bn" represents the pointer chain:
        // B: BeanPointer (the root bean)
        // n: NullPropertyPointer (for the missing property 'foo' at index 3)
        final String expectedSignature = "Bn";

        assertXPathResultsInNullPointer(path, path, expectedSignature);
    }

    /**
     * Asserts that a given XPath expression resolves to a NullPointer chain with a specific structure.
     *
     * @param path              The XPath expression to evaluate.
     * @param expectedPath      The expected string representation of the resulting pointer.
     * @param expectedSignature The expected signature representing the types of pointers in the chain.
     */
    private void assertXPathResultsInNullPointer(final String path, final String expectedPath, final String expectedSignature) {
        final Pointer pointer = context.getPointer(path);

        assertNotNull(pointer, () -> "Pointer for path '" + path + "' should not be null.");
        assertEquals(expectedPath, pointer.asPath(), () -> "Pointer.asPath() for '" + path + "' should be correct.");
        assertEquals(expectedSignature, generatePointerSignature(pointer), () -> "Pointer signature for '" + path + "' should be correct.");

        // The value pointer of a NullPointer chain should represent a non-existent value.
        final NodePointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(valuePointer.isActual(), () -> "Value pointer for path '" + path + "' should not be 'actual'.");

        // The signature of the value pointer should be the parent's signature plus 'N' for NullPointer.
        assertEquals(expectedSignature + "N", generatePointerSignature(valuePointer),
                () -> "Value pointer signature for '" + path + "' should be correct.");
    }

    /**
     * Generates a signature string for a given Pointer chain. Each character in the
     * signature represents the type of a pointer in the chain, allowing verification
     * of the internal structure created by JXPath.
     * <p>
     * Signature key:
     * 'B' = BeanPointer, 'b' = BeanPropertyPointer
     * 'D' = DynamicPointer, 'd' = DynamicPropertyPointer
     * 'C' = CollectionPointer, 'E' = NullElementPointer
     * 'N' = NullPointer, 'n' = NullPropertyPointer
     * 'V' = VariablePointer, 'M' = DOMNodePointer
     * '?' = Unknown
     *
     * @param pointer The pointer to generate a signature for.
     * @return A string representing the pointer chain structure.
     */
    private String generatePointerSignature(final Pointer pointer) {
        if (pointer == null) {
            return "";
        }
        char type = '?';
        if (pointer instanceof NullPointer) type = 'N';
        else if (pointer instanceof NullPropertyPointer) type = 'n';
        else if (pointer instanceof NullElementPointer) type = 'E';
        else if (pointer instanceof VariablePointer) type = 'V';
        else if (pointer instanceof CollectionPointer) type = 'C';
        else if (pointer instanceof BeanPointer) type = 'B';
        else if (pointer instanceof BeanPropertyPointer) type = 'b';
        else if (pointer instanceof DynamicPointer) type = 'D';
        else if (pointer instanceof DynamicPropertyPointer) type = 'd';
        else if (pointer instanceof DOMNodePointer) type = 'M';

        final NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        return generatePointerSignature(parent) + type;
    }
}