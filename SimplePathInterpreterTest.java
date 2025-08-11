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
import org.junit.jupiter.api.Test;

/**
 * Tests for SimplePathInterpreter which handles XPath evaluation for simple paths
 * without context-dependent parts. Tests various pointer types and path scenarios.
 */
class SimplePathInterpreterTest {

    private TestBeanWithNode testBean;
    private JXPathContext xpathContext;

    @BeforeEach
    protected void setUp() throws Exception {
        testBean = createComplexTestBean();
        xpathContext = createXPathContext(testBean);
    }

    /**
     * Tests XPath expressions with index predicates like [1], [2], etc.
     * Covers scenarios with existing and missing indices on various data structures.
     */
    @Test
    void testIndexPredicates() {
        // Map with nested property and index access
        verifyExistingPath("/map[@name='Key2'][@name='strings'][2]", "String 2", 
                          "/map[@name='Key2']/strings[2]", "BbDdBb", "BbDdBbB");
        
        // Bean property with collection index
        verifyExistingPath("/nestedBean[@name='strings'][2]", testBean.getNestedBean().getStrings()[1], 
                          "/nestedBean/strings[2]", "BbBb", "BbBbB");
        
        // Missing property with index - should create null pointer
        verifyNullPath("/nestedBean[@name='foo'][3]", "/nestedBean[@name='foo'][3]", "BbBn");
        
        // Index beyond collection bounds
        verifyNullPath("/nestedBean[@name='strings'][5]", "/nestedBean/strings[5]", "BbBbE");
        
        // Map collection access by index
        verifyExistingPath("/map[@name='Key3'][2]", Integer.valueOf(2), 
                          "/map[@name='Key3'][2]", "BbDd", "BbDdB");
        
        // Map collection - index out of bounds
        verifyNullPath("/map[@name='Key3'][5]", "/map[@name='Key3'][5]", "BbDdE");
        
        // Chained null path access
        verifyNullPath("/map[@name='Key3'][5]/foo", "/map[@name='Key3'][5]/foo", "BbDdENn");
        
        // Nested map with collection index
        verifyExistingPath("/map[@name='Key5'][@name='strings'][2]", "String 2", 
                          "/map[@name='Key5'][@name='strings'][2]", "BbDdDd", "BbDdDdB");
        
        // Array property access
        verifyExistingPath("/integers[2]", Integer.valueOf(2), "/integers[2]", "Bb", "BbB");
        
        // List element property access
        verifyExistingPath("/list[3]/int", Integer.valueOf(1), "/list[3]/int", "BbBb", "BbBbB");
        
        // Scalar treated as single-element collection
        verifyExistingPath("/int[1]", Integer.valueOf(1), "/int", "Bb", "BbB");
        verifyExistingPath(".[1]/int", Integer.valueOf(1), "/int", "Bb", "BbB");
    }

