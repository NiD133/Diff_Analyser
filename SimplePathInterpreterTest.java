/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the {@link SimplePathInterpreter}, focusing on how it handles
 * various XPath constructs against a complex object graph including beans, collections, maps, and DOM nodes.
 *
 * <h3>Pointer Signatures</h3>
 * These tests verify the internal structure of the pointer chain created by JXPath using a "signature" string.
 * This is a low-level check to ensure the interpreter builds the correct sequence of pointer types, especially
 * for paths to non-existent nodes.
 *
 * <p><b>Signature Codes:</b></p>
 * <ul>
 *   <li>'V': {@link VariablePointer} - A variable like {@code $foo}.</li>
 *   <li>'B': {@link BeanPointer} - A pointer to a Java Bean.</li>
 *   <li>'b': {@link BeanPropertyPointer} - A pointer to a property of a bean.</li>
 *   <li>'C': {@link CollectionPointer} - A pointer to a collection.</li>
 *   <li>'D': {@link DynamicPointer} - A pointer to a dynamic object (like a Map).</li>
 *   <li>'d': {@link DynamicPropertyPointer} - A pointer to a property of a dynamic object.</li>
 *   <li>'M': {@link DOMNodePointer} - A pointer to a DOM node.</li>
 *   <li>'N': {@link NullPointer} - A pointer representing a null node.</li>
 *   <li>'n': {@link NullPropertyPointer} - A pointer for a missing property.</li>
 *   <li>'E': {@link NullElementPointer} - A pointer for an out-of-bounds collection index.</li>
 *   <li>'?': Unknown pointer type.</li>
 * </ul>
 */
class SimplePathInterpreterTest {

    private TestBeanWithNode bean;
    private JXPathContext context;

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

    // -----------------------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------------------

