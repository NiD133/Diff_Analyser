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

public class SimplePathInterpreterTestTest2 {

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
    void testDoPredicateName() {
        // existingProperty[@name=existingProperty]
        assertValueAndPointer("/nestedBean[@name='int']", Integer.valueOf(1), "/nestedBean/int", "BbBb", "BbBbB");
        // /self::node()[@name=existingProperty]
        assertValueAndPointer("/.[@name='int']", Integer.valueOf(1), "/int", "Bb", "BbB");
        // dynamicProperty[@name=existingProperty]
        assertValueAndPointer("/map[@name='Key1']", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        // existingProperty[@name=collectionProperty]
        assertValueAndPointer("/nestedBean[@name='strings']", bean.getNestedBean().getStrings(), "/nestedBean/strings", "BbBb", "BbBbC");
        // existingProperty[@name=missingProperty]
        assertNullPointer("/nestedBean[@name='foo']", "/nestedBean[@name='foo']", "BbBn");
        // map[@name=collectionProperty]
        assertValueAndPointer("/map[@name='Key3']", bean.getMap().get("Key3"), "/map[@name='Key3']", "BbDd", "BbDdC");
        // map[@name=missingProperty]
        assertNullPointer("/map[@name='foo']", "/map[@name='foo']", "BbDd");
        // collectionProperty[@name=...] (find node)
        assertValueAndPointer("/list[@name='fruitco']", context.getValue("/vendor"), "/list[5]", "BbCM");
        // collectionProperty[@name=...] (find map entry)
        assertValueAndPointer("/map/Key3[@name='key']/name", "Name 9", "/map[@name='Key3'][4][@name='key']/name", "BbDdCDdBb", "BbDdCDdBbB");
        // map/collectionProperty[@name...]
        assertValueAndPointer("map/Key3[@name='fruitco']", context.getValue("/vendor"), "/map[@name='Key3'][3]", "BbDdCM");
        // Bean property -> DOM Node, name match
        assertValueAndPointer("/vendor[@name='fruitco']", context.getValue("/vendor"), "/vendor", "BbM");
        // Bean property -> DOM Node, name mismatch
        assertNullPointer("/vendor[@name='foo']", "/vendor[@name='foo']", "BbMn");
        assertNullPointer("/vendor[@name='foo'][3]", "/vendor[@name='foo'][3]", "BbMn");
        // existingProperty(bean)[@name=missingProperty]/anotherStep
        assertNullPointer("/nestedBean[@name='foo']/bar", "/nestedBean[@name='foo']/bar", "BbBnNn");
        // map[@name=missingProperty]/anotherStep
        assertNullPointer("/map[@name='foo']/bar", "/map[@name='foo']/bar", "BbDdNn");
        // existingProperty(node)[@name=missingProperty]/anotherStep
        assertNullPointer("/vendor[@name='foo']/bar", "/vendor[@name='foo']/bar", "BbMnNn");
        // existingProperty(node)[@name=missingProperty][index]/anotherStep
        assertNullPointer("/vendor[@name='foo'][3]/bar", "/vendor[@name='foo'][3]/bar", "BbMnNn");
        // Existing dynamic property + existing property
        assertValueAndPointer("/map[@name='Key2'][@name='name']", "Name 6", "/map[@name='Key2']/name", "BbDdBb", "BbDdBbB");
        // Existing dynamic property + existing property + index
        assertValueAndPointer("/map[@name='Key2'][@name='strings'][2]", "String 2", "/map[@name='Key2']/strings[2]", "BbDdBb", "BbDdBbB");
        // bean/map/map/property
        assertValueAndPointer("map[@name='Key5'][@name='key']/name", "Name 9", "/map[@name='Key5'][@name='key']/name", "BbDdDdBb", "BbDdDdBbB");
        assertNullPointer("map[@name='Key2'][@name='foo']", "/map[@name='Key2'][@name='foo']", "BbDdBn");
        assertNullPointer("map[@name='Key2'][@name='foo'][@name='bar']", "/map[@name='Key2'][@name='foo'][@name='bar']", "BbDdBnNn");
        // bean/map/node
        assertValueAndPointer("map[@name='Key4'][@name='fruitco']", context.getValue("/vendor"), "/map[@name='Key4']", "BbDdM");
    }
}
