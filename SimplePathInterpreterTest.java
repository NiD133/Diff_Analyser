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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SimplePathInterpreter} focusing on various XPath evaluation scenarios.
 * The tests cover different types of paths including:
 * - Index-based predicates
 * - Name-based predicates
 * - Standard path evaluations
 * - Property owner scenarios
 * - Null pointer cases
 */
class SimplePathInterpreterTest {

    private TestBeanWithNode bean;
    private JXPathContext context;

    /**
     * Asserts that a null pointer exists at the specified path with expected properties.
     */
    private void assertNullPointer(final String path, final String expectedPath, final String expectedSignature) {
        final Pointer pointer = context.getPointer(path);
        assertNotNull(pointer, "Pointer should exist at path: " + path);
        assertEquals(expectedPath, pointer.asPath(), "Pointer path mismatch");
        assertEquals(expectedSignature, pointerSignature(pointer), "Pointer signature mismatch");
        
        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) valuePointer).isActual(), "Value pointer should not be actual");
        assertEquals(expectedSignature + "N", pointerSignature(valuePointer), "Value pointer signature mismatch");
    }

    /**
     * Asserts both value and pointer properties at the specified path.
     */
    private void assertValueAndPointer(final String path, final Object expectedValue, final String expectedPath, 
                                       final String expectedSignature) {
        assertValueAndPointer(path, expectedValue, expectedPath, expectedSignature, expectedSignature);
    }

    private void assertValueAndPointer(final String path, final Object expectedValue, final String expectedPath, 
                                       final String expectedSignature, final String expectedValueSignature) {
        final Object value = context.getValue(path);
        assertEquals(expectedValue, value, "Value mismatch at path: " + path);
        
        final Pointer pointer = context.getPointer(path);
        assertEquals(expectedPath, pointer.toString(), "Pointer representation mismatch");
        assertEquals(expectedSignature, pointerSignature(pointer), "Pointer signature mismatch");
        
        final Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValueSignature, pointerSignature(valuePointer), "Value pointer signature mismatch");
    }

    /**
     * Generates a signature string representing the pointer hierarchy.
     * Each character represents a pointer type in the chain:
     * N: NullPointer, n: NullPropertyPointer, E: NullElementPointer
     * V: VariablePointer, C: CollectionPointer, B: BeanPointer
     * b: BeanPropertyPointer, D: DynamicPointer, d: DynamicPropertyPointer
     * M: DOMNodePointer
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
        final HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());
        
        bean.getList().add(new int[] { 1, 2 });
        bean.getList().add(bean.getVendor());
        
        bean.getMap().put("Key3", new Object[] { 
            new NestedTestBean("some"), 
            Integer.valueOf(2), 
            bean.getVendor(), 
            submap 
        });
        bean.getMap().put("Key4", bean.getVendor());
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);
        
        context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
    }

    @Nested
    class IndexPredicateTests {
        
        @Test
        void existingDynamicPropertyWithExistingPropertyAndIndex() {
            assertValueAndPointer(
                "/map[@name='Key2'][@name='strings'][2]", 
                "String 2", 
                "/map[@name='Key2']/strings[2]", 
                "BbDdBb", 
                "BbDdBbB"
            );
        }
        
        @Test
        void existingPropertyWithCollectionPropertyAndIndex() {
            assertValueAndPointer(
                "/nestedBean[@name='strings'][2]", 
                bean.getNestedBean().getStrings()[1], 
                "/nestedBean/strings[2]", 
                "BbBb", 
                "BbBbB"
            );
        }
        
        @Test
        void existingPropertyWithMissingPropertyAndIndex() {
            assertNullPointer(
                "/nestedBean[@name='foo'][3]", 
                "/nestedBean[@name='foo'][3]", 
                "BbBn"
            );
        }
        
        @Test
        void existingPropertyWithCollectionPropertyAndMissingIndex() {
            assertNullPointer(
                "/nestedBean[@name='strings'][5]", 
                "/nestedBean/strings[5]", 
                "BbBbE"
            );
        }
        
        @Test
        void mapWithCollectionPropertyAndIndex() {
            assertValueAndPointer(
                "/map[@name='Key3'][2]", 
                Integer.valueOf(2), 
                "/map[@name='Key3'][2]", 
                "BbDd", 
                "BbDdB"
            );
        }
        
        @Test
        void mapWithCollectionPropertyAndMissingIndex() {
            assertNullPointer(
                "/map[@name='Key3'][5]", 
                "/map[@name='Key3'][5]", 
                "BbDdE"
            );
        }
        
        @Test
        void mapWithCollectionPropertyMissingIndexAndProperty() {
            assertNullPointer(
                "/map[@name='Key3'][5]/foo", 
                "/map[@name='Key3'][5]/foo", 
                "BbDdENn"
            );
        }
        
        @Test
        void nestedMapWithCollectionAndIndex() {
            assertValueAndPointer(
                "/map[@name='Key5'][@name='strings'][2]", 
                "String 2", 
                "/map[@name='Key5'][@name='strings'][2]", 
                "BbDdDd", 
                "BbDdDdB"
            );
        }
        
        @Test
        void nestedMapWithCollectionAndMissingIndex() {
            assertNullPointer(
                "/map[@name='Key5'][@name='strings'][5]", 
                "/map[@name='Key5'][@name='strings'][5]", 
                "BbDdDdE"
            );
        }
        
        @Test
        void existingDynamicPropertyWithIndex() {
            assertValueAndPointer(
                "/map[@name='Key3'][2]", 
                Integer.valueOf(2), 
                "/map[@name='Key3'][2]", 
                "BbDd", 
                "BbDdB"
            );
        }
        
        @Test
        void mapWithMissingPropertyAndIndex() {
            assertNullPointer(
                "/map[@name='foo'][3]", 
                "/map[@name='foo'][3]", 
                "BbDdE"
            );
        }
        
        @Test
        void collectionPropertyWithIndex() {
            assertValueAndPointer(
                "/integers[2]", 
                Integer.valueOf(2), 
                "/integers[2]", 
                "Bb", 
                "BbB"
            );
        }
        
        @Test
        void existingPropertyWithCollectionPropertyIndex() {
            assertValueAndPointer(
                "/nestedBean/strings[2]", 
                bean.getNestedBean().getStrings()[1], 
                "/nestedBean/strings[2]", 
                "BbBb", 
                "BbBbB"
            );
        }
        
        @Test
        void existingPropertyIndexWithProperty() {
            assertValueAndPointer(
                "/list[3]/int", 
                Integer.valueOf(1), 
                "/list[3]/int", 
                "BbBb", 
                "BbBbB"
            );
        }
        
        @Test
        void existingPropertyWithMissingIndex() {
            assertNullPointer(
                "/list[6]", 
                "/list[6]", 
                "BbE"
            );
        }
        
        @Test
        void scalarPropertyAsCollectionWithIndex() {
            assertValueAndPointer(
                "/int[1]", 
                Integer.valueOf(1), 
                "/int", 
                "Bb", 
                "BbB"
            );
        }
    }

    @Nested
    class NamePredicateTests {
        
        @Test
        void existingPropertyWithExistingPropertyName() {
            assertValueAndPointer(
                "/nestedBean[@name='int']", 
                Integer.valueOf(1), 
                "/nestedBean/int", 
                "BbBb", 
                "BbBbB"
            );
        }
        
        @Test
        void selfNodeWithExistingPropertyName() {
            assertValueAndPointer(
                "/.[@name='int']", 
                Integer.valueOf(1), 
                "/int", 
                "Bb", 
                "BbB"
            );
        }
        
        @Test
        void dynamicPropertyWithExistingProperty() {
            assertValueAndPointer(
                "/map[@name='Key1']", 
                "Value 1", 
                "/map[@name='Key1']", 
                "BbDd", 
                "BbDdB"
            );
        }
        
        @Test
        void existingPropertyWithCollectionPropertyName() {
            assertValueAndPointer(
                "/nestedBean[@name='strings']", 
                bean.getNestedBean().getStrings(), 
                "/nestedBean/strings", 
                "BbBb", 
                "BbBbC"
            );
        }
        
        @Test
        void existingPropertyWithMissingPropertyName() {
            assertNullPointer(
                "/nestedBean[@name='foo']", 
                "/nestedBean[@name='foo']", 
                "BbBn"
            );
        }
        
        @Test
        void mapWithCollectionPropertyName() {
            assertValueAndPointer(
                "/map[@name='Key3']", 
                bean.getMap().get("Key3"), 
                "/map[@name='Key3']", 
                "BbDd", 
                "BbDdC"
            );
        }
        
        @Test
        void collectionPropertyWithNameMatch() {
            assertValueAndPointer(
                "/list[@name='fruitco']", 
                context.getValue("/vendor"), 
                "/list[5]", 
                "BbCM"
            );
        }
        
        @Test
        void beanPropertyDomNodeNameMatch() {
            assertValueAndPointer(
                "/vendor[@name='fruitco']", 
                context.getValue("/vendor"), 
                "/vendor", 
                "BbM"
            );
        }
        
        @Test
        void beanPropertyDomNodeNameMismatch() {
            assertNullPointer(
                "/vendor[@name='foo']", 
                "/vendor[@name='foo']", 
                "BbMn"
            );
        }
        
        @Test
        void existingDynamicPropertyWithExistingProperty() {
            assertValueAndPointer(
                "/map[@name='Key2'][@name='name']", 
                "Name 6", 
                "/map[@name='Key2']/name", 
                "BbDdBb", 
                "BbDdBbB"
            );
        }
        
        @Test
        void nestedMapWithProperty() {
            assertValueAndPointer(
                "map[@name='Key5'][@name='key']/name", 
                "Name 9", 
                "/map[@name='Key5'][@name='key']/name", 
                "BbDdDdBb", 
                "BbDdDdBbB"
            );
        }
    }

    @Nested
    class StandardPredicateTests {
        
        @Test
        void mapWithCollectionAndDomNode() {
            assertValueAndPointer(
                "map[@name='Key3'][@name='fruitco']", 
                context.getValue("/vendor"), 
                "/map[@name='Key3'][3]", 
                "BbDdCM"
            );
        }
        
        @Test
        void mapWithCollectionAndMissingNode() {
            assertNullPointer(
                "map[@name='Key3'][@name='foo']", 
                "/map[@name='Key3'][4][@name='foo']", 
                "BbDdCDd"
            );
        }
        
        @Test
        void beanDomNodeWithNameAndIndex() {
            assertValueAndPointer(
                "/vendor/contact[@name='jack'][2]", 
                "Jack Black", 
                "/vendor/contact[4]", 
                "BbMM"
            );
        }
        
        @Test
        void beanDomNodeWithNameAndMissingIndex() {
            assertNullPointer(
                "/vendor/contact[@name='jack'][5]", 
                "/vendor/contact[@name='jack'][5]", 
                "BbMnNn"
            );
        }
    }

    @Nested
    class PropertyOwnerStepTests {
        
        @Test
        void existingScalarProperty() {
            assertValueAndPointer(
                "/int", 
                Integer.valueOf(1), 
                "/int", 
                "Bb", 
                "BbB"
            );
        }
        
        @Test
        void selfNodeWithProperty() {
            assertValueAndPointer(
                "/./int", 
                Integer.valueOf(1), 
                "/int", 
                "Bb", 
                "BbB"
            );
        }
        
        @Test
        void missingProperty() {
            assertNullPointer(
                "/foo", 
                "/foo", 
                "Bn"
            );
        }
        
        @Test
        void nestedExistingScalarProperty() {
            assertValueAndPointer(
                "/nestedBean/int", 
                Integer.valueOf(1), 
                "/nestedBean/int", 
                "BbBb", 
                "BbBbB"
            );
        }
        
        @Test
        void existingPropertyInCollection() {
            assertValueAndPointer(
                "/list/int", 
                Integer.valueOf(1), 
                "/list[3]/int", 
                "BbBb", 
                "BbBbB"
            );
        }
        
        @Test
        void existingDynamicProperty() {
            assertValueAndPointer(
                "/map/Key1", 
                "Value 1", 
                "/map[@name='Key1']", 
                "BbDd", 
                "BbDdB"
            );
        }
        
        @Test
        void collectionProperty() {
            assertValueAndPointer(
                "/integers", 
                bean.getIntegers(), 
                "/integers", 
                "Bb", 
                "BbC"
            );
        }
    }

    @Nested
    class StandardStepTests {
        
        @Test
        void existingDomNodePath() {
            assertValueAndPointer(
                "/vendor/location/address/city", 
                "Fruit Market", 
                "/vendor/location[2]/address[1]/city[1]", 
                "BbMMMM"
            );
        }
        
        @Test
        void missingDomNode() {
            assertNullPointer(
                "/vendor/location/address/pity", 
                "/vendor/location[1]/address[1]/pity", 
                "BbMMMn"
            );
        }
    }

    @Nested
    class PropertyOwnerPredicateStepTests {
        
        @Test
        void missingPropertyWithNamePredicate() {
            assertNullPointer(
                "/foo[@name='foo']", 
                "/foo[@name='foo']", 
                "BnNn"
            );
        }
        
        @Test
        void missingPropertyWithIndex() {
            assertNullPointer(
                "/foo[3]", 
                "/foo[3]", 
                "Bn"
            );
        }
    }

    @Nested
    class StandardPredicateStepTests {
        
        @Test
        void domNodeWithAttributeName() {
            assertValueAndPointer(
                "/vendor/contact[@name='jack']", 
                "Jack", 
                "/vendor/contact[2]", 
                "BbMM"
            );
        }
        
        @Test
        void domNodeWithIndex() {
            assertValueAndPointer(
                "/vendor/contact[2]", 
                "Jack", 
                "/vendor/contact[2]", 
                "BbMM"
            );
        }
        
        @Test
        void domNodeWithMissingIndex() {
            assertNullPointer(
                "/vendor/contact[5]", 
                "/vendor/contact[5]", 
                "BbMn"
            );
        }
    }

    @Test
    void interpretExpressionPathWithNullElement() {
        context.getVariables().declareVariable("array", new String[] { "Value1" });
        context.getVariables().declareVariable("testnull", new TestNull());
        assertNullPointer(
            "$testnull/nothing[2]", 
            "$testnull/nothing[2]", 
            "VBbE"
        );
    }
}