    /**
     * Asserts that an XPath resolves to a "null pointer" (a pointer to a non-existent node)
     * and verifies its path representation and internal signature.
     */
    private void assertPathResolvesToNullPointer(final String path, final String expectedPath, final String expectedSignature) {
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "Pointer should not be null for path: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Pointer.asPath() should match for path: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Pointer signature should match for path: " + path);

        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) valuePointer).isActual(), "Value pointer should not be 'actual' for a null path: " + path);
        assertEquals(expectedSignature + "N", pointerSignature(valuePointer), "Value pointer signature should be correct for path: " + path);
    }

    /**
     * Asserts that an XPath resolves to an expected value and verifies the resulting pointer's
     * path representation and internal signature.
     */
    private void assertPathResolvesToValueAndPointer(final String path, final Object expectedValue, final String expectedPath, final String expectedSignature) {
        assertPathResolvesToValueAndPointer(path, expectedValue, expectedPath, expectedSignature, expectedSignature);
    }

    /**
     * Asserts that an XPath resolves to an expected value and verifies the resulting pointer's
     * path representation and internal signature, including a separate signature for the value pointer.
     */
    private void assertPathResolvesToValueAndPointer(final String path, final Object expectedValue, final String expectedPath,
            final String expectedSignature, final String expectedValueSignature) {
        // Check the value
        final Object value = context.getValue(path);
        assertEquals(expectedValue, value, "Value should match for path: " + path);

        // Check the pointer to the value
        final Pointer pointer = context.getPointer(path);
        assertEquals(expectedPath, pointer.toString(), "Pointer.toString() should match for path: " + path);
        assertEquals(expectedSignature, pointerSignature(pointer), "Pointer signature should match for path: " + path);

        // Check the pointer's value pointer
        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValueSignature, pointerSignature(valuePointer), "Value pointer signature should match for path: " + path);
    }

    /**
     * Generates a signature string for a given pointer chain to allow testing
     * of the internal pointer structure created by JXPath. Each character in the
     * signature represents the type of a pointer in the chain, starting from the root.
     * See class-level Javadoc for signature code definitions.
     *
     * @param pointer The final pointer in the chain.
     * @return A string representing the types of pointers in the chain.
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
            System.err.println("UNKNOWN POINTER TYPE: " + pointer.getClass());
        }
        final NodePointer parent = ((NodePointer) pointer).getImmediateParentPointer();
        return pointerSignature(parent) + type;
    }

    // -----------------------------------------------------------------------
    // Test Cases
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Path Navigation without Predicates")
    class StepNavigationTests {

        @Test
        @DisplayName("/int: Accessing an existing scalar property")
        void shouldAccessExistingScalarProperty() {
            assertPathResolvesToValueAndPointer("/int", 1, "/int", "Bb", "BbB");
        }

        @Test
        @DisplayName("./int: Accessing a property via self:: axis")
        void shouldAccessPropertyViaSelfAxis() {
            assertPathResolvesToValueAndPointer("/./int", 1, "/int", "Bb", "BbB");
        }

        @Test
        @DisplayName("/foo: Accessing a missing top-level property")
        void shouldReturnNullPointerForMissingTopLevelProperty() {
            assertPathResolvesToNullPointer("/foo", "/foo", "Bn");
        }

        @Test
        @DisplayName("/nestedBean/int: Accessing a property of a nested bean")
        void shouldAccessPropertyOfNestedBean() {
            assertPathResolvesToValueAndPointer("/nestedBean/int", 1, "/nestedBean/int", "BbBb", "BbBbB");
        }

        @Test
        @DisplayName("/nestedBean/strings: Accessing a collection property")
        void shouldAccessCollectionProperty() {
            assertPathResolvesToValueAndPointer("/nestedBean/strings", bean.getNestedBean().getStrings(), "/nestedBean/strings", "BbBb", "BbBbC");
        }

        @Test
        @DisplayName("/nestedBean/foo: Accessing a missing property of a nested bean")
        void shouldReturnNullPointerForMissingPropertyOfNestedBean() {
            assertPathResolvesToNullPointer("/nestedBean/foo", "/nestedBean/foo", "BbBn");
        }

        @Test
        @DisplayName("/map/foo: Accessing a missing property of a map")
        void shouldReturnNullPointerForMissingPropertyOfMap() {
            assertPathResolvesToNullPointer("/map/foo", "/map[@name='foo']", "BbDd");
        }

        @Test
        @DisplayName("/list/int: Finding and accessing a property within a collection")
        void shouldFindAndAccessPropertyInList() {
            assertPathResolvesToValueAndPointer("/list/int", 1, "/list[3]/int", "BbBb", "BbBbB");
        }

        @Test
        @DisplayName("/list/foo: Searching for a missing property within a collection")
        void shouldReturnNullPointerForMissingPropertyInList() {
            assertPathResolvesToNullPointer("/list/foo", "/list[1]/foo", "BbBn");
        }

        @Test
        @DisplayName("/nestedBean/foo/bar: Accessing a deeply nested missing property")
        void shouldReturnChainedNullPointerForDeeplyMissingProperty() {
            assertPathResolvesToNullPointer("/nestedBean/foo/bar", "/nestedBean/foo/bar", "BbBnNn");
        }

        @Test
        @DisplayName("/map/foo/bar: Accessing a property on a missing map entry")
        void shouldReturnChainedNullPointerForPropertyOnMissingMapEntry() {
            assertPathResolvesToNullPointer("/map/foo/bar", "/map[@name='foo']/bar", "BbDdNn");
        }

        @Test
        @DisplayName("/map/Key1: Accessing an existing dynamic property (map entry)")
        void shouldAccessExistingDynamicProperty() {
            assertPathResolvesToValueAndPointer("/map/Key1", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        }

        @Test
        @DisplayName("/vendor/location/address/city: Navigating through DOM nodes")
        void shouldNavigateDOM() {
            assertPathResolvesToValueAndPointer("/vendor/location/address/city", "Fruit Market", "/vendor/location[2]/address[1]/city[1]", "BbMMMM");
        }

        @Test
        @DisplayName("/vendor/location/address/pity: Accessing a missing DOM node")
        void shouldReturnNullPointerForMissingDOMNode() {
            assertPathResolvesToNullPointer("/vendor/location/address/pity", "/vendor/location[1]/address[1]/pity", "BbMMMn");
        }
    }

    @Nested
    @DisplayName("Tests for Name Predicates [@name='...']")
    class NamePredicateTests {

        @Test
        @DisplayName("/nestedBean[@name='int']: On an existing property")
        void shouldFilterByExistingPropertyName() {
            assertPathResolvesToValueAndPointer("/nestedBean[@name='int']", 1, "/nestedBean/int", "BbBb", "BbBbB");
        }

        @Test
        @DisplayName("/.[@name='int']: On the root node")
        void shouldFilterRootByPropertyName() {
            assertPathResolvesToValueAndPointer("/.[@name='int']", 1, "/int", "Bb", "BbB");
        }

        @Test
        @DisplayName("/map[@name='Key1']: On a dynamic property (map key)")
        void shouldFilterMapByKey() {
            assertPathResolvesToValueAndPointer("/map[@name='Key1']", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        }

        @Test
        @DisplayName("/nestedBean[@name='foo']: On a missing property")
        void shouldReturnNullPointerForMissingPropertyName() {
            assertPathResolvesToNullPointer("/nestedBean[@name='foo']", "/nestedBean[@name='foo']", "BbBn");
        }

        @Test
        @DisplayName("/map[@name='foo']: On a missing map key")
        void shouldReturnNullPointerForMissingMapKey() {
            assertPathResolvesToNullPointer("/map[@name='foo']", "/map[@name='foo']", "BbDd");
        }

        @Test
        @DisplayName("/list[@name='fruitco']: To find a DOM node in a collection")
        void shouldFindNodeInCollectionByName() {
            assertPathResolvesToValueAndPointer("/list[@name='fruitco']", context.getValue("/vendor"), "/list[5]", "BbCM");
        }

        @Test
        @DisplayName("/map/Key3[@name='key']/name: To find a map entry in a collection")
        void shouldFindMapInCollectionByNameAndAccessProperty() {
            assertPathResolvesToValueAndPointer("/map/Key3[@name='key']/name", "Name 9", "/map[@name='Key3'][4][@name='key']/name", "BbDdCDdBb", "BbDdCDdBbB");
        }

        @Test
        @DisplayName("/vendor[@name='fruitco']: On a DOM node with a matching name attribute")
        void shouldFilterDOMNodeByMatchingNameAttribute() {
            assertPathResolvesToValueAndPointer("/vendor[@name='fruitco']", context.getValue("/vendor"), "/vendor", "BbM");
        }

        @Test
        @DisplayName("/vendor[@name='foo']: On a DOM node with a non-matching name attribute")
        void shouldReturnNullPointerForMismatchedDOMNodeNameAttribute() {
            assertPathResolvesToNullPointer("/vendor[@name='foo']", "/vendor[@name='foo']", "BbMn");
        }

        @Test
        @DisplayName("/nestedBean[@name='foo']/bar: Chained path on a missing property")
        void shouldReturnChainedNullPointerAfterMissingNamePredicate() {
            assertPathResolvesToNullPointer("/nestedBean[@name='foo']/bar", "/nestedBean[@name='foo']/bar", "BbBnNn");
        }
    }

    @Nested
    @DisplayName("Tests for Index Predicates [...n...]")
    class IndexPredicateTests {

        @Test
        @DisplayName("/integers[2]: On a simple collection")
        void shouldAccessElementInSimpleCollectionByIndex() {
            assertPathResolvesToValueAndPointer("/integers[2]", 2, "/integers[2]", "Bb", "BbB");
        }

        @Test
        @DisplayName("/nestedBean/strings[2]: On a nested collection property")
        void shouldAccessElementInNestedCollectionByIndex() {
            assertPathResolvesToValueAndPointer("/nestedBean/strings[2]", bean.getNestedBean().getStrings()[1], "/nestedBean/strings[2]", "BbBb", "BbBbB");
        }

        @Test
        @DisplayName("/list[3]/int: Accessing a property after indexing a collection")
        void shouldAccessPropertyOfElementInCollection() {
            assertPathResolvesToValueAndPointer("/list[3]/int", 1, "/list[3]/int", "BbBb", "BbBbB");
        }

        @Test
        @DisplayName("/list[6]: With an out-of-bounds index")
        void shouldReturnNullElementPointerForMissingIndex() {
            assertPathResolvesToNullPointer("/list[6]", "/list[6]", "BbE");
        }

        @Test
        @DisplayName("/nestedBean/strings[5]: On a nested collection with an out-of-bounds index")
        void shouldReturnNullElementPointerForMissingIndexInNestedCollection() {
            assertPathResolvesToNullPointer("/nestedBean/strings[5]", "/nestedBean/strings[5]", "BbBbE");
        }

        @Test
        @DisplayName("/map/Key3[5]/foo: Chained path on an out-of-bounds index")
        void shouldReturnChainedNullPointerAfterMissingIndex() {
            assertPathResolvesToNullPointer("/map/Key3[5]/foo", "/map[@name='Key3'][5]/foo", "BbDdENn");
        }

        @Test
        @DisplayName("/int[1]: On a scalar property treated as a collection")
        void shouldTreatScalarAsCollectionForIndex() {
            assertPathResolvesToValueAndPointer("/int[1]", 1, "/int", "Bb", "BbB");
        }

        @Test
        @DisplayName("/vendor/contact[2]: On DOM nodes")
        void shouldAccessDOMNodeByIndex() {
            assertPathResolvesToValueAndPointer("/vendor/contact[2]", "Jack", "/vendor/contact[2]", "BbMM");
        }

        @Test
        @DisplayName("/vendor/contact[5]: On DOM nodes with an out-of-bounds index")
        void shouldReturnNullPointerForMissingDOMNodeIndex() {
            assertPathResolvesToNullPointer("/vendor/contact[5]", "/vendor/contact[5]", "BbMn");
        }
    }

    @Nested
    @DisplayName("Tests for Combined Predicates")
    class CombinedPredicateTests {

        @Test
        @DisplayName("/map[@name='Key2'][@name='strings'][2]: Chained name and index predicates")
        void shouldHandleChainedNameAndIndexPredicates() {
            assertPathResolvesToValueAndPointer("/map[@name='Key2'][@name='strings'][2]", "String 2", "/map[@name='Key2']/strings[2]", "BbDdBb", "BbDdBbB");
        }

        @Test
        @DisplayName("/map[@name='Key5'][@name='strings'][2]: Chained map, name, and index predicates")
        void shouldHandleDeeplyChainedPredicates() {
            assertPathResolvesToValueAndPointer("/map[@name='Key5'][@name='strings'][2]", "String 2", "/map[@name='Key5'][@name='strings'][2]", "BbDdDd", "BbDdDdB");
        }

        @Test
        @DisplayName("/map[@name='Key3'][@name='fruitco']: Name predicate on a collection from a map")
        void shouldFindNodeByNameInCollectionFromMap() {
            assertPathResolvesToValueAndPointer("map[@name='Key3'][@name='fruitco']", context.getValue("/vendor"), "/map[@name='Key3'][3]", "BbDdCM");
        }


        @Test
        @DisplayName("/vendor/contact[@name='jack'][2]: Name and index predicate on DOM nodes")
        void shouldFilterDOMByNameThenSelectByIndex() {
            assertPathResolvesToValueAndPointer("/vendor/contact[@name='jack'][2]", "Jack Black", "/vendor/contact[4]", "BbMM");
        }

        @Test
        @DisplayName("/vendor/contact[@name='jack'][5]: Name and out-of-bounds index on DOM nodes")
        void shouldReturnNullPointerForMissingIndexAfterNameFilter() {
            assertPathResolvesToNullPointer("/vendor/contact[@name='jack'][5]", "/vendor/contact[@name='jack'][5]", "BbMnNn");
        }
    }

    @Nested
    @DisplayName("Tests for Expression Paths")
    class ExpressionPathTests {
        @Test
        @DisplayName("$var/property[index]: Path starting with a variable")
        void shouldHandlePathStartingWithVariable() {
            context.getVariables().declareVariable("testnull", new TestNull());
            assertPathResolvesToNullPointer("$testnull/nothing[2]", "$testnull/nothing[2]", "VBbE");
        }
    }
}