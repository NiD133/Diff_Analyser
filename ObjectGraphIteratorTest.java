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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for ObjectGraphIterator, which traverses object graphs or flattens nested iterators.
 */
class ObjectGraphIteratorTest {

    //<editor-fold desc="Test Fixture: Domain objects for graph traversal">
    static class Forest {
        final List<Tree> trees = new ArrayList<>();
        Tree addTree() {
            final Tree tree = new Tree();
            trees.add(tree);
            return tree;
        }
        Iterator<Tree> treeIterator() {
            return trees.iterator();
        }
    }

    static class Tree {
        final List<Branch> branches = new ArrayList<>();
        Branch addBranch() {
            final Branch branch = new Branch();
            branches.add(branch);
            return branch;
        }
        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
    }

    static class Branch {
        final List<Leaf> leaves = new ArrayList<>();
        Leaf addLeaf() {
            final Leaf leaf = new Leaf();
            leaves.add(leaf);
            return leaf;
        }
        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    static class Leaf {
        // A terminal node in the graph.
    }

    /**
     * A Transformer that navigates the Forest -> Tree -> Branch -> Leaf object graph.
     */
    static class LeafFinder implements Transformer<Object, Object> {
        @Override
        public Object transform(final Object input) {
            if (input instanceof Forest) {
                return ((Forest) input).treeIterator();
            }
            if (input instanceof Tree) {
                return ((Tree) input).branchIterator();
            }
            if (input instanceof Branch) {
                return ((Branch) input).leafIterator();
            }
            if (input instanceof Leaf) {
                return input; // Return the leaf itself as a terminal object
            }
            throw new ClassCastException("Unsupported object type: " + input.getClass().getName());
        }
    }
    //</editor-fold>

    @Nested
    @DisplayName("When traversing an object graph")
    class GraphTraversalTests {

        @Test
        @DisplayName("should return only the root object when no transformer is provided")
        void shouldReturnRootWhenNoTransformerIsProvided() {
            // Arrange
            final Forest forest = new Forest();
            forest.addTree().addBranch().addLeaf();
            final Iterator<Object> iterator = new ObjectGraphIterator<>(forest, null);

            // Act & Assert
            assertTrue(iterator.hasNext());
            assertSame(forest, iterator.next());
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        @DisplayName("should find a single leaf in a simple graph")
        void shouldFindSingleLeafInSimpleGraph() {
            // Arrange
            final Forest forest = new Forest();
            final Leaf expectedLeaf = forest.addTree().addBranch().addLeaf();
            final Iterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());

            // Act
            final List<Object> result = IteratorUtils.toList(iterator);

            // Assert
            assertIterableEquals(List.of(expectedLeaf), result);
        }

        @Test
        @DisplayName("should find all leaves in a complex graph with empty branches/trees")
        void shouldFindAllLeavesInComplexGraph() {
            // Arrange
            final Forest forest = new Forest();
            final Tree tree1 = forest.addTree();
            forest.addTree(); // Empty tree, should be skipped
            final Tree tree3 = forest.addTree();

            final Branch branch1_1 = tree1.addBranch();
            final Branch branch1_2 = tree1.addBranch();

            final Branch branch3_1 = tree3.addBranch();
            tree3.addBranch(); // Empty branch, should be skipped
            final Branch branch3_3 = tree3.addBranch();

            final Leaf leaf1 = branch1_1.addLeaf();
            final Leaf leaf2 = branch1_1.addLeaf();
            final Leaf leaf3 = branch1_2.addLeaf();
            final Leaf leaf4 = branch3_1.addLeaf();
            final Leaf leaf5 = branch3_3.addLeaf();

            final List<Leaf> expectedLeaves = List.of(leaf1, leaf2, leaf3, leaf4, leaf5);
            final Iterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());

            // Act
            final List<Object> actualLeaves = IteratorUtils.toList(iterator);

            // Assert
            assertIterableEquals(expectedLeaves, actualLeaves);
        }
    }

