/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,

 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

/**
 * Tests for {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    // A simple tree node class for testing graph traversal.
    private static class Node {
        private final String value;
        private final List<Node> children = new ArrayList<>();

        Node(final String value) {
            this.value = value;
        }

        Node addChild(final Node child) {
            children.add(child);
            return this;
        }

        Iterator<Node> getChildrenIterator() {
            return children.iterator();
        }

        @Override
        public String toString() {
            return "Node[" + value + "]";
        }
    }

    /**
     * Tests that the iterator correctly traverses a simple object graph,
     * extracting leaf nodes as defined by the transformer.
     */
    @Test
    public void shouldTraverseObjectGraphAndExtractLeafNodes() {
        // Arrange
        final Node root = new Node("root");
        final Node child1 = new Node("child1");
        final Node child2 = new Node("child2_leaf");
        final Node grandchild1 = new Node("grandchild1_leaf");

        root.addChild(child1.addChild(grandchild1)).addChild(child2);

        // This transformer explores nodes with children, and returns leaf nodes as elements.
        final Transformer<Object, Object> nodeExplorer = input -> {
            if (input instanceof Node) {
                final Node node = (Node) input;
                if (node.children.isEmpty()) {
                    return node; // This is a leaf, return it as a value.
                }
                return node.getChildrenIterator(); // This is a branch, return an iterator over its children.
            }
            return input;
        };

        final ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(root, nodeExplorer);

        // Act
        final List<Object> foundNodes = new ArrayList<>();
        iterator.forEachRemaining(foundNodes::add);

        // Assert
        // The iterator should perform a depth-first traversal and collect the leaves.
        assertEquals(2, foundNodes.size());
        assertTrue("Expected grandchild1_leaf to be found", foundNodes.contains(grandchild1));
        assertTrue("Expected child2_leaf to be found", foundNodes.contains(child2));
    }

    /**
     * Tests basic iteration over a pre-existing iterator (e.g., a list iterator).
     */
    @Test
    public void shouldIterateOverElementsOfRootIterator() {
        // Arrange
        final List<String> list = Arrays.asList("A", "B", "C");
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(list.iterator());

        // Act & Assert
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests that an iterator with a single root object and no transformer
     * returns only that root object.
     */
    @Test
    public void iteratorWithSingleRootShouldYieldOnlyRoot() {
        // Arrange
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>("root", null);

        // Act & Assert
        assertTrue(iterator.hasNext());
        assertEquals("root", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests that hasNext() returns false when the root iterator is empty.
     */
    @Test
    public void hasNextShouldReturnFalseForEmptyRootIterator() {
        // Arrange
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(Collections.emptyIterator());

        // Assert
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests that hasNext() returns false when the root iterator is null.
     */
    @Test
    public void hasNextShouldReturnFalseForNullRootIterator() {
        // Arrange
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>((Iterator<String>) null);

        // Assert
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests that next() throws NoSuchElementException when the iteration is finished.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowExceptionWhenIterationIsFinished() {
        // Arrange
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(Collections.emptyIterator());

        // Act
        iterator.next(); // Should throw
    }

    /**
     * Tests that the remove() operation works correctly on the underlying iterator.
     */
    @Test
    public void removeShouldModifyUnderlyingCollection() {
        // Arrange
        final List<String> list = new ArrayList<>(Arrays.asList("A", "B"));
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(list.iterator());

        // Act
        iterator.next(); // Consume "A"
        iterator.remove();

        // Assert
        assertEquals("List should have one element after remove", 1, list.size());
        assertEquals("The remaining element should be 'B'", "B", list.get(0));
    }

    /**
     * Tests that remove() throws IllegalStateException if next() has not been called.
     */
    @Test(expected = IllegalStateException.class)
    public void removeShouldThrowExceptionBeforeFirstNext() {
        // Arrange
        final List<String> list = new ArrayList<>(Arrays.asList("A", "B"));
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(list.iterator());

        // Act
        iterator.remove(); // Should throw
    }

    /**
     * Tests that remove() throws IllegalStateException if the element returned by next()
     * was a root object, not an element from an underlying iterator.
     */
    @Test(expected = IllegalStateException.class)
    public void removeShouldThrowExceptionForRootElement() {
        // Arrange
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>("root", null);

        // Act
        iterator.next(); // Returns the root object
        iterator.remove(); // Should throw, as root is not from a removable collection
    }

    /**
     * Tests that the iterator fails fast with a ConcurrentModificationException
     * if the underlying collection is modified during iteration.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void nextShouldThrowCMEWhenUnderlyingCollectionIsModified() {
        // Arrange
        final List<String> list = new ArrayList<>(Arrays.asList("A"));
        final Iterator<String> listIterator = list.iterator();
        final ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(listIterator);

        // Act
        graphIterator.next(); // Consume "A"
        list.add("B"); // Modify the list after creating the iterator
        graphIterator.next(); // This should throw
    }

    /**
     * Tests that the iterator throws an exception if the provided transformer fails.
     */
    @Test
    public void shouldThrowExceptionWhenTransformerFails() {
        // Arrange
        final Transformer<String, String> failingTransformer = ExceptionTransformer.exceptionTransformer();
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>("root", failingTransformer);

        // Act
        assertNotNull(iterator.next()); // First next() returns the root
        try {
            iterator.hasNext(); // This will apply the failing transformer to "root"
            fail("Expected a RuntimeException from the failing transformer");
        } catch (final RuntimeException e) {
            // Assert
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}