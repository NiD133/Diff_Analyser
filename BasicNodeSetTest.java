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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Tests for {@link BasicNodeSet} functionality.
 * 
 * BasicNodeSet is a collection of pointers that can be used to navigate
 * and manipulate nodes in a JXPath context.
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    private JXPathContext context;
    private BasicNodeSet nodeSet;

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Test that pointers can be added to a BasicNodeSet and retrieved correctly.
     * This test uses integer array elements from a bean to verify:
     * - Pointers are stored with correct paths
     * - Values match the original integers
     * - Nodes match the values (for non-XML models)
     */
    @Test
    void testAdd_shouldStorePointersWithCorrectPathsAndValues() {
        // Given: A path to an integer array in the test bean
        String integersPath = "/bean/integers";
        
        // When: Adding all pointers from that path
        addPointersFromPath(integersPath);
        
        // Then: The nodeSet should contain all four integer elements
        List<String> expectedPaths = Arrays.asList(
            "/bean/integers[1]", 
            "/bean/integers[2]", 
            "/bean/integers[3]", 
            "/bean/integers[4]"
        );
        List<Integer> expectedValues = Arrays.asList(1, 2, 3, 4);
        
        assertPointerPaths(expectedPaths, nodeSet.getPointers());
        assertEquals(expectedValues, nodeSet.getValues());
        assertEquals(expectedValues, nodeSet.getNodes());
    }

    /**
     * Test that pointers can be removed from a BasicNodeSet.
     * This verifies that after removal:
     * - The removed pointer is no longer in the set
     * - Other pointers remain unchanged
     * - Values and nodes are updated accordingly
     */
    @Test
    void testRemove_shouldRemoveSpecificPointerAndUpdateCollections() {
        // Given: A nodeSet with four integer pointers
        addPointersFromPath("/bean/integers");
        
        // When: Removing the fourth element
        removePointersFromPath("/bean/integers[4]");
        
        // Then: Only three elements should remain
        List<String> expectedPaths = Arrays.asList(
            "/bean/integers[1]", 
            "/bean/integers[2]", 
            "/bean/integers[3]"
        );
        List<Integer> expectedValues = Arrays.asList(1, 2, 3);
        
        assertPointerPaths(expectedPaths, nodeSet.getPointers());
        assertEquals(expectedValues, nodeSet.getValues());
        assertEquals(expectedValues, nodeSet.getNodes());
    }

    /**
     * Test the distinction between nodes and values in XML models.
     * For XML elements:
     * - Values are the text content of the elements
     * - Nodes are the actual DOM Element objects
     * This test verifies both are retrieved correctly.
     */
    @Test
    void testNodes_xmlModelShouldDistinguishBetweenNodesAndValues() {
        // Given: A path to XML contact elements
        String contactsPath = "/document/vendor/contact";
        
        // When: Adding pointers from the XML document
        addPointersFromPath(contactsPath);
        
        // Then: Verify pointer paths
        List<String> expectedPaths = Arrays.asList(
            "/document/vendor[1]/contact[1]",
            "/document/vendor[1]/contact[2]",
            "/document/vendor[1]/contact[3]",
            "/document/vendor[1]/contact[4]"
        );
        assertPointerPaths(expectedPaths, nodeSet.getPointers());
        
        // And: Values should be the text content
        List<String> expectedValues = Arrays.asList("John", "Jack", "Jim", "Jack Black");
        assertEquals(expectedValues, nodeSet.getValues());
        
        // And: Nodes should be DOM elements with correct tag names and values
        List<String> expectedTagNames = Arrays.asList("contact", "contact", "contact", "contact");
        assertDomElementsHaveTagNames(expectedTagNames, nodeSet.getNodes());
        assertDomElementsHaveTextValues(expectedValues, nodeSet.getNodes());
    }

    // Helper methods with descriptive names

    /**
     * Adds all pointers found at the given XPath to the nodeSet.
     * Also triggers cache refresh to ensure consistent state.
     */
    private void addPointersFromPath(String xpath) {
        Iterator<Pointer> pointers = context.iteratePointers(xpath);
        while (pointers.hasNext()) {
            nodeSet.add(pointers.next());
        }
        refreshNodeSetCaches();
    }

    /**
     * Removes all pointers found at the given XPath from the nodeSet.
     * Also triggers cache refresh to ensure consistent state.
     */
    private void removePointersFromPath(String xpath) {
        Iterator<Pointer> pointers = context.iteratePointers(xpath);
        while (pointers.hasNext()) {
            nodeSet.remove(pointers.next());
        }
        refreshNodeSetCaches();
    }

    /**
     * Forces the nodeSet to refresh its internal caches by calling all getter methods.
     * This ensures that the nodeSet's state is consistent after modifications.
     */
    private void refreshNodeSetCaches() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Asserts that the pointer paths match the expected paths.
     * Converts pointers to their string representation for comparison.
     */
    private void assertPointerPaths(List<String> expectedPaths, List<Pointer> actualPointers) {
        assertEquals(expectedPaths.toString(), actualPointers.toString());
    }

    /**
     * Asserts that DOM elements have the expected tag names.
     * 
     * @param expectedTagNames List of expected tag names in order
     * @param elements List of DOM elements to check
     */
    private void assertDomElementsHaveTagNames(List<String> expectedTagNames, List elements) {
        assertEquals(expectedTagNames.size(), elements.size(), 
            "Number of elements should match expected tag names");
        
        Iterator<String> nameIter = expectedTagNames.iterator();
        Iterator elementIter = elements.iterator();
        
        while (elementIter.hasNext()) {
            Element element = (Element) elementIter.next();
            String expectedName = nameIter.next();
            assertEquals(expectedName, element.getTagName(), 
                "Element tag name should match expected");
        }
    }

    /**
     * Asserts that DOM elements have the expected text values.
     * 
     * @param expectedValues List of expected text values in order
     * @param elements List of DOM elements to check
     */
    private void assertDomElementsHaveTextValues(List<String> expectedValues, List elements) {
        assertEquals(expectedValues.size(), elements.size(), 
            "Number of elements should match expected values");
        
        Iterator<String> valueIter = expectedValues.iterator();
        Iterator elementIter = elements.iterator();
        
        while (elementIter.hasNext()) {
            Element element = (Element) elementIter.next();
            String expectedValue = valueIter.next();
            String actualValue = element.getFirstChild().getNodeValue();
            assertEquals(expectedValue, actualValue, 
                "Element text content should match expected value");
        }
    }
}