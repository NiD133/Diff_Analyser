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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Tests for BasicNodeSet.
 *
 * These tests intentionally "nudge" the NodeSet after mutations to exercise
 * all cache-returning methods (pointers/values/nodes) and verify consistency.
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    private static final String PATH_BEAN_INTEGERS = "/bean/integers";
    private static final String PATH_BEAN_INTEGERS_4 = "/bean/integers[4]";
    private static final String PATH_VENDOR1_CONTACT = "/document/vendor[1]/contact";

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
     * Verifies that adding a group of pointers for a bean property results in
     * correct pointer paths, values and nodes.
     */
    @Test
    void testAdd() {
        addPointersFor(PATH_BEAN_INTEGERS);

        assertAll(
            () -> assertEquals(
                indexedPaths(PATH_BEAN_INTEGERS, 1, 2, 3, 4),
                pointerStrings(),
                "Pointer paths should match"
            ),
            () -> assertEquals(
                list(1, 2, 3, 4),
                nodeSet.getValues(),
                "Values should be the integer list"
            ),
            () -> assertEquals(
                list(1, 2, 3, 4),
                nodeSet.getNodes(),
                "Nodes should equal values for bean model"
            )
        );
    }

    /**
     * Demonstrates that nodes can differ from values in XML models:
     * - values are element text contents
     * - nodes are the actual DOM elements
     */
    @Test
    void testNodesInXmlModel() {
        addPointersFor("/document/vendor/contact");

        assertAll(
            () -> assertEquals(
                indexedPaths(PATH_VENDOR1_CONTACT, 1, 2, 3, 4),
                pointerStrings(),
                "Pointer paths for vendor[1]/contact[*] should match"
            ),
            () -> assertEquals(
                list("John", "Jack", "Jim", "Jack Black"),
                nodeSet.getValues(),
                "Values should be element text"
            ),
            () -> {
                final List<Element> elements = asElements(nodeSet.getNodes());
                assertElementNames(list("contact", "contact", "contact", "contact"), elements);
                assertElementValues(list("John", "Jack", "Jim", "Jack Black"), elements);
            }
        );
    }

    /**
     * Verifies removing a single pointer updates pointers, values and nodes consistently.
     */
    @Test
    void testRemoveSinglePointer() {
        addPointersFor(PATH_BEAN_INTEGERS);
        removePointersFor(PATH_BEAN_INTEGERS_4);

        assertAll(
            () -> assertEquals(
                indexedPaths(PATH_BEAN_INTEGERS, 1, 2, 3),
                pointerStrings(),
                "Pointer paths after removal should match"
            ),
            () -> assertEquals(
                list(1, 2, 3),
                nodeSet.getValues(),
                "Values should reflect removal"
            ),
            () -> assertEquals(
                list(1, 2, 3),
                nodeSet.getNodes(),
                "Nodes should reflect removal (bean model)"
            )
        );
    }

    // ---------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------

    /**
     * Add all pointers matching the supplied XPath to the NodeSet.
     */
    private void addPointersFor(final String xpath) {
        for (final Iterator<Pointer> it = context.iteratePointers(xpath); it.hasNext();) {
            nodeSet.add(it.next());
        }
        // Force cache updates in BasicNodeSet
        nudge();
    }

    /**
     * Remove all pointers matching the supplied XPath from the NodeSet.
     */
    private void removePointersFor(final String xpath) {
        for (final Iterator<Pointer> it = context.iteratePointers(xpath); it.hasNext();) {
            nodeSet.remove(it.next());
        }
        // Force cache updates in BasicNodeSet
        nudge();
    }

    /**
     * Access all cache-producing methods to ensure internal state is consistent.
     */
    private void nudge() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Collects current pointer representations as strings for comparison.
     */
    private List<String> pointerStrings() {
        return nodeSet.getPointers()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * Builds a list of indexed XPath strings: base + "[i]" for each index.
     */
    private static List<String> indexedPaths(final String base, final int... indices) {
        return Arrays.stream(indices)
                .mapToObj(i -> base + "[" + i + "]")
                .collect(Collectors.toList());
    }

    /**
     * Typed assertion: verify tag names for the provided elements.
     */
    private static void assertElementNames(final List<String> expectedNames, final List<Element> elements) {
        assertEquals(expectedNames.size(), elements.size(), "Element count should match");
        for (int i = 0; i < elements.size(); i++) {
            assertEquals(expectedNames.get(i), elements.get(i).getTagName(), "Element name at index " + i);
        }
    }

    /**
     * Typed assertion: verify text contents for the provided elements.
     */
    private static void assertElementValues(final List<String> expectedValues, final List<Element> elements) {
        assertEquals(expectedValues.size(), elements.size(), "Element count should match");
        for (int i = 0; i < elements.size(); i++) {
            assertEquals(expectedValues.get(i),
                    elements.get(i).getFirstChild().getNodeValue(),
                    "Element value at index " + i);
        }
    }

    /**
     * Casts a raw nodes list to a list of DOM Elements (used in XML model tests).
     */
    @SuppressWarnings("unchecked")
    private static List<Element> asElements(final List<?> nodes) {
        return (List<Element>) (List<?>) nodes;
    }
}