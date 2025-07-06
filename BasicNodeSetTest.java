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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Tests for {@link BasicNodeSet}.
 *
 * <p>This class contains test cases that verify the functionality of the {@link BasicNodeSet} class,
 * including adding, removing, and retrieving pointers, nodes, and values.</p>
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    private JXPathContext context;
    private BasicNodeSet nodeSet;

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        // Initialize JXPathContext with a sample data model
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Adds pointers to the {@code nodeSet} based on the given XPath expression.
     *
     * @param xpath The XPath expression to use to find pointers.
     */
    private void addPointersToNodeSet(final String xpath) {
        context.iteratePointers(xpath).forEachRemaining(nodeSet::add);
        // Call nudge to populate the cached values after modifying the nodeSet
        nudge();
    }

    /**
     * Asserts that a list of DOM elements have the expected tag names.
     *
     * @param expectedNames A list of expected tag names.
     * @param elements      A list of DOM elements to check.
     */
    private void assertElementNames(final List<String> expectedNames, final List<Element> elements) {
        assertEquals(expectedNames.size(), elements.size(), "Number of elements should match the number of expected names.");
        for (int i = 0; i < expectedNames.size(); i++) {
            assertEquals(expectedNames.get(i), elements.get(i).getTagName(), "Element tag name at index " + i + " does not match.");
        }
    }

    /**
     * Asserts that a list of DOM elements have the expected text values.
     *
     * @param expectedValues A list of expected text values.
     * @param elements       A list of DOM elements to check.
     */
    private void assertElementValues(final List<String> expectedValues, final List<Element> elements) {
        assertEquals(expectedValues.size(), elements.size(), "Number of elements should match the number of expected values.");
        for (int i = 0; i < expectedValues.size(); i++) {
            assertEquals(expectedValues.get(i), elements.get(i).getFirstChild().getNodeValue(), "Element value at index " + i + " does not match.");
        }
    }

    /**
     * Simulates accessing the nodeSet's pointers, values and nodes to trigger internal cache population.
     * This is required because `BasicNodeSet` caches the result of these calls.
     */
    private void nudge() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Removes pointers from the {@code nodeSet} based on the given XPath expression.
     *
     * @param xpath The XPath expression to use to find pointers to remove.
     */
    private void removePointersFromNodeSet(final String xpath) {
        context.iteratePointers(xpath).forEachRemaining(nodeSet::remove);
        // Call nudge to populate the cached values after modifying the nodeSet
        nudge();
    }

    /**
     * Tests adding pointers to the {@link BasicNodeSet}.
     * It verifies that the pointers, values, and nodes are correctly added and retrieved.
     */
    @Test
    void testAdd() {
        addPointersToNodeSet("/bean/integers");
        List<String> expectedPointers = list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]", "/bean/integers[4]").stream().map(Object::toString).collect(Collectors.toList());
        List<Integer> expectedValues = list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4));

        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString(), "Pointers should match the expected values.");
        assertEquals(expectedValues, nodeSet.getValues(), "Values should match the expected values.");
        assertEquals(expectedValues, nodeSet.getNodes(), "Nodes should match the expected values.");
    }

    /**
     * Demonstrates a scenario where nodes and values are different, which is common in XML models.
     * It tests the retrieval of pointers, values, and nodes from an XML document.
     */
    @Test
    void testNodes() {
        addPointersToNodeSet("/document/vendor/contact");
        List<String> expectedPointers = list("/document/vendor[1]/contact[1]", "/document/vendor[1]/contact[2]", "/document/vendor[1]/contact[3]", "/document/vendor[1]/contact[4]")
                .stream().map(Object::toString).collect(Collectors.toList());
        List<String> expectedValues = list("John", "Jack", "Jim", "Jack Black");
        List<String> expectedNames = list("contact", "contact", "contact", "contact");

        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString(), "Pointers should match the expected values.");
        assertEquals(expectedValues, nodeSet.getValues(), "Values should match the expected values.");
        assertElementNames(expectedNames, (List<Element>) nodeSet.getNodes());
        assertElementValues(expectedValues, (List<Element>) nodeSet.getNodes());
    }

    /**
     * Tests removing a pointer from the {@link BasicNodeSet}.
     * It verifies that the pointers, values, and nodes are correctly removed and retrieved.
     */
    @Test
    void testRemove() {
        addPointersToNodeSet("/bean/integers");
        removePointersFromNodeSet("/bean/integers[4]");

        List<String> expectedPointers = list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]").stream().map(Object::toString).collect(Collectors.toList());
        List<Integer> expectedValues = list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));

        assertEquals(expectedPointers.toString(), nodeSet.getPointers().toString(), "Pointers should match the expected values.");
        assertEquals(expectedValues, nodeSet.getValues(), "Values should match the expected values.");
        assertEquals(expectedValues, nodeSet.getNodes(), "Nodes should match the expected values.");
    }
}