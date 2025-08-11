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
package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the
 * {@link CompositeSet} implementation.
 */
public class CompositeSetTest<E> extends AbstractSetTest<E> {

    // Test data constants for better readability
    private static final String ELEMENT_1 = "1";
    private static final String ELEMENT_2 = "2";
    private static final String ELEMENT_3 = "3";
    private static final String ELEMENT_4 = "4";

    /**
     * Creates a test set containing elements "1" and "2".
     * @return HashSet with elements "1" and "2"
     */
    @SuppressWarnings("unchecked")
    public Set<E> buildSetWithElements1And2() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) ELEMENT_1);
        set.add((E) ELEMENT_2);
        return set;
    }

    /**
     * Creates a test set containing elements "3" and "4".
     * @return HashSet with elements "3" and "4"
     */
    @SuppressWarnings("unchecked")
    public Set<E> buildSetWithElements3And4() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) ELEMENT_3);
        set.add((E) ELEMENT_4);
        return set;
    }

    // Legacy method names for backward compatibility
    public Set<E> buildOne() {
        return buildSetWithElements1And2();
    }

    public Set<E> buildTwo() {
        return buildSetWithElements3And4();
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> set = new CompositeSet<>(contained);
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithValidSets_ShouldCombineAllElements() {
        // Given: Two separate sets with different elements
        final Set<E> setWith1And2 = buildSetWithElements1And2();
        final Set<E> setWith3And4 = buildSetWithElements3And4();
        
        // When: Adding both sets to a composite set
        final CompositeSet<E> compositeSet = new CompositeSet<>();
        compositeSet.addComposited(setWith1And2, setWith3And4);
        
        // Then: Composite should contain all elements from both sets
        assertEquals(4, compositeSet.size());
        assertTrue(compositeSet.contains(ELEMENT_1));
        assertTrue(compositeSet.contains(ELEMENT_2));
        assertTrue(compositeSet.contains(ELEMENT_3));
        assertTrue(compositeSet.contains(ELEMENT_4));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithNullValues_ShouldHandleGracefully() {
        // Given: A composite set
        final CompositeSet<E> compositeSet = new CompositeSet<>();
        
        // When & Then: Adding null values should not cause exceptions
        compositeSet.addComposited((Set<E>) null);
        compositeSet.addComposited((Set<E>[]) null);
        compositeSet.addComposited(null, null);
        compositeSet.addComposited(null, null, null);
        
        assertTrue(compositeSet.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithDifferentConstructorApproaches_ShouldProduceSameResult() {
        // Given: Sets with test data
        final Set<E> setWith1And2 = buildSetWithElements1And2();
        final Set<E> setWith3And4 = buildSetWithElements3And4();
        
        // When: Creating composite sets using different approaches
        final CompositeSet<E> compositeSet1 = new CompositeSet<>();
        compositeSet1.addComposited(setWith1And2, setWith3And4);
        
        final CompositeSet<E> compositeSet2 = new CompositeSet<>(setWith1And2);
        compositeSet2.addComposited(setWith3And4);
        
        // Then: Both approaches should produce equivalent results
        assertEquals(compositeSet1, compositeSet2);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithOverlappingElements_ShouldThrowException() {
        // Given: A set with elements 1, 2, 3
        final HashSet<E> existingSet = new HashSet<>();
        existingSet.add((E) ELEMENT_1);
        existingSet.add((E) ELEMENT_2);
        existingSet.add((E) ELEMENT_3);
        
        final CompositeSet<E> compositeSet = new CompositeSet<>(existingSet);
        
        // When & Then: Adding a set with overlapping elements should throw exception
        final Set<E> overlappingSet = buildSetWithElements1And2(); // Contains "1" and "2"
        
        assertThrows(UnsupportedOperationException.class, 
            () -> compositeSet.addComposited(overlappingSet),
            "Should throw UnsupportedOperationException when adding overlapping elements without mutator");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithMultipleOverlappingSets_ShouldThrowException() {
        // Given: A composite set with existing elements
        final HashSet<E> existingSet = new HashSet<>();
        existingSet.add((E) ELEMENT_1);
        existingSet.add((E) ELEMENT_2);
        existingSet.add((E) ELEMENT_3);
        
        final CompositeSet<E> compositeSet = new CompositeSet<>(existingSet);
        
        // When & Then: Adding multiple sets with overlapping elements should throw exception
        final Set<E> overlappingSet1 = buildSetWithElements1And2();
        final Set<E> overlappingSet2 = buildSetWithElements3And4();
        
        assertThrows(UnsupportedOperationException.class,
            () -> compositeSet.addComposited(existingSet, overlappingSet1),
            "Should throw exception when adding two overlapping sets");
            
        assertThrows(UnsupportedOperationException.class,
            () -> compositeSet.addComposited(existingSet, overlappingSet1, overlappingSet2),
            "Should throw exception when adding three overlapping sets");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testContains_WithElementInCompositeSet_ShouldReturnTrue() {
        // Given: A composite set containing multiple underlying sets
        final CompositeSet<E> compositeSet = new CompositeSet<>(
            buildSetWithElements1And2(), 
            buildSetWithElements3And4()
        );
        
        // When & Then: Should find elements from any underlying set
        assertTrue(compositeSet.contains(ELEMENT_1));
        assertTrue(compositeSet.contains(ELEMENT_3));
        assertFalse(compositeSet.contains("nonexistent"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testContainsAll_WithNullCollection_ShouldReturnFalse() {
        // Given: A composite set with elements
        final CompositeSet<E> compositeSet = new CompositeSet<>(
            buildSetWithElements1And2(), 
            buildSetWithElements3And4()
        );
        
        // When & Then: containsAll with null should return false
        assertFalse(compositeSet.containsAll(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCollisionResolution_WhenResolutionFails_ShouldThrowException() {
        // Given: A composite set with a mutator that doesn't resolve collisions
        final Set<E> setWith1And2 = buildSetWithElements1And2();
        final Set<E> setWith3And4 = buildSetWithElements3And4();
        final CompositeSet<E> compositeSet = new CompositeSet<>(setWith1And2, setWith3And4);
        
        // Set up a mutator that fails to resolve collisions
        compositeSet.setMutator(createFailingMutator());

        // When & Then: Adding a set with overlapping elements should throw IllegalArgumentException
        final HashSet<E> overlappingSet = new HashSet<>();
        overlappingSet.add((E) ELEMENT_1); // This overlaps with existing elements
        
        assertThrows(IllegalArgumentException.class, 
            () -> compositeSet.addComposited(overlappingSet),
            "Should throw IllegalArgumentException when collision resolution fails");
    }

    /**
     * Creates a SetMutator that throws UnsupportedOperationException for add operations
     * and does nothing for collision resolution (causing collisions to remain unresolved).
     */
    private SetMutator<E> createFailingMutator() {
        return new SetMutator<E>() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean add(final CompositeSet<E> composite,
                    final List<Set<E>> collections, final E obj) {
                throw new UnsupportedOperationException("Add operation not supported");
            }

            @Override
            public boolean addAll(final CompositeSet<E> composite,
                    final List<Set<E>> collections, final Collection<? extends E> coll) {
                throw new UnsupportedOperationException("AddAll operation not supported");
            }

            @Override
            public void resolveCollision(final CompositeSet<E> comp, final Set<E> existing,
                final Set<E> added, final Collection<E> intersects) {
                // Intentionally do nothing - this causes collision resolution to fail
            }
        };
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRemoveAll_WithNullCollection_ShouldReturnFalse() {
        // Given: A composite set with elements
        final CompositeSet<E> compositeSet = new CompositeSet<>(
            buildSetWithElements1And2(), 
            buildSetWithElements3And4()
        );
        
        // When & Then: removeAll with null should return false
        assertFalse(compositeSet.removeAll(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRemove_FromCompositeSet_ShouldRemoveFromUnderlyingSet() {
        // Given: Composite set with underlying sets
        final Set<E> setWith1And2 = buildSetWithElements1And2();
        final Set<E> setWith3And4 = buildSetWithElements3And4();
        final CompositeSet<E> compositeSet = new CompositeSet<>(setWith1And2, setWith3And4);
        
        // When: Removing elements from composite set
        compositeSet.remove(ELEMENT_1);
        compositeSet.remove(ELEMENT_3);
        
        // Then: Elements should be removed from underlying sets
        assertFalse(setWith1And2.contains(ELEMENT_1));
        assertFalse(setWith3And4.contains(ELEMENT_3));
        assertFalse(compositeSet.contains(ELEMENT_1));
        assertFalse(compositeSet.contains(ELEMENT_3));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRemove_FromUnderlyingSet_ShouldReflectInCompositeSet() {
        // Given: Composite set with underlying sets
        final Set<E> setWith1And2 = buildSetWithElements1And2();
        final Set<E> setWith3And4 = buildSetWithElements3And4();
        final CompositeSet<E> compositeSet = new CompositeSet<>(setWith1And2, setWith3And4);
        
        // When: Removing elements directly from underlying sets
        setWith1And2.remove(ELEMENT_1);
        setWith3And4.remove(ELEMENT_3);
        
        // Then: Changes should be reflected in composite set
        assertFalse(compositeSet.contains(ELEMENT_1));
        assertFalse(compositeSet.contains(ELEMENT_3));
    }

//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/CompositeSet.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/CompositeSet.fullCollection.version4.obj");
//    }
}