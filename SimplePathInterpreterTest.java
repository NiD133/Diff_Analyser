package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

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
 * Tests for SimplePathInterpreter.
 *
 * Notes about pointer signature codes used by these tests:
 *   B = BeanPointer
 *   b = BeanPropertyPointer
 *   C = CollectionPointer
 *   D = DynamicPointer
 *   d = DynamicPropertyPointer
 *   E = NullElementPointer
 *   M = DOMNodePointer
 *   N = NullPointer
 *   n = NullPropertyPointer
 *   V = VariablePointer
 *
 * The signature for a pointer is built by concatenating the codes for all pointers in the chain
 * from the root down to the current pointer. Value pointers get their own signature too.
 */
class SimplePathInterpreterTest {

    private static final String VENDOR_PATH = "/vendor";

    private TestBeanWithNode bean;
    private JXPathContext context;

    // ---------- Assertion helpers ----------

    /**
     * Assert that the given XPath resolves to an actual value and that the pointer chain (asPath and signature)
     * matches the expectations.
     */
    private void assertValueAndPointer(final String path,
                                       final Object expectedValue,
                                       final String expectedPath,
                                       final String expectedSignature) {
        assertValueAndPointer(path, expectedValue, expectedPath, expectedSignature, expectedSignature);
    }

    /**
     * Assert that the given XPath resolves to an actual value and that both the pointer chain signature and
     * the value pointer chain signature match the expectations.
     */
    private void assertValueAndPointer(final String path,
                                       final Object expectedValue,
                                       final String expectedPath,
                                       final String expectedSignature,
                                       final String expectedValueSignature) {
        final Object value = context.getValue(path);
        assertEquals(expectedValue, value, "Unexpected value for path: " + path);

        final Pointer pointer = context.getPointer(path);
        assertEquals(expectedPath, pointer.toString(), "Unexpected pointer.toString for path: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Unexpected pointer signature for path: " + path);

        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValueSignature, pointerSignature(valuePointer),
                "Unexpected value-pointer signature for path: " + path);
    }

    /**
     * Assert that the given XPath resolves to a "null pointer" (a resolvable path that does not point to an actual value).
     * Also verifies both pointer and value pointer signatures.
     */
    private void assertNullPointer(final String path,
                                   final String expectedPath,
                                   final String expectedSignature) {
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "Expected a non-null pointer for path (but a null pointer object): " + path);
        assertEquals(expectedPath, pointer.asPath(), "Unexpected pointer.asPath for path: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Unexpected pointer signature for path: " + path);

        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) valuePointer).isActual(), "Expected a non-actual value pointer for path: " + path);
        assertEquals(expectedSignature + "N", pointerSignature(valuePointer),
                "Unexpected value-pointer signature for path: " + path);
    }

    // ---------- Pointer signature utilities ----------

    /**
     * Builds a compact signature string for the given pointer by walking the parent chain and appending one character per pointer type.
     * See the legend at the top of this class for the meaning of each character.
     */
    private String pointerSignature(final Pointer pointer) {
        if (pointer == null) {
            return "";
        }

        final char typeCode = signatureCodeFor(pointer);
        final NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        return pointerSignature(parent) + typeCode;
    }

    private char signatureCodeFor(final Pointer pointer) {
        // The order of checks matters due to type hierarchies.
        if (pointer instanceof NullPointer) {
            return 'N';
        }
        if (pointer instanceof NullPropertyPointer) {
            return 'n';
        }
        if (pointer instanceof NullElementPointer) {
            return 'E';
        }
        if (pointer instanceof VariablePointer) {
            return 'V';
        }
        if (pointer instanceof CollectionPointer) {
            return 'C';
        }
        if (pointer instanceof BeanPointer) {
            return 'B';
        }
        if (pointer instanceof BeanPropertyPointer) {
            return 'b';
        }
        if (pointer instanceof DynamicPointer) {
            return 'D';
        }
        if (pointer instanceof DynamicPropertyPointer) {
            return 'd';
        }
        if (pointer instanceof DOMNodePointer) {
            return 'M';
        }
        throw new AssertionError("Unknown pointer type in test signature mapping: " + pointer.getClass().getName());
    }

    // ---------- Test fixture ----------

    @BeforeEach
    protected void setUp() throws Exception {
        bean = TestBeanWithNode.createTestBeanWithDOM();

        final Map<String, Object> submap = new HashMap<>();
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

    private Object vendor() {
        return context.getValue(VENDOR_PATH);
    }

    // ---------- Tests mapped to SimplePathInterpreter responsibilities ----------

    @Test
    @DisplayName("doPredicateIndex: collection indexing and null-element behavior")
    void testDoPredicateIndex() {
        // Existing dynamic property + existing property + index
        assertValueAndPointer("/map[@name='Key2'][@name='strings'][2]", "String 2", "/map[@name='Key2']/strings[2]", "BbDdBb", "BbDdBbB");

        // existingProperty[@name=collectionProperty][index]
        assertValueAndPointer("/nestedBean[@name='strings'][2]", bean.getNestedBean().getStrings()[1], "/nestedBean/strings[2]", "BbBb", "BbBbB");

        // existingProperty[@name=missingProperty][index]
        assertNullPointer("/nestedBean[@name='foo'][3]", "/nestedBean[@name='foo'][3]", "BbBn");

        // existingProperty[@name=collectionProperty][missingIndex]
        assertNullPointer("/nestedBean[@name='strings'][5]", "/nestedBean/strings[5]", "BbBbE");

        // map[@name=collectionProperty][index]
        assertValueAndPointer("/map[@name='Key3'][2]", Integer.valueOf(2), "/map[@name='Key3'][2]", "BbDd", "BbDdB");

        // map[@name=collectionProperty][missingIndex]
        assertNullPointer("/map[@name='Key3'][5]", "/map[@name='Key3'][5]", "BbDdE");

        // map[@name=collectionProperty][missingIndex]/property
        assertNullPointer("/map[@name='Key3'][5]/foo", "/map[@name='Key3'][5]/foo", "BbDdENn");

        // map[@name=map][@name=collection][index]
        assertValueAndPointer("/map[@name='Key5'][@name='strings'][2]", "String 2", "/map[@name='Key5'][@name='strings'][2]", "BbDdDd", "BbDdDdB");

        // map[@name=map][@name=collection][missingIndex]
        assertNullPointer("/map[@name='Key5'][@name='strings'][5]", "/map[@name='Key5'][@name='strings'][5]", "BbDdDdE");

        // Existing dynamic property + indexing (duplicate to enforce stability)
        assertValueAndPointer("/map[@name='Key3'][2]", Integer.valueOf(2), "/map[@name='Key3'][2]", "BbDd", "BbDdB");

        // Existing dynamic property + indexing + property
        assertValueAndPointer("/map[@name='Key3'][1]/name", "some", "/map[@name='Key3'][1]/name", "BbDdBb", "BbDdBbB");

        // map[@name=missingProperty][index]
        assertNullPointer("/map[@name='foo'][3]", "/map[@name='foo'][3]", "BbDdE");

        // collectionProperty[index]
        assertValueAndPointer("/integers[2]", Integer.valueOf(2), "/integers[2]", "Bb", "BbB");

        // existingProperty/collectionProperty[index]
        assertValueAndPointer("/nestedBean/strings[2]", bean.getNestedBean().getStrings()[1], "/nestedBean/strings[2]", "BbBb", "BbBbB");

        // existingProperty[index]/existingProperty
        assertValueAndPointer("/list[3]/int", Integer.valueOf(1), "/list[3]/int", "BbBb", "BbBbB");

        // existingProperty[missingIndex]
        assertNullPointer("/list[6]", "/list[6]", "BbE");

        // existingProperty/missingProperty[index]
        assertNullPointer("/nestedBean/foo[3]", "/nestedBean/foo[3]", "BbBn");

        // map[@name=missingProperty][index]
        assertNullPointer("/map/foo[3]", "/map[@name='foo'][3]", "BbDdE");

        // existingProperty/collectionProperty[missingIndex]
        assertNullPointer("/nestedBean/strings[5]", "/nestedBean/strings[5]", "BbBbE");

        // map/collectionProperty[missingIndex]/property
        assertNullPointer("/map/Key3[5]/foo", "/map[@name='Key3'][5]/foo", "BbDdENn");

        // map[@name=map]/collection[index]
        assertValueAndPointer("/map[@name='Key5']/strings[2]", "String 2", "/map[@name='Key5'][@name='strings'][2]", "BbDdDd", "BbDdDdB");

        // map[@name=map]/collection[missingIndex]
        assertNullPointer("/map[@name='Key5']/strings[5]", "/map[@name='Key5'][@name='strings'][5]", "BbDdDdE");

        // scalarPropertyAsCollection[index]
        assertValueAndPointer("/int[1]", Integer.valueOf(1), "/int", "Bb", "BbB");

        // scalarPropertyAsCollection[index] via self
        assertValueAndPointer(".[1]/int", Integer.valueOf(1), "/int", "Bb", "BbB");
    }

    @Test
    @DisplayName("doPredicateName: name-attribute predicates on beans, maps and DOM nodes")
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
        assertValueAndPointer("/list[@name='fruitco']", vendor(), "/list[5]", "BbCM");

        // collectionProperty[@name=...] (find map entry)
        assertValueAndPointer("/map/Key3[@name='key']/name", "Name 9", "/map[@name='Key3'][4][@name='key']/name", "BbDdCDdBb", "BbDdCDdBbB");

        // map/collectionProperty[@name...]
        assertValueAndPointer("map/Key3[@name='fruitco']", vendor(), "/map[@name='Key3'][3]", "BbDdCM");

        // Bean property -> DOM Node, name match
        assertValueAndPointer("/vendor[@name='fruitco']", vendor(), "/vendor", "BbM");

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
        assertValueAndPointer("map[@name='Key4'][@name='fruitco']", vendor(), "/map[@name='Key4']", "BbDdM");
    }

    @Test
    @DisplayName("doPredicatesStandard: chained predicates and indexing on DOM nodes and collections")
    void testDoPredicatesStandard() {
        // bean/map/collection/node
        assertValueAndPointer("map[@name='Key3'][@name='fruitco']", vendor(), "/map[@name='Key3'][3]", "BbDdCM");

        // bean/map/collection/missingNode
        assertNullPointer("map[@name='Key3'][@name='foo']", "/map[@name='Key3'][4][@name='foo']", "BbDdCDd");

        // bean/map/node
        assertValueAndPointer("map[@name='Key4'][@name='fruitco']", vendor(), "/map[@name='Key4']", "BbDdM");

        // bean/map/emptyCollection[@name=foo]
        assertNullPointer("map[@name='Key6'][@name='fruitco']", "/map[@name='Key6'][@name='fruitco']", "BbDdCn");

        // bean/node[@name=foo][index]
        assertValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");

        // bean/node[@name=foo][missingIndex]
        assertNullPointer("/vendor/contact[@name='jack'][5]", "/vendor/contact[@name='jack'][5]", "BbMnNn");

        // bean/node/.[@name=foo][index]
        assertValueAndPointer("/vendor/contact/.[@name='jack']", "Jack", "/vendor/contact[2]", "BbMM");
    }

    @Test
    @DisplayName("doStepNoPredicatesPropertyOwner: property-owner steps without predicates")
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

    @Test
    @DisplayName("doStepNoPredicatesStandard: DOM traversal without predicates")
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

    @Test
    @DisplayName("doStepPredicatesPropertyOwner: property-owner steps with predicates")
    void testDoStepPredicatesPropertyOwner() {
        // missingProperty[@name=foo]
        assertNullPointer("/foo[@name='foo']", "/foo[@name='foo']", "BnNn");

        // missingProperty[index]
        assertNullPointer("/foo[3]", "/foo[3]", "Bn");
    }

    @Test
    @DisplayName("doStepPredicatesStandard: DOM steps with predicates and indexing")
    void testDoStepPredicatesStandard() {
        // Looking for an actual XML attribute called "name"
        // nodeProperty/name[@name=value]
        assertValueAndPointer("/vendor/contact[@name='jack']", "Jack", "/vendor/contact[2]", "BbMM");

        // Indexing in XML
        assertValueAndPointer("/vendor/contact[2]", "Jack", "/vendor/contact[2]", "BbMM");

        // Indexing in XML, no result
        assertNullPointer("/vendor/contact[5]", "/vendor/contact[5]", "BbMn");

        // Combination of search by name and indexing in XML
        assertValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");

        // Duplicate of the previous case to ensure consistent evaluation
        assertValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");
    }

    @Test
    @DisplayName("interpretSimpleExpressionPath: variable root with predicates")
    void testInterpretExpressionPath() {
        context.getVariables().declareVariable("array", new String[] { "Value1" });
        context.getVariables().declareVariable("testnull", new TestNull());

        assertNullPointer("$testnull/nothing[2]", "$testnull/nothing[2]", "VBbE");
    }
}