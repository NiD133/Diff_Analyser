package org.apache.commons.jxpath.ri.axes;

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

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for SimplePathInterpreter with predicates on XML nodes")
class SimplePathInterpreterPredicatesTest {

    private JXPathContext context;

    /**
     * Sets up a complex bean structure with nested objects, lists, maps, and a DOM element.
     * This data is used as the context for JXPath evaluations.
     */
    @BeforeEach
    void setup() {
        final TestBeanWithNode bean = TestBeanWithNode.createTestBeanWithDOM();
        final HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());

        bean.getList().add(new int[]{1, 2});
        bean.getList().add(bean.getVendor());
        bean.getMap().put("Key3", new Object[]{new NestedTestBean("some"), 2, bean.getVendor(), submap});
        bean.getMap().put("Key4", bean.getVendor());
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);

        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    @Test
    @DisplayName("Finds an XML node using an attribute predicate like [@name='value']")
    void findNodeByAttributePredicate() {
        // This XPath looks for a <contact> element with a 'name' attribute equal to 'jack'.
        assertPathResolvesToValueAndPointer(
                "/vendor/contact[@name='jack']",
                "Jack",
                "/vendor/contact[2]",
                "BbMM");
    }

    @Test
    @DisplayName("Finds an XML node using a numeric index like [2]")
    void findNodeByIndexPredicate() {
        // This XPath selects the second <contact> element.
        assertPathResolvesToValueAndPointer(
                "/vendor/contact[2]",
                "Jack",
                "/vendor/contact[2]",
                "BbMM");
    }

    @Test
    @DisplayName("Returns a NullPointer for an out-of-bounds index")
    void returnsNullPointerForOutOfBoundsIndex() {
        // This XPath attempts to select a non-existent fifth <contact> element.
        assertPathResolvesToNullPointer(
                "/vendor/contact[5]",
                "/vendor/contact[5]",
                "BbMn");
    }

    @Test
    @DisplayName("Finds an XML node using combined attribute and index predicates")
    void findNodeByCombinedPredicates() {
        // This XPath first finds all <contact> elements with name='jack',
        // and then selects the second one from that result set.
        assertPathResolvesToValueAndPointer(
                "/vendor/contact[@name='jack'][2]",
                "Jack Black",
                "/vendor/contact[4]",
                "BbMM");
    }

    // -----------------------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------------------

    /**
     * Asserts that an XPath resolves to a null pointer, verifying its path and internal signature.
     */
    private void assertPathResolvesToNullPointer(final String path, final String expectedPath, final String expectedSignature) {
        final Pointer pointer = context.getPointer(path);

        assertAll("Assertions for null pointer at path: " + path,
                () -> assertNotNull(pointer, "Pointer should not be null"),
                () -> assertEquals(expectedPath, pointer.asPath(), "asPath() should match expected path"),
                () -> assertEquals(expectedSignature, pointerSignature(pointer), "Pointer signature should match"),
                () -> {
                    final NodePointer valuePointer = ((NodePointer) pointer).getValuePointer();
                    assertAll("Assertions for the value pointer",
                            () -> assertFalse(valuePointer.isActual(), "Value pointer should not be actual (i.e., should represent null)"),
                            () -> assertEquals(expectedSignature + "N", pointerSignature(valuePointer), "Value pointer signature should indicate a NullPointer")
                    );
                }
        );
    }

    /**
     * Asserts that an XPath resolves to a specific value and pointer, verifying its path and internal signature.
     */
    private void assertPathResolvesToValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final String expectedSignature) {
        assertPathResolvesToValueAndPointer(path, expectedValue, expectedPath, expectedSignature, expectedSignature);
    }

    /**
     * Asserts that an XPath resolves to a specific value and pointer, verifying its path and internal signatures.
     */
    private void assertPathResolvesToValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final String expectedPointerSignature, final String expectedValueSignature) {
        final Pointer pointer = context.getPointer(path);
        final Object value = context.getValue(path);

        assertAll("Assertions for path: " + path,
                () -> assertEquals(expectedValue, value, "getValue() should return the correct value"),
                () -> assertEquals(expectedPath, pointer.toString(), "Pointer's toString() representation should match"),
                () -> assertEquals(expectedPointerSignature, pointerSignature(pointer), "Pointer signature should match"),
                () -> {
                    final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
                    assertEquals(expectedValueSignature, pointerSignature(valuePointer), "Value pointer signature should match");
                }
        );
    }

    /**
     * Generates a signature string representing the chain of pointer types.
     * This is a white-box testing utility to verify the internal pointer structure
     * created by JXPath. Each character in the signature corresponds to a specific
     * NodePointer implementation in the hierarchy.
     *
     * <p><b>Signature Codes:</b></p>
     * <ul>
     *   <li>'V': {@link VariablePointer} - A pointer to a context variable.</li>
     *   <li>'B': {@link BeanPointer} - A pointer to a JavaBean.</li>
     *   <li>'b': {@link BeanPropertyPointer} - A pointer to a property of a JavaBean.</li>
     *   <li>'C': {@link CollectionPointer} - A pointer to a collection.</li>
     *   <li>'D': {@link DynamicPointer} - A pointer to a dynamic object (e.g., a Map).</li>
     *   <li>'d': {@link DynamicPropertyPointer} - A pointer to a property of a dynamic object.</li>
     *   <li>'M': {@link DOMNodePointer} - A pointer to a DOM node.</li>
     *   <li>'N': {@link NullPointer} - A pointer to a non-existent object (path evaluates to null).</li>
     *   <li>'n': {@link NullPropertyPointer} - A pointer to a non-existent property of an existing object.</li>
     *   <li>'E': {@link NullElementPointer} - A pointer to a non-existent element in a collection.</li>
     *   <li>'?': Unknown pointer type.</li>
     * </ul>
     *
     * @param pointer The end pointer of the chain to generate a signature for.
     * @return A string representing the types of pointers from the root to the given pointer.
     */
    private String pointerSignature(final Pointer pointer) {
        if (pointer == null) {
            return "";
        }
        char type = '?';
        if (pointer instanceof NullPointer) {
            type = 'N';
        } else if (pointer instanceof NullPropertyPointer) {
            type = 'n';
        } else if (pointer instanceof NullElementPointer) {
            type = 'E';
        } else if (pointer instanceof VariablePointer) {
            type = 'V';
        } else if (pointer instanceof CollectionPointer) {
            type = 'C';
        } else if (pointer instanceof BeanPointer) {
            type = 'B';
        } else if (pointer instanceof BeanPropertyPointer) {
            type = 'b';
        } else if (pointer instanceof DynamicPointer) {
            type = 'D';
        } else if (pointer instanceof DynamicPropertyPointer) {
            type = 'd';
        } else if (pointer instanceof DOMNodePointer) {
            type = 'M';
        } else {
            System.err.println("UNKNOWN TYPE: " + pointer.getClass());
        }
        final NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        return pointerSignature(parent) + type;
    }
}