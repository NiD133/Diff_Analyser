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

package org.apache.commons.jxpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Unit tests for the BasicNodeSet class.
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    /** Context for JXPath operations */
    private JXPathContext context;

    /** Instance of BasicNodeSet under test */
    private BasicNodeSet nodeSet;

    /**
     * Sets up the test environment before each test.
     */
    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Adds pointers from the specified XPath to the nodeSet.
     *
     * @param xpath the XPath expression to evaluate
     */
    private void addPointers(final String xpath) {
        Iterator<Pointer> iterator = context.iteratePointers(xpath);
        while (iterator.hasNext()) {
            nodeSet.add(iterator.next());
        }
        refreshNodeSet();
    }

    /**
     * Removes pointers from the specified XPath from the nodeSet.
     *
     * @param xpath the XPath expression to evaluate
     */
    private void removePointers(final String xpath) {
        Iterator<Pointer> iterator = context.iteratePointers(xpath);
        while (iterator.hasNext()) {
            nodeSet.remove(iterator.next());
        }
        refreshNodeSet();
    }

    /**
     * Refreshes the nodeSet by accessing its pointers, values, and nodes.
     */
    private void refreshNodeSet() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Asserts that the names of the DOM elements match the expected names.
     *
     * @param expectedNames the list of expected element names
     * @param elements the list of DOM elements to check
     */
    private void assertElementNames(final List<String> expectedNames, final List<Element> elements) {
        assertEquals(expectedNames.size(), elements.size(), "Element list sizes do not match");
        Iterator<String> nameIterator = expectedNames.iterator();
        Iterator<Element> elementIterator = elements.iterator();
        while (elementIterator.hasNext()) {
            assertEquals(nameIterator.next(), elementIterator.next().getTagName(), "Element names do not match");
        }
    }

    /**
     * Asserts that the values of the DOM elements match the expected values.
     *
     * @param expectedValues the list of expected element values
     * @param elements the list of DOM elements to check
     */
    private void assertElementValues(final List<String> expectedValues, final List<Element> elements) {
        assertEquals(expectedValues.size(), elements.size(), "Element list sizes do not match");
        Iterator<String> valueIterator = expectedValues.iterator();
        Iterator<Element> elementIterator = elements.iterator();
        while (elementIterator.hasNext()) {
            assertEquals(valueIterator.next(), elementIterator.next().getFirstChild().getNodeValue(), "Element values do not match");
        }
    }

    /**
     * Tests adding pointers to the nodeSet.
     */
    @Test
    void testAddPointers() {
        addPointers("/bean/integers");
        assertEquals(list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]", "/bean/integers[4]").toString(), nodeSet.getPointers().toString(), "Pointers do not match expected values");
        assertEquals(list(1, 2, 3, 4), nodeSet.getValues(), "Values do not match expected values");
        assertEquals(list(1, 2, 3, 4), nodeSet.getNodes(), "Nodes do not match expected values");
    }

    /**
     * Tests the behavior of nodes and values in XML models.
     */
    @Test
    void testNodesInXMLModel() {
        addPointers("/document/vendor/contact");
        assertEquals(list("/document/vendor[1]/contact[1]", "/document/vendor[1]/contact[2]", "/document/vendor[1]/contact[3]", "/document/vendor[1]/contact[4]").toString(), nodeSet.getPointers().toString(), "Pointers do not match expected values");
        assertEquals(list("John", "Jack", "Jim", "Jack Black"), nodeSet.getValues(), "Values do not match expected values");
        assertElementNames(list("contact", "contact", "contact", "contact"), nodeSet.getNodes());
        assertElementValues(list("John", "Jack", "Jim", "Jack Black"), nodeSet.getNodes());
    }

    /**
     * Tests removing a pointer from the nodeSet.
     */
    @Test
    void testRemovePointer() {
        addPointers("/bean/integers");
        removePointers("/bean/integers[4]");
        assertEquals(list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]").toString(), nodeSet.getPointers().toString(), "Pointers do not match expected values after removal");
        assertEquals(list(1, 2, 3), nodeSet.getValues(), "Values do not match expected values after removal");
        assertEquals(list(1, 2, 3), nodeSet.getNodes(), "Nodes do not match expected values after removal");
    }
}