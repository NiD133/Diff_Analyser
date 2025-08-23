package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.TestBeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SimplePathInterpreter Predicate Handling")
class SimplePathInterpreterPredicatesTest {

    private JXPathContext context;
    private Object vendorNode;

    @BeforeEach
    void setUp() {
        final TestBeanWithNode bean = TestBeanWithNode.createTestBeanWithDOM();
        vendorNode = bean.getVendor();

        final HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());

        bean.getList().add(new int[]{1, 2});
        bean.getList().add(vendorNode);

        bean.getMap().put("Key3", new Object[]{new NestedTestBean("some"), 2, vendorNode, submap});
        bean.getMap().put("Key4", vendorNode);
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);

        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    /**
     * Asserts that a given JXPath resolves to an existing node with the correct value
     * and canonical path.
     *
     * @param path                The JXPath to evaluate.
     * @param expectedValue       The expected value from context.getValue(path).
     * @param expectedCanonicalPath The expected canonical path from pointer.asPath().
     */
    private void assertPathResolvesToValue(final String path, final Object expectedValue, final String expectedCanonicalPath) {
        // Check that the path resolves to the correct value
        assertEquals(expectedValue, context.getValue(path),
            () -> "GetValue for path '" + path + "' should return the correct value.");

        // Check that the pointer to the path is valid and has the correct canonical representation
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, () -> "Pointer for path '" + path + "' should not be null.");
        assertTrue(((NodePointer) pointer).isActual(),
            () -> "Pointer for path '" + path + "' should be actual.");
        assertEquals(expectedCanonicalPath, pointer.asPath(),
            () -> "Pointer for path '" + path + "' should have the correct canonical path.");
    }

    /**
     * Asserts that a given JXPath resolves to a "null pointer" for a non-existent node.
     * In lenient mode, JXPath returns a non-null Pointer object that represents the
     * non-existent node and is marked as not "actual".
     *
     * @param path                The JXPath to evaluate.
     * @param expectedCanonicalPath The expected canonical path for the non-existent node.
     */
    private void assertPathResolvesToNullPointer(final String path, final String expectedCanonicalPath) {
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, () -> "Pointer for non-existent path '" + path + "' should not be null in lenient mode.");

        // The pointer should correctly represent the path that was queried.
        assertEquals(expectedCanonicalPath, pointer.asPath(),
            () -> "NullPointer for path '" + path + "' should have the correct canonical path.");

        // The key characteristic of a NullPointer is that it's not "actual".
        assertFalse(((NodePointer) pointer).isActual(),
            () -> "Pointer for path '" + path + "' should NOT be actual.");
    }

    @Test
    @DisplayName("Path with chained name predicates on a collection resolves to the correct node")
    void pathWithChainedNamePredicatesResolvesToNode() {
        assertPathResolvesToValue("map[@name='Key3'][@name='fruitco']", vendorNode, "/map[@name='Key3'][3]");
    }

    @Test
    @DisplayName("Path with chained name predicates for a missing node returns a null pointer")
    void pathWithChainedNamePredicatesForMissingNodeReturnsNullPointer() {
        assertPathResolvesToNullPointer("map[@name='Key3'][@name='foo']", "/map[@name='Key3'][4][@name='foo']");
    }

    @Test
    @DisplayName("Path with a name predicate that matches the node itself succeeds")
    void pathWithNamePredicateMatchingNodeSucceeds() {
        assertPathResolvesToValue("map[@name='Key4'][@name='fruitco']", vendorNode, "/map[@name='Key4']");
    }

    @Test
    @DisplayName("Path with a name predicate on an empty collection returns a null pointer")
    void pathWithNamePredicateOnEmptyCollectionReturnsNullPointer() {
        assertPathResolvesToNullPointer("map[@name='Key6'][@name='fruitco']", "/map[@name='Key6'][@name='fruitco']");
    }

    @Test
    @DisplayName("Path with name and index predicates resolves to the correct value")
    void pathWithNameAndIndexPredicatesResolvesToValue() {
        // Find all 'contact' nodes with name 'jack', then take the second one.
        assertPathResolvesToValue("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]");
    }

    @Test
    @DisplayName("Path with name and an invalid index predicate returns a null pointer")
    void pathWithNameAndInvalidIndexPredicateReturnsNullPointer() {
        // There is no 5th 'contact' node with name 'jack'.
        assertPathResolvesToNullPointer("/vendor/contact[@name='jack'][5]", "/vendor/contact[@name='jack'][5]");
    }

    @Test
    @DisplayName("Path with a self-axis and name predicate resolves to the first match")
    void pathWithSelfNodeAndNamePredicateResolvesToFirstMatch() {
        // This is equivalent to /vendor/contact[@name='jack']
        assertPathResolvesToValue("/vendor/contact/.[@name='jack']", "Jack", "/vendor/contact[2]");
    }
}