    /**
     * Tests XPath expressions with name predicates like [@name='value'].
     * Covers property access, map lookups, and DOM node searches.
     */
    @Test
    void testNamePredicates() {
        // Bean property access by name
        verifyExistingPath("/nestedBean[@name='int']", Integer.valueOf(1), 
                          "/nestedBean/int", "BbBb", "BbBbB");
        
        // Self-reference with name predicate
        verifyExistingPath("/.[@name='int']", Integer.valueOf(1), "/int", "Bb", "BbB");
        
        // Map entry access
        verifyExistingPath("/map[@name='Key1']", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        
        // Collection property access
        verifyExistingPath("/nestedBean[@name='strings']", testBean.getNestedBean().getStrings(), 
                          "/nestedBean/strings", "BbBb", "BbBbC");
        
        // Missing property access
        verifyNullPath("/nestedBean[@name='foo']", "/nestedBean[@name='foo']", "BbBn");
        
        // Search in collection by name attribute
        verifyExistingPath("/list[@name='fruitco']", xpathContext.getValue("/vendor"), 
                          "/list[5]", "BbCM");
        
        // Complex nested map access
        verifyExistingPath("/map/Key3[@name='key']/name", "Name 9", 
                          "/map[@name='Key3'][4][@name='key']/name", "BbDdCDdBb", "BbDdCDdBbB");
        
        // DOM node name matching
        verifyExistingPath("/vendor[@name='fruitco']", xpathContext.getValue("/vendor"), 
                          "/vendor", "BbM");
        
        // DOM node name mismatch
        verifyNullPath("/vendor[@name='foo']", "/vendor[@name='foo']", "BbMn");
        
        // Chained property access on maps
        verifyExistingPath("/map[@name='Key2'][@name='name']", "Name 6", 
                          "/map[@name='Key2']/name", "BbDdBb", "BbDdBbB");
    }

    /**
     * Tests complex predicate combinations on standard InfoSet nodes (like DOM).
     * Covers scenarios with multiple predicates and mixed name/index access.
     */
    @Test
    void testComplexPredicatesOnStandardNodes() {
        // Map collection with DOM node search
        verifyExistingPath("map[@name='Key3'][@name='fruitco']", xpathContext.getValue("/vendor"), 
                          "/map[@name='Key3'][3]", "BbDdCM");
        
        // Map collection search with no match
        verifyNullPath("map[@name='Key3'][@name='foo']", "/map[@name='Key3'][4][@name='foo']", "BbDdCDd");
        
        // Direct map to DOM node access
        verifyExistingPath("map[@name='Key4'][@name='fruitco']", xpathContext.getValue("/vendor"), 
                          "/map[@name='Key4']", "BbDdM");
        
        // Empty collection search
        verifyNullPath("map[@name='Key6'][@name='fruitco']", "/map[@name='Key6'][@name='fruitco']", "BbDdCn");
        
        // DOM node with name and index predicates
        verifyExistingPath("/vendor/contact[@name='jack'][2]", "Jack Black", 
                          "/vendor/contact[4]", "BbMM");
        
        // DOM node search with out-of-bounds index
        verifyNullPath("/vendor/contact[@name='jack'][5]", "/vendor/contact[@name='jack'][5]", "BbMnNn");
        
        // DOM node self-reference with name predicate
        verifyExistingPath("/vendor/contact/.[@name='jack']", "Jack", 
                          "/vendor/contact[2]", "BbMM");
    }

    /**
     * Tests simple property access without predicates on property owners (beans, maps).
     * Covers scalar properties, collections, and missing properties.
     */
    @Test
    void testSimplePropertyAccess() {
        // Basic scalar property
        verifyExistingPath("/int", Integer.valueOf(1), "/int", "Bb", "BbB");
        
        // Self-reference
        verifyExistingPath("/./int", Integer.valueOf(1), "/int", "Bb", "BbB");
        
        // Missing property
        verifyNullPath("/foo", "/foo", "Bn");
        
        // Nested property access
        verifyExistingPath("/nestedBean/int", Integer.valueOf(1), "/nestedBean/int", "BbBb", "BbBbB");
        verifyExistingPath("/nestedBean/strings", testBean.getNestedBean().getStrings(), 
                          "/nestedBean/strings", "BbBb", "BbBbC");
        
        // Missing nested property
        verifyNullPath("/nestedBean/foo", "/nestedBean/foo", "BbBn");
        
        // Map property access
        verifyNullPath("/map/foo", "/map[@name='foo']", "BbDd");
        verifyExistingPath("/map/Key1", "Value 1", "/map[@name='Key1']", "BbDd", "BbDdB");
        
        // Collection property search
        verifyExistingPath("/list/int", Integer.valueOf(1), "/list[3]/int", "BbBb", "BbBbB");
        verifyNullPath("/list/foo", "/list[1]/foo", "BbBn");
        
        // Collection property
        verifyExistingPath("/integers", testBean.getIntegers(), "/integers", "Bb", "BbC");
        
        // Chained missing properties
        verifyNullPath("/nestedBean/foo/bar", "/nestedBean/foo/bar", "BbBnNn");
        verifyNullPath("/list/int/bar", "/list[3]/int/bar", "BbBbBn");
    }

    /**
     * Tests simple property access on standard InfoSet nodes (DOM nodes).
     * Covers existing and missing DOM elements and attributes.
     */
    @Test
    void testSimpleDOMNodeAccess() {
        // Existing DOM path
        verifyExistingPath("/vendor/location/address/city", "Fruit Market", 
                          "/vendor/location[2]/address[1]/city[1]", "BbMMMM");
        
        // Missing DOM element
        verifyNullPath("/vendor/location/address/pity", "/vendor/location[1]/address[1]/pity", "BbMMMn");
        
        // Missing element in missing parent
        verifyNullPath("/vendor/location/address/itty/bitty", 
                      "/vendor/location[1]/address[1]/itty/bitty", "BbMMMnNn");
        
        // Missing child of existing element
        verifyNullPath("/vendor/location/address/city/pretty", 
                      "/vendor/location[2]/address[1]/city[1]/pretty", "BbMMMMn");
    }

    /**
     * Tests predicates on missing properties.
     * Ensures proper null pointer creation for non-existent paths.
     */
    @Test
    void testPredicatesOnMissingProperties() {
        // Missing property with name predicate
        verifyNullPath("/foo[@name='foo']", "/foo[@name='foo']", "BnNn");
        
        // Missing property with index predicate
        verifyNullPath("/foo[3]", "/foo[3]", "Bn");
    }

    /**
     * Tests predicates on standard InfoSet nodes (DOM).
     * Covers XML attribute matching and indexing.
     */
    @Test
    void testPredicatesOnStandardNodes() {
        // XML attribute matching
        verifyExistingPath("/vendor/contact[@name='jack']", "Jack", "/vendor/contact[2]", "BbMM");
        
        // XML indexing
        verifyExistingPath("/vendor/contact[2]", "Jack", "/vendor/contact[2]", "BbMM");
        
        // XML indexing out of bounds
        verifyNullPath("/vendor/contact[5]", "/vendor/contact[5]", "BbMn");
        
        // Combined name and index predicates
        verifyExistingPath("/vendor/contact[@name='jack'][2]", "Jack Black", 
                          "/vendor/contact[4]", "BbMM");
    }

    /**
     * Tests variable references in XPath expressions.
     */
    @Test
    void testVariableReferences() {
        xpathContext.getVariables().declareVariable("array", new String[] { "Value1" });
        xpathContext.getVariables().declareVariable("testnull", new TestNull());
        
        // Variable reference with missing property and index
        verifyNullPath("$testnull/nothing[2]", "$testnull/nothing[2]", "VBbE");
    }

    // Helper methods for test setup and verification

    private TestBeanWithNode createComplexTestBean() {
        TestBeanWithNode bean = TestBeanWithNode.createTestBeanWithDOM();
        
        // Create nested map structure for testing
        HashMap<String, Object> submap = new HashMap<>();
        submap.put("key", new NestedTestBean("Name 9"));
        submap.put("strings", bean.getNestedBean().getStrings());
        
        // Add complex objects to list
        bean.getList().add(new int[] { 1, 2 });
        bean.getList().add(bean.getVendor());
        
        // Add complex objects to map
        bean.getMap().put("Key3", new Object[] { 
            new NestedTestBean("some"), 
            Integer.valueOf(2), 
            bean.getVendor(), 
            submap 
        });
        bean.getMap().put("Key4", bean.getVendor());
        bean.getMap().put("Key5", submap);
        bean.getMap().put("Key6", new Object[0]);
        
        return bean;
    }

    private JXPathContext createXPathContext(TestBeanWithNode bean) {
        JXPathContext context = JXPathContext.newContext(null, bean);
        context.setLenient(true);
        context.setFactory(new TestBeanFactory());
        return context;
    }

    /**
     * Verifies that a path resolves to null and creates appropriate null pointers.
     */
    private void verifyNullPath(String xpath, String expectedPath, String expectedPointerSignature) {
        Pointer pointer = xpathContext.getPointer(xpath);
        assertNotNull(pointer, "Null path should exist: " + xpath);
        assertEquals(expectedPath, pointer.asPath(), "Null path representation: " + xpath);
        assertEquals(expectedPointerSignature, buildPointerSignature(pointer), 
                    "Pointer type signature: " + xpath);
        
        Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertFalse(((NodePointer) valuePointer).isActual(), "Path should point to null: " + xpath);
        assertEquals(expectedPointerSignature + "N", buildPointerSignature(valuePointer), 
                    "Value pointer signature: " + xpath);
    }

    /**
     * Verifies that a path resolves to the expected value with correct pointer types.
     */
    private void verifyExistingPath(String xpath, Object expectedValue, String expectedPath, 
                                   String expectedPointerSignature) {
        verifyExistingPath(xpath, expectedValue, expectedPath, expectedPointerSignature, expectedPointerSignature);
    }

    /**
     * Verifies that a path resolves to the expected value with correct pointer types,
     * allowing different signatures for the main pointer and value pointer.
     */
    private void verifyExistingPath(String xpath, Object expectedValue, String expectedPath, 
                                   String expectedPointerSignature, String expectedValuePointerSignature) {
        // Verify the resolved value
        Object actualValue = xpathContext.getValue(xpath);
        assertEquals(expectedValue, actualValue, "XPath value: " + xpath);
        
        // Verify the pointer path representation
        Pointer pointer = xpathContext.getPointer(xpath);
        assertEquals(expectedPath, pointer.toString(), "Pointer path: " + xpath);
        
        // Verify pointer type chain
        assertEquals(expectedPointerSignature, buildPointerSignature(pointer), 
                    "Pointer signature: " + xpath);
        
        // Verify value pointer type chain
        Pointer valuePointer = ((NodePointer) pointer).getValuePointer();
        assertEquals(expectedValuePointerSignature, buildPointerSignature(valuePointer), 
                    "Value pointer signature: " + xpath);
    }

    /**
     * Builds a signature string representing the chain of pointer types.
     * Each character represents a different pointer type:
     * N=NullPointer, n=NullPropertyPointer, E=NullElementPointer, 
     * V=VariablePointer, C=CollectionPointer, B=BeanPointer, 
     * b=BeanPropertyPointer, D=DynamicPointer, d=DynamicPropertyPointer, 
     * M=DOMNodePointer
     */
    private String buildPointerSignature(Pointer pointer) {
        if (pointer == null) {
            return "";
        }
        
        char pointerTypeCode = determinePointerTypeCode(pointer);
        NodePointer parentPointer = ((NodePointer) pointer).getImmediateParentPointer();
        return buildPointerSignature(parentPointer) + pointerTypeCode;
    }

    private char determinePointerTypeCode(Pointer pointer) {
        if (pointer instanceof NullPointer) return 'N';
        if (pointer instanceof NullPropertyPointer) return 'n';
        if (pointer instanceof NullElementPointer) return 'E';
        if (pointer instanceof VariablePointer) return 'V';
        if (pointer instanceof CollectionPointer) return 'C';
        if (pointer instanceof BeanPointer) return 'B';
        if (pointer instanceof BeanPropertyPointer) return 'b';
        if (pointer instanceof DynamicPointer) return 'D';
        if (pointer instanceof DynamicPropertyPointer) return 'd';
        if (pointer instanceof DOMNodePointer) return 'M';
        
        System.err.println("UNKNOWN POINTER TYPE: " + pointer.getClass());
        return '?';
    }
}