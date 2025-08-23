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
import org.junit.jupiter.api.Test;

public class SimplePathInterpreterTestTest5 {

    private TestBeanWithNode bean;

    private JXPathContext context;

    private void assertNullPointer(final String path, final String expectedPath, final String expectedSignature) {
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "Null path exists: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Null path as path: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Checking Signature: " + path);
        final Pointer vPointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) vPointer).isActual(), "Null path is null: " + path);
        assertEquals(expectedSignature + "N", pointerSignature(vPointer), "Checking value pointer signature: " + path);
    }

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

    /**
     * Since we need to test the internal Signature of a pointer, we will get a signature which will contain a single character per pointer in the chain,
     * representing that pointer's type.
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

    @BeforeEach
    protected void setUp() throws Exception {
        bean = TestBeanWithNode.createTestBeanWithDOM();
        final HashMap submap = new HashMap();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());
        bean.getList().add(new int[] { 1, 2 });
        bean.getList().add(bean.getVendor());
        bean.getMap().put("Key3", new Object[] { new NestedTestBean("some"), Integer.valueOf(2), bean.getVendor(), submap });
        bean.getMap().put("Key4", bean.getVendor());
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);
        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    @Test
    void testDoStepNoPredicatesStandard() {
        // Existing DOM node
        assertValueAndPointer("/vendor/location/address/city", "Fruit Market", "/vendor/location[2]/address[1]/city[1]", "BbMMMM");
        // Missing DOM node
        assertNullPointer("/vendor/location/address/pity", "/vendor/location[1]/address[1]/pity", "BbMMMn");
        // Missing DOM node inside a missing element
        assertNullPointer("/vendor/location/address/itty/bitty", "/vendor/location[1]/address[1]/itty/bitty", "BbMMMnNn");
        // Missing DOM node by search for the best match
        assertNullPointer("/vendor/location/address/city/pretty", "/vendor/location[2]/address[1]/city[1]/pretty", "BbMMMMn");
    }
}
