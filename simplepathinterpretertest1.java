package org.apache.commons.jxpath.ri.axes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.DynamicPointer;
import org.apache.commons.jxpath.ri.model.beans.DynamicPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.NullElementPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.TestBeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests JXPath evaluation involving indexed predicates, such as '[2]' or '[@name=...][2]'.
 * This focuses on how SimplePathInterpreter handles these paths.
 */
@DisplayName("XPath Evaluation with Indexed Predicates")
public class SimplePathInterpreterTestTest1 {

    private JXPathContext context;
    private TestBeanWithNode bean;

    @BeforeEach
    void setUp() {
        bean = TestBeanWithNode.createTestBeanWithDOM();
        final HashMap<String, Object> submap = new HashMap<>();
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

    /**
     * Asserts that an XPath resolves to a specific value and that the resulting
     * pointer chain has the expected structure.
     *
     * @param path                      The XPath to evaluate.
     * @param expectedValue             The expected value from context.getValue(path).
     * @param expectedPointerPath       The expected canonical path from pointer.asPath().
     * @param expectedPointerChain      A description of the types in the pointer's hierarchy.
     * @param expectedValuePointerChain A description of the types for the pointer to the actual value.
     */
    private void assertPathEvaluatesTo(
            final String path,
            final Object expectedValue,
            final String expectedPointerPath,
            final String expectedPointerChain,
            final String expectedValuePointerChain) {

        assertEquals(expectedValue, context.getValue(path), "Value for path: " + path);

        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "Pointer should not be null for path: " + path);
        assertEquals(expectedPointerPath, pointer.asPath(), "Pointer path for: " + path);

        assertEquals(expectedPointerChain, getPointerChainDescription(pointer), "Pointer chain for: " + path);

        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValuePointerChain, getPointerChainDescription(valuePointer), "Value pointer chain for: " + path);
    }

    /**
     * Asserts that an XPath resolves to a "null pointer" for a non-existent node.
     * In lenient mode, JXPath creates special pointers (e.g., NullPropertyPointer)
     * to represent paths that do not map to an actual object.
     *
     * @param path                 The XPath to evaluate.
     * @param expectedPointerPath  The expected canonical path from pointer.asPath().
     * @param expectedPointerChain A description of the types in the null pointer's hierarchy.
     */
    private void assertPathResolvesToNullPointer(
            final String path,
            final String expectedPointerPath,
            final String expectedPointerChain) {

        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "A 'null pointer' object should exist for path: " + path);
        assertEquals(expectedPointerPath, pointer.asPath(), "Null pointer path for: " + path);

        assertEquals(expectedPointerChain, getPointerChainDescription(pointer), "Null pointer chain for: " + path);

        final NodePointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(valuePointer.isActual(), "Value pointer for a null path should not be 'actual': " + path);

        // The value of a NullPropertyPointer or NullElementPointer is a NullPointer.
        String expectedValuePointerChain = expectedPointerChain + " -> NullPointer";
        assertEquals(expectedValuePointerChain, getPointerChainDescription(valuePointer), "Value pointer chain for null path: " + path);
    }

    /**
     * Generates a human-readable string representing the chain of pointer types
     * from the root to the given pointer. Replaces the cryptic single-character
     * signature from the original test.
     *
     * @param pointer The final pointer in the chain.
     * @return A string like "BeanPointer -> BeanPropertyPointer -> DynamicPointer".
     */
    private String getPointerChainDescription(final Pointer pointer) {
        if (pointer == null) {
            return "null";
        }
        final List<String> classNames = new ArrayList<>();
        NodePointer current = (NodePointer) pointer;
        while (current != null) {
            classNames.add(current.getClass().getSimpleName());
            current = current.getImmediateParentPointer();
        }
        Collections.reverse(classNames);
        return String.join(" -> ", classNames);
    }

    @Nested
    @DisplayName("Tests for existing properties and elements")
    class ExistingPathsTests {

        @Test
        void testAccessBeanPropertyCollectionByIndex() {
            assertPathEvaluatesTo(
                    "/nestedBean/strings[2]",
                    bean.getNestedBean().getStrings()[1],
                    "/nestedBean/strings[2]",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer -> BeanPointer");
        }

        @Test
        void testAccessBeanPropertyCollectionWithPredicateAndIndex() {
            assertPathEvaluatesTo(
                    "/nestedBean[@name='strings'][2]",
                    bean.getNestedBean().getStrings()[1],
                    "/nestedBean/strings[2]",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer -> BeanPointer");
        }

        @Test
        void testAccessMapEntryCollectionByIndex() {
            assertPathEvaluatesTo(
                    "/map[@name='Key3'][2]",
                    2,
                    "/map[@name='Key3'][2]",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> BeanPointer");
        }

        @Test
        void testAccessPropertyOfIndexedElementInMap() {
            assertPathEvaluatesTo(
                    "/map[@name='Key3'][1]/name",
                    "some",
                    "/map[@name='Key3'][1]/name",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> BeanPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> BeanPropertyPointer -> BeanPointer");
        }

        @Test
        void testChainedMapAndCollectionAccessWithPredicates() {
            assertPathEvaluatesTo(
                    "/map[@name='Key5'][@name='strings'][2]",
                    "String 2",
                    "/map[@name='Key5']/strings[2]",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> DynamicPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> DynamicPropertyPointer -> BeanPointer");
        }

        @Test
        void testAccessPropertyOfIndexedElementInList() {
            assertPathEvaluatesTo(
                    "/list[3]/int",
                    1,
                    "/list[3]/int",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer -> BeanPointer");
        }

        @Test
        void testAccessScalarPropertyAsCollection() {
            // A scalar property can be accessed with index [1] as if it were a single-element collection.
            assertPathEvaluatesTo(
                    "/int[1]",
                    1,
                    "/int",
                    "BeanPointer -> BeanPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> BeanPointer");

            assertPathEvaluatesTo(
                    ".[1]/int",
                    1,
                    "/int",
                    "BeanPointer -> BeanPropertyPointer",
                    "BeanPointer -> BeanPropertyPointer -> BeanPointer");
        }
    }

    @Nested
    @DisplayName("Tests for non-existent properties and out-of-bounds indices")
    class NonExistentPathsTests {

        @Test
        void testAccessMissingBeanPropertyWithPredicateAndIndex() {
            assertPathResolvesToNullPointer(
                    "/nestedBean[@name='foo'][3]",
                    "/nestedBean[@name='foo'][3]",
                    "BeanPointer -> BeanPropertyPointer -> NullPropertyPointer");
        }

        @Test
        void testAccessBeanPropertyCollectionWithOutOfBoundsIndex() {
            assertPathResolvesToNullPointer(
                    "/nestedBean/strings[5]",
                    "/nestedBean/strings[5]",
                    "BeanPointer -> BeanPropertyPointer -> BeanPropertyPointer -> NullElementPointer");
        }

        @Test
        void testAccessMapEntryCollectionWithOutOfBoundsIndex() {
            assertPathResolvesToNullPointer(
                    "/map[@name='Key3'][5]",
                    "/map[@name='Key3'][5]",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> NullElementPointer");
        }

        @Test
        void testAccessPropertyOfElementWithOutOfBoundsIndex() {
            assertPathResolvesToNullPointer(
                    "/map[@name='Key3'][5]/foo",
                    "/map[@name='Key3'][5]/foo",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> NullElementPointer -> NullPropertyPointer");
        }

        @Test
        void testAccessMissingMapEntryWithIndex() {
            assertPathResolvesToNullPointer(
                    "/map[@name='foo'][3]",
                    "/map[@name='foo'][3]",
                    "BeanPointer -> BeanPropertyPointer -> DynamicPropertyPointer -> NullElementPointer");
        }

        @Test
        void testAccessListWithOutOfBoundsIndex() {
            assertPathResolvesToNullPointer(
                    "/list[6]",
                    "/list[6]",
                    "BeanPointer -> BeanPropertyPointer -> NullElementPointer");
        }
    }
}