    @Nested
    @DisplayName("When flattening nested iterators")
    class FlatteningIteratorTests {

        @Test
        @DisplayName("should iterate over all elements from a list of iterators")
        void shouldIterateAllElements() {
            // Arrange
            final List<String> list1 = List.of("One", "Two");
            final List<String> list2 = List.of("Three");
            final List<String> list3 = List.of("Four", "Five");
            final List<Iterator<String>> iterators = List.of(list1.iterator(), list2.iterator(), list3.iterator());
            final List<String> expected = List.of("One", "Two", "Three", "Four", "Five");

            final Iterator<String> iterator = new ObjectGraphIterator<>(iterators.iterator());

            // Act
            final List<String> actual = IteratorUtils.toList(iterator);

            // Assert
            assertIterableEquals(expected, actual);
        }

        @Test
        @DisplayName("should correctly skip empty inner iterators")
        void shouldSkipEmptyIterators() {
            // Arrange
            final List<String> list1 = List.of("One", "Two");
            final List<String> list2 = List.of("Three");
            final List<Iterator<String>> iterators = List.of(
                Collections.emptyIterator(),
                list1.iterator(),
                Collections.emptyIterator(),
                list2.iterator(),
                Collections.emptyIterator()
            );
            final List<String> expected = List.of("One", "Two", "Three");

            final Iterator<String> iterator = new ObjectGraphIterator<>(iterators.iterator());

            // Act
            final List<String> actual = IteratorUtils.toList(iterator);

            // Assert
            assertIterableEquals(expected, actual);
        }

        @Test
        @DisplayName("should allow calling next() without hasNext()")
        void shouldAllowCallingNextWithoutHasNext() {
            // Arrange
            final List<String> list1 = List.of("One", "Two");
            final List<String> list2 = List.of("Three");
            final List<Iterator<String>> iterators = List.of(list1.iterator(), list2.iterator());
            final Iterator<String> iterator = new ObjectGraphIterator<>(iterators.iterator());

            // Act & Assert
            assertEquals("One", iterator.next());
            assertEquals("Two", iterator.next());
            assertEquals("Three", iterator.next());
            assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        @DisplayName("should remove elements from the correct underlying iterator")
        void shouldRemoveElementsFromUnderlyingIterator() {
            // Arrange
            final List<String> list1 = new ArrayList<>(List.of("One", "Two"));
            final List<String> list2 = new ArrayList<>(List.of("Three"));
            final List<Iterator<String>> iterators = List.of(list1.iterator(), list2.iterator());
            final Iterator<String> iterator = new ObjectGraphIterator<>(iterators.iterator());

            // Act
            iterator.next(); // "One"
            iterator.remove(); // Removes "One" from list1

            iterator.next(); // "Two"
            iterator.next(); // "Three"
            iterator.remove(); // Removes "Three" from list2

            // Assert
            assertIterableEquals(List.of("Two"), list1);
            assertTrue(list2.isEmpty());
            assertFalse(iterator.hasNext());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("should behave as an empty iterator when the root is null")
        void shouldBeEmptyWhenRootIsNull() {
            // Arrange
            final Iterator<Object> iterator = new ObjectGraphIterator<>(null, null);

            // Act & Assert
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        @DisplayName("should throw IllegalStateException on remove() if root is null")
        void shouldThrowFromRemoveWhenRootIsNull() {
            // Arrange
            final Iterator<Object> iterator = new ObjectGraphIterator<>(null);

            // Act & Assert
            assertThrows(IllegalStateException.class, iterator::remove,
                "Remove should throw if next() has not been called.");
        }

        @Test
        @DisplayName("should behave as an empty iterator when the root iterator is empty")
        void shouldBeEmptyWhenRootIteratorIsEmpty() {
            // Arrange
            final Iterator<Object> iterator = new ObjectGraphIterator<>(Collections.emptyIterator());

            // Act & Assert
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::next);
        }
    }
}