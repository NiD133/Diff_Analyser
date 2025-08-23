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

public class SimplePathInterpreterTestTest4 {

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
    void testDoStepNoPredicatesPropertyOwner() {
        // Existing scalar property
        assertValueAndPointer("/int", Integer.valueOf(1), "/int", "Bb", "BbB");
        // self::
        assertValueAndPointer("/./int", Integer.valueOf(1), "/int", "Bb", "BbB");
        // Missing property
        assertNullPointer("/foo", "/foo", "Bn");
        // existingProperty/existingScalarProperty
        assertValueAndPointer("/nestedBean/int", Integer.valueOf(1), "/nestedBean/int", "BbBb", "BbBbB");
        // existingProperty/collectionProperty
        assertValueAndPointer("/nestedBean/strings", bean.getNestedBean().getStrings(), "/nestedBean/strings", "BbBb", "BbBbC");
        // existingProperty/missingProperty
        assertNullPointer("/nestedBean/foo", "/nestedBean/foo", "BbBn");
        // map/missingProperty
        assertNullPointer("/map/foo", "/map[@name='foo']", "BbDd");
        // Existing property by search in collection
        assertValueAndPointer("/list/int", Integer.valueOf(1), "/list[3]/int", "BbBb", "BbBbB");
        // Missing property by search in collection
        assertNullPointer("/list/foo", "/list[1]/foo", "BbBn");
        // existingProperty/missingProperty/missingProperty
        assertNullPointer("/nestedBean/foo/bar", "/nestedBean/foo/bar", "BbBnNn");
        // collection/existingProperty/missingProperty
        assertNullPointer("/list/int/bar", "/list[3]/int/bar", "BbBbBn");
        // collectionProperty/missingProperty/missingProperty
        assertNullPointer("/list/foo/bar", "/list[1]/foo/bar", "BbBnNn");
        // map/missingProperty/anotherStep
        assertNullPointer("/map/foo/bar", "/map[@name='foo']/bar", "BbDdNn");
        // Existing dynamic property
        assertValueAndPointer("/map/Key1", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        // collectionProperty
        assertValueAndPointer("/integers", bean.getIntegers(), "/integers", "Bb", "BbC");
    }
}
