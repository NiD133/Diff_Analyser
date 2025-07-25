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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Tests for {@link BasicNodeSet}.  These tests verify the add, remove, and retrieval
 * of pointers, values, and nodes within the BasicNodeSet.  The tests also demonstrate
 * scenarios where nodes and values may differ, particularly when working with XML models.
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    private JXPathContext context;
    private BasicNodeSet nodeSet;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Initialize JXPathContext with a test bean for consistent testing.
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Adds pointers to the {@link #nodeSet} based on the provided XPath expression.
     * After adding, it triggers a "nudge" to ensure the node set is fully populated.
     *
     * @param xpath The XPath expression to locate the pointers to add.
     */
    private void addPointersToNodeSet(final String xpath) {
        Iterator<Pointer> pointerIterator = context.iteratePointers(xpath);
        while (pointerIterator.hasNext()) {
            nodeSet.add(pointerIterator.next());
        }
        nudgeNodeSet();
    }

    /**
     * Asserts that a list of DOM elements have the expected tag names.
     * Useful for verifying node retrieval from the NodeSet.
     *
     * @param expectedNames The list of expected tag names.
     * @param actualElements The list of DOM elements to check.
     */
    private void assertElementTagNames(final List<String> expectedNames, final List<Element> actualElements) {
        assertEquals(expectedNames.size(), actualElements.size(), "Number of elements should match the number of expected names.");
        for (int i = 0; i < expectedNames.size(); i++) {
            assertEquals(expectedNames.get(i), actualElements.get(i).getTagName(), "Element tag name at index " + i + " does not match.");
        }
    }

    /**
     * Asserts that a list of DOM elements have the expected text values.
     * This is used to confirm the values extracted from the nodes in the NodeSet.
     *
     * @param expectedValues The list of expected text values.
     * @param actualElements The list of DOM elements to check.
     */
    private void assertElementTextValues(final List<String> expectedValues, final List<Element> actualElements) {
        assertEquals(expectedValues.size(), actualElements.size(), "Number of elements should match the number of expected values.");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(expectedValues.get(i), actualElements.get(i).getFirstChild().getNodeValue(), "Element value at index " + i + " does not match.");
        }
    }

    /**
     *  "Nudges" the {@link #nodeSet} by calling getPointers(), getValues(), and getNodes().
     *  This simulates access and population of the node set's cached data.
     */
    private void nudgeNodeSet() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Removes pointers from the {@link #nodeSet} based on the provided XPath expression.
     * After removal, it triggers a "nudge" to update the node set.
     *
     * @param xpath The XPath expression to locate the pointers to remove.
     */
    private void removePointersFromNodeSet(final String xpath) {
        Iterator<Pointer> pointerIterator = context.iteratePointers(xpath);
        while (pointerIterator.hasNext()) {
            nodeSet.remove(pointerIterator.next());
        }
        nudgeNodeSet();
    }

    /**
     * Tests adding pointers to the BasicNodeSet and verifies that the pointers, values, and nodes
     * are correctly populated and accessible.
     */
    @Test
    void testAddPointers() {
        String xpath = "/bean/integers";
        addPointersToNodeSet(xpath);

        List<String> expectedPointers = list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]", "/bean/integers[4]");
        List<Integer> expectedValues = list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4));

        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString(), "Pointers in the node set do not match the expected pointers.");
        assertEquals(expectedValues, nodeSet.getValues(), "Values in the node set do not match the expected values.");
        assertEquals(expectedValues, nodeSet.getNodes(), "Nodes in the node set do not match the expected nodes.");
    }

    /**
     * Demonstrates a scenario where the nodes and values retrieved from the BasicNodeSet are different.
     * This is common in XML models where the node is an Element, but the value is the element's text content.
     */
    @Test
    void testNodesAndValuesAreDistinct() {
        String xpath = "/document/vendor/contact";
        addPointersToNodeSet(xpath);

        List<String> expectedPointers = list("/document/vendor[1]/contact[1]", "/document/vendor[1]/contact[2]", "/document/vendor[1]/contact[3]", "/document/vendor[1]/contact[4]");
        List<String> expectedValues = list("John", "Jack", "Jim", "Jack Black");
        List<String> expectedNames = list("contact", "contact", "contact", "contact");


        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString(), "Pointers in the node set do not match the expected pointers.");
        assertEquals(expectedValues, nodeSet.getValues(), "Values in the node set do not match the expected values.");
        assertElementTagNames(expectedNames, (List<Element>) nodeSet.getNodes());
        assertElementTextValues(expectedValues, (List<Element>) nodeSet.getNodes());
    }

    /**
     * Tests removing a pointer from the BasicNodeSet and verifies that the pointers, values, and nodes
     * are correctly updated after the removal.
     */
    @Test
    void testRemovePointer() {
        String xpathToAdd = "/bean/integers";
        addPointersToNodeSet(xpathToAdd);

        String xpathToRemove = "/bean/integers[4]";
        removePointersFromNodeSet(xpathToRemove);

        List<String> expectedPointers = list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]");
        List<Integer> expectedValues = list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));

        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString(), "Pointers in the node set do not match the expected pointers after removal.");
        assertEquals(expectedValues, nodeSet.getValues(), "Values in the node set do not match the expected values after removal.");
        assertEquals(expectedValues, nodeSet.getNodes(), "Nodes in the node set do not match the expected nodes after removal.");
    }
}