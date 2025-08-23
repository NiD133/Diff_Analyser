package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for SimplePathInterpreter with [@name=...] predicates")
public class SimplePathInterpreterNamePredicateTest {

    private JXPathContext context;
    private TestBeanWithNode bean;

    // Fields to hold expected values for clearer assertions
    private Object vendor;
    private Object key3Value;
    private String[] nestedBeanStrings;

    @BeforeEach
    protected void setUp() {
        bean = TestBeanWithNode.createTestBeanWithDOM();
        vendor = bean.getVendor();
        nestedBeanStrings = bean.getNestedBean().getStrings();

        final Map<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", nestedBeanStrings);

        bean.getList().add(new int[] { 1, 2 });
        bean.getList().add(vendor);

        key3Value = new Object[] { new NestedTestBean("some"), 2, vendor, submap };

        bean.getMap().put("Key3", key3Value);
        bean.getMap().put("Key4", vendor);
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);

        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    // --- Refactored Assertion Helpers ---

    private void assertValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final List<String> expectedPointerSignature) {
        assertValueAndPointer(path, expectedValue, expectedPath, expectedPointerSignature, expectedPointerSignature);
    }

    private void assertValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final List<String> expectedPointerSignature, final List<String> expectedValueSignature) {
        final Object value = context.getValue(path);
        assertEquals(expectedValue, value, "Value check failed for path: " + path);

        final Pointer pointer = context.getPointer(path);
        assertEquals(expectedPath, pointer.toString(), "Pointer path check failed for: " + path);
        assertEquals(expectedPointerSignature, getPointerSignature(pointer), "Pointer signature check failed for: " + path);

        final Pointer vPointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValueSignature, getPointerSignature(vPointer), "Value pointer signature check failed for: " + path);
    }

    private void assertNullPointer(final String path, final String expectedPath, final List<String> expectedSignature) {
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "Pointer should not be null for path: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Null pointer path check failed for: " + path);
        assertEquals(expectedSignature, getPointerSignature(pointer), "Null pointer signature check failed for: " + path);

        final Pointer vPointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) vPointer).isActual(), "Value pointer for a null path should not be 'actual' for: " + path);

        final List<String> expectedValueSignature = new ArrayList<>(expectedSignature);
        expectedValueSignature.add("Null");
        assertEquals(expectedValueSignature, getPointerSignature(vPointer), "Value pointer signature for null path check failed for: " + path);
    }

    /**
     * Generates a signature for a pointer chain, representing each pointer's type with a descriptive name.
     * This makes tests easier to read and debug than using cryptic character codes.
     * @param pointer The end pointer of the chain.
     * @return A list of strings representing the types of pointers in the chain from root to tip.
     */
    private List<String> getPointerSignature(final Pointer pointer) {
        if (pointer == null) {
            return new ArrayList<>();
        }
        final NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        final List<String> signature = getPointerSignature(parent);
        signature.add(getPointerTypeName(pointer));
        return signature;
    }

    /**
     * Returns a descriptive name for a given Pointer's type.
     */
    private String getPointerTypeName(final Pointer pointer) {
        if (pointer instanceof NullPointer) return "Null";
        if (pointer instanceof NullPropertyPointer) return "NullProperty";
        if (pointer instanceof NullElementPointer) return "NullElement";
        if (pointer instanceof VariablePointer) return "Variable";
        if (pointer instanceof CollectionPointer) return "Collection";
        if (pointer instanceof BeanPointer) return "Bean";
        if (pointer instanceof BeanPropertyPointer) return "BeanProperty";
        if (pointer instanceof DynamicPointer) return "Dynamic";
        if (pointer instanceof DynamicPropertyPointer) return "DynamicProperty";
        if (pointer instanceof DOMNodePointer) return "DOMNode";
        return "Unknown:" + pointer.getClass().getSimpleName();
    }

    // --- Structured Test Cases ---

    @Nested
    @DisplayName("on Property Owners (Beans, Maps)")
    class PropertyOwnerTests {
        @Test
        @DisplayName("selects an existing bean property: /nestedBean[@name='int']")
        void predicateSelectsExistingBeanProperty() {
            assertValueAndPointer("/nestedBean[@name='int']", 1, "/nestedBean/int",
                List.of("Bean", "BeanProperty", "Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Bean", "BeanProperty", "Bean"));
        }

        @Test
        @DisplayName("selects a property on self node: /.[@name='int']")
        void predicateSelectsPropertyOnSelf() {
            assertValueAndPointer("/.[@name='int']", 1, "/int",
                List.of("Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Bean"));
        }

        @Test
        @DisplayName("selects an existing dynamic property: /map[@name='Key1']")
        void predicateSelectsExistingDynamicProperty() {
            assertValueAndPointer("/map[@name='Key1']", "Value 1", "/map[@name='Key1']",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty"),
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean"));
        }

        @Test
        @DisplayName("selects a collection-valued bean property: /nestedBean[@name='strings']")
        void predicateSelectsCollectionValuedBeanProperty() {
            assertValueAndPointer("/nestedBean[@name='strings']", nestedBeanStrings, "/nestedBean/strings",
                List.of("Bean", "BeanProperty", "Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Bean", "BeanProperty", "Collection"));
        }

        @Test
        @DisplayName("selects a collection-valued dynamic property: /map[@name='Key3']")
        void predicateSelectsCollectionValuedDynamicProperty() {
            assertValueAndPointer("/map[@name='Key3']", key3Value, "/map[@name='Key3']",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty"),
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Collection"));
        }

        @Test
        @DisplayName("selects a DOM node property: /vendor[@name='fruitco']")
        void predicateSelectsDomNodeProperty() {
            assertValueAndPointer("/vendor[@name='fruitco']", vendor, "/vendor",
                List.of("Bean", "BeanProperty", "DOMNode"));
        }
    }

    @Nested
    @DisplayName("on Collections and their Elements")
    class CollectionTests {
        @Test
        @DisplayName("finds a DOM node in a collection: /list[@name='fruitco']")
        void predicateFindsDomNodeInCollection() {
            assertValueAndPointer("/list[@name='fruitco']", vendor, "/list[5]",
                List.of("Bean", "BeanProperty", "Collection", "DOMNode"));
        }

        @Test
        @DisplayName("finds a map entry in a collection: /map/Key3[@name='key']/name")
        void predicateFindsMapInCollection() {
            assertValueAndPointer("/map/Key3[@name='key']/name", "Name 9", "/map[@name='Key3'][4][@name='key']/name",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Collection", "Dynamic", "DynamicProperty", "Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Collection", "Dynamic", "DynamicProperty", "Bean", "BeanProperty", "Bean"));
        }

        @Test
        @DisplayName("finds an element in a collection property of a map: map/Key3[@name='fruitco']")
        void predicateFindsElementInMapCollectionProperty() {
            assertValueAndPointer("map/Key3[@name='fruitco']", vendor, "/map[@name='Key3'][3]",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Collection", "DOMNode"));
        }
    }

    @Nested
    @DisplayName("with Chained Predicates")
    class ChainedPredicateTests {
        @Test
        @DisplayName("on existing dynamic property and existing bean property: /map[@name='Key2'][@name='name']")
        void chainedPredicatesOnDynamicAndBeanProperties() {
            assertValueAndPointer("/map[@name='Key2'][@name='name']", "Name 6", "/map[@name='Key2']/name",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean", "BeanProperty", "Bean"));
        }

        @Test
        @DisplayName("on nested maps: map[@name='Key5'][@name='key']/name")
        void chainedPredicatesOnNestedMaps() {
            assertValueAndPointer("map[@name='Key5'][@name='key']/name", "Name 9", "/map[@name='Key5'][@name='key']/name",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Dynamic", "DynamicProperty", "Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Dynamic", "DynamicProperty", "Bean", "BeanProperty", "Bean"));
        }

        @Test
        @DisplayName("on a map and a DOM node: map[@name='Key4'][@name='fruitco']")
        void chainedPredicatesOnMapAndDomNode() {
            assertValueAndPointer("map[@name='Key4'][@name='fruitco']", vendor, "/map[@name='Key4']",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "DOMNode"));
        }

        @Test
        @DisplayName("with an index: /map[@name='Key2'][@name='strings'][2]")
        void chainedPredicatesWithIndex() {
            assertValueAndPointer("/map[@name='Key2'][@name='strings'][2]", "String 2", "/map[@name='Key2']/strings[2]",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean", "BeanProperty"),
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean", "BeanProperty", "Bean"));
        }
    }

    @Nested
    @DisplayName("for Missing Properties (NullPointer results)")
    class NullResultTests {
        @Test
        @DisplayName("on a missing bean property: /nestedBean[@name='foo']")
        void missingBeanProperty() {
            assertNullPointer("/nestedBean[@name='foo']", "/nestedBean[@name='foo']",
                List.of("Bean", "BeanProperty", "Bean", "NullProperty"));
        }

        @Test
        @DisplayName("on a missing dynamic property: /map[@name='foo']")
        void missingDynamicProperty() {
            assertNullPointer("/map[@name='foo']", "/map[@name='foo']",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty"));
        }

        @Test
        @DisplayName("on a DOM node with a mismatched name: /vendor[@name='foo']")
        void mismatchedDomNodeName() {
            assertNullPointer("/vendor[@name='foo']", "/vendor[@name='foo']",
                List.of("Bean", "BeanProperty", "DOMNode", "NullProperty"));
        }

        @Test
        @DisplayName("followed by another path segment: /nestedBean[@name='foo']/bar")
        void missingPropertyFollowedByAnotherSegment() {
            assertNullPointer("/nestedBean[@name='foo']/bar", "/nestedBean[@name='foo']/bar",
                List.of("Bean", "BeanProperty", "Bean", "NullProperty", "Null", "NullProperty"));
        }

        @Test
        @DisplayName("on a valid property followed by a missing one: map[@name='Key2'][@name='foo']")
        void validPropertyFollowedByMissingProperty() {
            assertNullPointer("map[@name='Key2'][@name='foo']", "/map[@name='Key2'][@name='foo']",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean", "NullProperty"));
        }

        @Test
        @DisplayName("with multiple missing properties: map[@name='Key2'][@name='foo'][@name='bar']")
        void multipleMissingProperties() {
            assertNullPointer("map[@name='Key2'][@name='foo'][@name='bar']", "/map[@name='Key2'][@name='foo'][@name='bar']",
                List.of("Bean", "BeanProperty", "Dynamic", "DynamicProperty", "Bean", "NullProperty", "Null", "NullProperty"));
        }
    }
}