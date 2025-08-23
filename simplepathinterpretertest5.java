package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.HashMap;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.TestBeanFactory;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for SimplePathInterpreter focusing on XPath evaluation
 * over a mixed model of Java Beans and DOM nodes.
 * These tests verify that JXPath can navigate from a bean property
 * into a DOM structure and correctly handle both existing and non-existing paths.
 */
@DisplayName("SimplePathInterpreter: Mixed Bean/DOM Model")
public class SimplePathInterpreterTestTest5 {

    private JXPathContext context;

    @BeforeEach
    protected void setUp() throws Exception {
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
    public void findsExistingDomNodeProperty() {
        String path = "/vendor/location/address/city";
        String expectedPath = "/vendor/location[2]/address[1]/city[1]";
        Object expectedValue = "Fruit Market";

        assertPointerPointsToValue(path, expectedValue, expectedPath, DOMNodePointer.class);
    }

    @Test
    public void returnsNullPointerForMissingDomNodeProperty() {
        String path = "/vendor/location/address/pity";
        String expectedPath = "/vendor/location[1]/address[1]/pity";

        assertPathPointsToNull(path, expectedPath, NullPropertyPointer.class);
    }

    @Test
    public void returnsNestedNullPointerForDeeplyMissingPathInDom() {
        String path = "/vendor/location/address/itty/bitty";
        String expectedPath = "/vendor/location[1]/address[1]/itty/bitty";

        assertPathPointsToNull(path, expectedPath, NullPropertyPointer.class);
    }

    @Test
    public void returnsNullPointerForMissingChildOfExistingDomNode() {
        String path = "/vendor/location/address/city/pretty";
        String expectedPath = "/vendor/location[2]/address[1]/city[1]/pretty";

        assertPathPointsToNull(path, expectedPath, NullPropertyPointer.class);
    }

    /**
     * Asserts that an XPath resolves to a specific value and that the resulting
     * Pointer has the expected path and type.
     *
     * @param xpath             The XPath to evaluate.
     * @param expectedValue     The expected value from the XPath.
     * @param expectedPointerPath The expected canonical path of the resulting Pointer.
     * @param expectedPointerType The expected class of the resulting Pointer.
     */
    private void assertPointerPointsToValue(final String xpath, final Object expectedValue,
                                            final String expectedPointerPath, final Class<? extends Pointer> expectedPointerType) {
        // 1. Check the value
        assertEquals(expectedValue, context.getValue(xpath), "Value mismatch for XPath: " + xpath);

        // 2. Check the pointer
        final Pointer pointer = context.getPointer(xpath);
        assertNotNull(pointer, "Pointer should not be null for XPath: " + xpath);
        assertEquals(expectedPointerPath, pointer.asPath(), "Pointer path mismatch for XPath: " + xpath);
        assertInstanceOf(expectedPointerType, pointer, "Pointer type mismatch for XPath: " + xpath);
    }

    /**
     * Asserts that an XPath resolves to a non-existent location, represented by a "null pointer"
     * (e.g., NullPropertyPointer). It verifies the pointer's path, type, and that it
     * correctly indicates a non-actual node.
     *
     * @param xpath             The XPath to evaluate.
     * @param expectedPointerPath The expected canonical path of the resulting null pointer.
     * @param expectedPointerType The expected class of the resulting null pointer.
     */
    private void assertPathPointsToNull(final String xpath, final String expectedPointerPath,
                                        final Class<? extends Pointer> expectedPointerType) {
        final Pointer pointer = context.getPointer(xpath);

        // 1. Check the pointer itself
        assertNotNull(pointer, "A 'null pointer' object should exist for path: " + xpath);
        assertEquals(expectedPointerPath, pointer.asPath(), "Null pointer path mismatch for XPath: " + xpath);
        assertInstanceOf(expectedPointerType, pointer, "Null pointer type mismatch for XPath: " + xpath);

        // 2. Check that the pointer's value is not an actual node
        final NodePointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(valuePointer.isActual(), "Value pointer for a null path should not be 'actual' for XPath: " + xpath);
    }
}