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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Tests for the {@link BasicNodeSet} class, verifying its behavior as a
 * collection of JXPath {@link Pointer} objects.
 */
@DisplayName("BasicNodeSet")
class BasicNodeSetTest extends AbstractJXPathTest {

    private JXPathContext context;
    private BasicNodeSet nodeSet;

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Tests that adding pointers correctly updates the node set's internal state,
     * including the derived lists of nodes and values.
     */
    @Test
    void testAddPointersUpdatesStateCorrectly() {
        // Arrange
        final String xpath = "/bean/integers";
        final List<String> expectedPointerStrings = List.of(
                "/bean/integers[1]",
                "/bean/integers[2]",
                "/bean/integers[3]",
                "/bean/integers[4]"
        );
        final List<Integer> expectedValues = List.of(1, 2, 3, 4);

        // Act
        addPointers(xpath);

        // Assert
        final List<String> actualPointerStrings = nodeSet.getPointers().stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        assertEquals(expectedPointerStrings, actualPointerStrings);
        assertEquals(expectedValues, nodeSet.getValues());
        assertEquals(expectedValues, nodeSet.getNodes());
    }

    /**
     * Tests that removing a pointer correctly updates the node set's internal state.
     */
    @Test
    void testRemovePointerUpdatesStateCorrectly() {
        // Arrange
        addPointers("/bean/integers"); // Add all integers first
        final String xpathToRemove = "/bean/integers[4]";
        final List<String> expectedPointerStrings = List.of(
                "/bean/integers[1]",
                "/bean/integers[2]",
                "/bean/integers[3]"
        );
        final List<Integer> expectedValues = List.of(1, 2, 3);

        // Act
        removePointers(xpathToRemove);

        // Assert
        final List<String> actualPointerStrings = nodeSet.getPointers().stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        assertEquals(expectedPointerStrings, actualPointerStrings);
        assertEquals(expectedValues, nodeSet.getValues());
        assertEquals(expectedValues, nodeSet.getNodes());
    }

    /**
     * Verifies that for XML documents, getNodes() returns DOM Elements,
     * while getValues() returns their text content. This demonstrates a key
     * distinction in how BasicNodeSet handles different data models.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testGetNodesForXmlReturnsElementsAndGetValuesReturnsTextContent() {
        // Arrange
        final String xpath = "/document/vendor/contact";
        addPointers(xpath);

        final List<String> expectedPointerStrings = List.of(
                "/document/vendor[1]/contact[1]",
                "/document/vendor[1]/contact[2]",
                "/document/vendor[1]/contact[3]",
                "/document/vendor[1]/contact[4]"
        );
        final List<String> expectedValues = List.of("John", "Jack", "Jim", "Jack Black");
        final List<String> expectedElementNames = List.of("contact", "contact", "contact", "contact");

        // Act
        // The SUT's getNodes() returns a raw List, requiring a cast.
        final List<Element> actualNodes = (List<Element>) nodeSet.getNodes();
        final List<String> actualPointerStrings = nodeSet.getPointers().stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        // Assert
        assertEquals(expectedPointerStrings, actualPointerStrings, "Pointers should match");
        assertEquals(expectedValues, nodeSet.getValues(), "Values should be the text content of elements");
        assertElementTagNames(expectedElementNames, actualNodes);
        assertElementTextContent(expectedValues, actualNodes);
    }

    // -----------------------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------------------

    /**
     * Finds all pointers matching the given XPath and adds them to the node set.
     *
     * @param xpath The XPath expression to evaluate.
     */
    private void addPointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext();) {
            nodeSet.add(iter.next());
        }
        // After modification, force the internal caches to be re-evaluated.
        updateInternalCaches();
    }

    /**
     * Finds all pointers matching the given XPath and removes them from the node set.
     *
     * @param xpath The XPath expression to evaluate.
     */
    private void removePointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext();) {
            nodeSet.remove(iter.next());
        }
        // After modification, force the internal caches to be re-evaluated.
        updateInternalCaches();
    }

    /**
     * Forces the re-computation of the lazily-initialized caches within the
     * BasicNodeSet. This is necessary after adding or removing pointers to
     * ensure that subsequent calls to getPointers(), getValues(), and getNodes()
     * reflect the changes.
     */
    private void updateInternalCaches() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Asserts that a list of DOM elements has the expected tag names.
     *
     * @param expectedTagNames The list of expected tag names.
     * @param actualElements   The list of DOM {@link Element}s to check.
     */
    private void assertElementTagNames(final List<String> expectedTagNames, final List<Element> actualElements) {
        final List<String> actualTagNames = actualElements.stream()
                .map(Element::getTagName)
                .collect(Collectors.toList());
        assertEquals(expectedTagNames, actualTagNames, "Element tag names should match");
    }

    /**
     * Asserts that a list of DOM elements has the expected text content.
     *
     * @param expectedText   The list of expected text values.
     * @param actualElements The list of DOM {@link Element}s to check.
     */
    private void assertElementTextContent(final List<String> expectedText, final List<Element> actualElements) {
        final List<String> actualText = actualElements.stream()
                .map(element -> element.getFirstChild().getNodeValue())
                .collect(Collectors.toList());
        assertEquals(expectedText, actualText, "Element text content should match");
    }
}