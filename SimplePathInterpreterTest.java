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

/**
 * Unit tests for the SimplePathInterpreter class.
 */
class SimplePathInterpreterTest {

    private TestBeanWithNode testBean;
    private JXPathContext jxPathContext;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        testBean = TestBeanWithNode.createTestBeanWithDOM();
        HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", testBean.getNestedBean().getStrings());
        testBean.getList().add(new int[]{1, 2});
        testBean.getList().add(testBean.getVendor());
        testBean.getMap().put("Key3", new Object[]{new NestedTestBean("some"), Integer.valueOf(2), testBean.getVendor(), submap});
        testBean.getMap().put("Key4", testBean.getVendor());
        testBean.getMap().put("Key5", submap);
        testBean.getMap().put("Key6", new Object[0]);
        jxPathContext = JXPathContext.newContext(null, testBean);
        jxPathContext.setLenient(true);
        jxPathContext.setFactory(new TestBeanFactory());
    }

    /**
     * Tests the interpretation of paths with predicates involving indices.
     */
    @Test
    void testPredicateIndex() {
        assertValueAndPointer("/map[@name='Key2'][@name='strings'][2]", "String 2", "/map[@name='Key2']/strings[2]", "BbDdBb", "BbDdBbB");
        assertValueAndPointer("/nestedBean[@name='strings'][2]", testBean.getNestedBean().getStrings()[1], "/nestedBean/strings[2]", "BbBb", "BbBbB");
        assertNullPointer("/nestedBean[@name='foo'][3]", "/nestedBean[@name='foo'][3]", "BbBn");
        assertNullPointer("/nestedBean[@name='strings'][5]", "/nestedBean/strings[5]", "BbBbE");
        assertValueAndPointer("/map[@name='Key3'][2]", Integer.valueOf(2), "/map[@name='Key3'][2]", "BbDd", "BbDdB");
        assertNullPointer("/map[@name='Key3'][5]", "/map[@name='Key3'][5]", "BbDdE");
        assertNullPointer("/map[@name='Key3'][5]/foo", "/map[@name='Key3'][5]/foo", "BbDdENn");
        assertValueAndPointer("/map[@name='Key5'][@name='strings'][2]", "String 2", "/map[@name='Key5'][@name='strings'][2]", "BbDdDd", "BbDdDdB");
        assertNullPointer("/map[@name='Key5'][@name='strings'][5]", "/map[@name='Key5'][@name='strings'][5]", "BbDdDdE");
        assertValueAndPointer("/map[@name='Key3'][2]", Integer.valueOf(2), "/map[@name='Key3'][2]", "BbDd", "BbDdB");
        assertValueAndPointer("/map[@name='Key3'][1]/name", "some", "/map[@name='Key3'][1]/name", "BbDdBb", "BbDdBbB");
        assertNullPointer("/map[@name='foo'][3]", "/map[@name='foo'][3]", "BbDdE");
        assertValueAndPointer("/integers[2]", Integer.valueOf(2), "/integers[2]", "Bb", "BbB");
        assertValueAndPointer("/nestedBean/strings[2]", testBean.getNestedBean().getStrings()[1], "/nestedBean/strings[2]", "BbBb", "BbBbB");
        assertValueAndPointer("/list[3]/int", Integer.valueOf(1), "/list[3]/int", "BbBb", "BbBbB");
        assertNullPointer("/list[6]", "/list[6]", "BbE");
        assertNullPointer("/nestedBean/foo[3]", "/nestedBean/foo[3]", "BbBn");
        assertNullPointer("/map/foo[3]", "/map[@name='foo'][3]", "BbDdE");
        assertNullPointer("/nestedBean/strings[5]", "/nestedBean/strings[5]", "BbBbE");
        assertNullPointer("/map/Key3[5]/foo", "/map[@name='Key3'][5]/foo", "BbDdENn");
        assertValueAndPointer("/map[@name='Key5']/strings[2]", "String 2", "/map[@name='Key5'][@name='strings'][2]", "BbDdDd", "BbDdDdB");
        assertNullPointer("/map[@name='Key5']/strings[5]", "/map[@name='Key5'][@name='strings'][5]", "BbDdDdE");
        assertValueAndPointer("/int[1]", Integer.valueOf(1), "/int", "Bb", "BbB");
        assertValueAndPointer(".[1]/int", Integer.valueOf(1), "/int", "Bb", "BbB");
    }

    /**
     * Tests the interpretation of paths with predicates involving names.
     */
    @Test
    void testPredicateName() {
        assertValueAndPointer("/nestedBean[@name='int']", Integer.valueOf(1), "/nestedBean/int", "BbBb", "BbBbB");
        assertValueAndPointer("/.[@name='int']", Integer.valueOf(1), "/int", "Bb", "BbB");
        assertValueAndPointer("/map[@name='Key1']", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        assertValueAndPointer("/nestedBean[@name='strings']", testBean.getNestedBean().getStrings(), "/nestedBean/strings", "BbBb", "BbBbC");
        assertNullPointer("/nestedBean[@name='foo']", "/nestedBean[@name='foo']", "BbBn");
        assertValueAndPointer("/map[@name='Key3']", testBean.getMap().get("Key3"), "/map[@name='Key3']", "BbDd", "BbDdC");
        assertNullPointer("/map[@name='foo']", "/map[@name='foo']", "BbDd");
        assertValueAndPointer("/list[@name='fruitco']", jxPathContext.getValue("/vendor"), "/list[5]", "BbCM");
        assertValueAndPointer("/map/Key3[@name='key']/name", "Name 9", "/map[@name='Key3'][4][@name='key']/name", "BbDdCDdBb", "BbDdCDdBbB");
        assertValueAndPointer("map/Key3[@name='fruitco']", jxPathContext.getValue("/vendor"), "/map[@name='Key3'][3]", "BbDdCM");
        assertValueAndPointer("/vendor[@name='fruitco']", jxPathContext.getValue("/vendor"), "/vendor", "BbM");
        assertNullPointer("/vendor[@name='foo']", "/vendor[@name='foo']", "BbMn");
        assertNullPointer("/vendor[@name='foo'][3]", "/vendor[@name='foo'][3]", "BbMn");
        assertNullPointer("/nestedBean[@name='foo']/bar", "/nestedBean[@name='foo']/bar", "BbBnNn");
        assertNullPointer("/map[@name='foo']/bar", "/map[@name='foo']/bar", "BbDdNn");
        assertNullPointer("/vendor[@name='foo']/bar", "/vendor[@name='foo']/bar", "BbMnNn");
        assertNullPointer("/vendor[@name='foo'][3]/bar", "/vendor[@name='foo'][3]/bar", "BbMnNn");
        assertValueAndPointer("/map[@name='Key2'][@name='name']", "Name 6", "/map[@name='Key2']/name", "BbDdBb", "BbDdBbB");
        assertValueAndPointer("/map[@name='Key2'][@name='strings'][2]", "String 2", "/map[@name='Key2']/strings[2]", "BbDdBb", "BbDdBbB");
        assertValueAndPointer("map[@name='Key5'][@name='key']/name", "Name 9", "/map[@name='Key5'][@name='key']/name", "BbDdDdBb", "BbDdDdBbB");
        assertNullPointer("map[@name='Key2'][@name='foo']", "/map[@name='Key2'][@name='foo']", "BbDdBn");
        assertNullPointer("map[@name='Key2'][@name='foo'][@name='bar']", "/map[@name='Key2'][@name='foo'][@name='bar']", "BbDdBnNn");
        assertValueAndPointer("map[@name='Key4'][@name='fruitco']", jxPathContext.getValue("/vendor"), "/map[@name='Key4']", "BbDdM");
    }

    /**
     * Tests the interpretation of paths with standard predicates.
     */
    @Test
    void testPredicatesStandard() {
        assertValueAndPointer("map[@name='Key3'][@name='fruitco']", jxPathContext.getValue("/vendor"), "/map[@name='Key3'][3]", "BbDdCM");
        assertNullPointer("map[@name='Key3'][@name='foo']", "/map[@name='Key3'][4][@name='foo']", "BbDdCDd");
        assertValueAndPointer("map[@name='Key4'][@name='fruitco']", jxPathContext.getValue("/vendor"), "/map[@name='Key4']", "BbDdM");
        assertNullPointer("map[@name='Key6'][@name='fruitco']", "/map[@name='Key6'][@name='fruitco']", "BbDdCn");
        assertValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");
        assertNullPointer("/vendor/contact[@name='jack'][5]", "/vendor/contact[@name='jack'][5]", "BbMnNn");
        assertValueAndPointer("/vendor/contact/.[@name='jack']", "Jack", "/vendor/contact[2]", "BbMM");
    }

    /**
     * Tests the interpretation of steps with no predicates for property owners.
     */
    @Test
    void testStepNoPredicatesPropertyOwner() {
        assertValueAndPointer("/int", Integer.valueOf(1), "/int", "Bb", "BbB");
        assertValueAndPointer("/./int", Integer.valueOf(1), "/int", "Bb", "BbB");
        assertNullPointer("/foo", "/foo", "Bn");
        assertValueAndPointer("/nestedBean/int", Integer.valueOf(1), "/nestedBean/int", "BbBb", "BbBbB");
        assertValueAndPointer("/nestedBean/strings", testBean.getNestedBean().getStrings(), "/nestedBean/strings", "BbBb", "BbBbC");
        assertNullPointer("/nestedBean/foo", "/nestedBean/foo", "BbBn");
        assertNullPointer("/map/foo", "/map[@name='foo']", "BbDd");
        assertValueAndPointer("/list/int", Integer.valueOf(1), "/list[3]/int", "BbBb", "BbBbB");
        assertNullPointer("/list/foo", "/list[1]/foo", "BbBn");
        assertNullPointer("/nestedBean/foo/bar", "/nestedBean/foo/bar", "BbBnNn");
        assertNullPointer("/list/int/bar", "/list[3]/int/bar", "BbBbBn");
        assertNullPointer("/list/foo/bar", "/list[1]/foo/bar", "BbBnNn");
        assertNullPointer("/map/foo/bar", "/map[@name='foo']/bar", "BbDdNn");
        assertValueAndPointer("/map/Key1", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        assertValueAndPointer("/integers", testBean.getIntegers(), "/integers", "Bb", "BbC");
    }

    /**
     * Tests the interpretation of steps with no predicates for standard nodes.
     */
    @Test
    void testStepNoPredicatesStandard() {
        assertValueAndPointer("/vendor/location/address/city", "Fruit Market", "/vendor/location[2]/address[1]/city[1]", "BbMMMM");
        assertNullPointer("/vendor/location/address/pity", "/vendor/location[1]/address[1]/pity", "BbMMMn");
        assertNullPointer("/vendor/location/address/itty/bitty", "/vendor/location[1]/address[1]/itty/bitty", "BbMMMnNn");
        assertNullPointer("/vendor/location/address/city/pretty", "/vendor/location[2]/address[1]/city[1]/pretty", "BbMMMMn");
    }

    /**
     * Tests the interpretation of steps with predicates for property owners.
     */
    @Test
    void testStepPredicatesPropertyOwner() {
        assertNullPointer("/foo[@name='foo']", "/foo[@name='foo']", "BnNn");
        assertNullPointer("/foo[3]", "/foo[3]", "Bn");
    }

    /**
     * Tests the interpretation of steps with predicates for standard nodes.
     */
    @Test
    void testStepPredicatesStandard() {
        assertValueAndPointer("/vendor/contact[@name='jack']", "Jack", "/vendor/contact[2]", "BbMM");
        assertValueAndPointer("/vendor/contact[2]", "Jack", "/vendor/contact[2]", "BbMM");
        assertNullPointer("/vendor/contact[5]", "/vendor/contact[5]", "BbMn");
        assertValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");
        assertValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");
    }

    /**
     * Tests the interpretation of expression paths.
     */
    @Test
    void testInterpretExpressionPath() {
        jxPathContext.getVariables().declareVariable("array", new String[]{"Value1"});
        jxPathContext.getVariables().declareVariable("testnull", new TestNull());
        assertNullPointer("$testnull/nothing[2]", "$testnull/nothing[2]", "VBbE");
    }

    /**
     * Asserts that the pointer for a given path is null.
     */
    private void assertNullPointer(String path, String expectedPath, String expectedSignature) {
        Pointer pointer = jxPathContext.getPointer(path);
        assertNotNull(pointer, "Null path exists: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Null path as path: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Checking Signature: " + path);
        Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) valuePointer).isActual(), "Null path is null: " + path);
        assertEquals(expectedSignature + "N", pointerSignature(valuePointer), "Checking value pointer signature: " + path);
    }

    /**
     * Asserts the value and pointer for a given path.
     */
    private void assertValueAndPointer(String path, Object expectedValue, String expectedPath, String expectedSignature) {
        assertValueAndPointer(path, expectedValue, expectedPath, expectedSignature, expectedSignature);
    }

    /**
     * Asserts the value and pointer for a given path with expected value signature.
     */
    private void assertValueAndPointer(String path, Object expectedValue, String expectedPath, String expectedSignature, String expectedValueSignature) {
        Object value = jxPathContext.getValue(path);
        assertEquals(expectedValue, value, "Checking value: " + path);
        Pointer pointer = jxPathContext.getPointer(path);
        assertEquals(expectedPath, pointer.toString(), "Checking pointer: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Checking signature: " + path);
        Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValueSignature, pointerSignature(valuePointer), "Checking value pointer signature: " + path);
    }

    /**
     * Generates a signature string for a pointer chain.
     */
    private String pointerSignature(Pointer pointer) {
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
        NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        return pointerSignature(parent) + type;
    }
}