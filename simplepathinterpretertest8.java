package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestNull;
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
 * Tests for {@link SimplePathInterpreter} focusing on its behavior in lenient mode,
 * particularly the creation of "null" pointers for non-existent paths.
 * These tests often verify the internal structure of the generated {@link Pointer}
 * chains, which is crucial for the 'createPath' functionality of JXPath.
 */
@DisplayName("SimplePathInterpreter Lenient Mode Tests")
class SimplePathInterpreterTest {

    private JXPathContext context;

    @BeforeEach
    void setUp() {
        // This setup creates a complex bean structure. While not all parts are used
        // in every test, it provides a rich environment for various XPath queries.
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
    void testPointerForNonExistentIndexedPropertyInLenientMode() {
        // ARRANGE
        // The TestNull object has a "nothing" property which is null.
        context.getVariables().declareVariable("testnull", new TestNull());

        // Path to a non-existent element (index [2]) of a null property.
        // In lenient mode, JXPath should not throw an exception but return
        // a chain of "null pointers" representing the path.
        final String path = "$testnull/nothing[2]";

        // The expected pointer path string.
        final String expectedPointerPath = "$testnull/nothing[2]";

        // The expected signature represents the chain of pointer types created by JXPath:
        // V: VariablePointer for "$testnull"
        // B: BeanPointer for the TestNull object
        // b: BeanPropertyPointer for the "nothing" property
        // E: NullElementPointer for the non-existent index [2]
        final String expectedSignature = "VBbE";

        // ACT & ASSERT
        // The assertNullPointer helper verifies that JXPath creates the correct
        // chain of pointers for a path that does not resolve to an actual value.
        assertNullPointer(path, expectedPointerPath, expectedSignature);
    }

    /**
     * Asserts that a given XPath resolves to a "null pointer" - a pointer that
     * represents a non-existent node in lenient mode. It checks the pointer's path
     * representation, its internal type signature, and confirms its value is null.
     *
     * @param path              The XPath expression to evaluate.
     * @param expectedPath      The expected string representation of the resulting pointer.
     * @param expectedSignature The expected internal type signature of the pointer chain.
     */
    private void assertNullPointer(final String path, final String expectedPath, final String expectedSignature) {
        // Act
        final Pointer pointer = context.getPointer(path);

        // Assert
        assertNotNull(pointer, "A pointer should be returned for the path: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Pointer.asPath() should match the queried path");

        String actualSignature = pointerSignature(pointer);
        assertEquals(expectedSignature, actualSignature, "The chain of pointer types should match the expected signature");

        // A "null pointer" chain's final value should be a non-actual pointer
        final NodePointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(valuePointer.isActual(), "The value pointer for a null path should not be 'actual'");

        String actualValueSignature = pointerSignature(valuePointer);
        assertEquals(expectedSignature + "N", actualValueSignature, "The value pointer should append a NullPointer 'N' to the signature");
    }

    /**
     * Generates a compact signature string representing the types of pointers in a chain.
     * This is a testing utility to verify the internal structure of the pointer hierarchy
     * created by JXPath for a given path.
     * <p>
     * Each character in the signature corresponds to a specific Pointer subclass:
     * <ul>
     *   <li>'V': {@link VariablePointer}</li>
     *   <li>'B': {@link BeanPointer}</li>
     *   <li>'b': {@link BeanPropertyPointer}</li>
     *   <li>'D': {@link DynamicPointer}</li>
     *   <li>'d': {@link DynamicPropertyPointer}</li>
     *   <li>'C': {@link CollectionPointer}</li>
     *   <li>'M': {@link DOMNodePointer}</li>
     *   <li>'N': {@link NullPointer} - Represents a null value</li>
     *   <li>'n': {@link NullPropertyPointer} - Represents a non-existent property</li>
     *   <li>'E': {@link NullElementPointer} - Represents a non-existent collection element</li>
     *   <li>'?': Unknown pointer type</li>
     * </ul>
     * The signature is built by recursively traversing up the parent pointers.
     *
     * @param pointer The leaf pointer of the chain.
     * @return A string representing the pointer types from root to leaf.
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
            System.err.println("UNKNOWN POINTER TYPE: " + pointer.getClass().getName());
        }
        final NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        return pointerSignature(parent) + type;
    }

    // The assertValueAndPointer helper is preserved as it may be used by other tests in the original suite.
    private void assertValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final String expectedSignature) {
        assertValueAndPointer(path, expectedValue, expectedPath, expectedSignature, expectedSignature);
    }

    private void assertValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final String expectedSignature, final String expectedValueSignature) {
        final Object value = context.getValue(path);
        assertEquals(expectedValue, value, "Checking value: " + path);
        final Pointer pointer = context.getPointer(path);
        assertEquals(expectedPath, pointer.toString(), "Checking pointer: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Checking signature: " + path);
        final Pointer vPointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValueSignature, pointerSignature(vPointer), "Checking value pointer signature: " + path);
    }
}