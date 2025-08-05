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

    @SuppressWarnings("unchecked")
    public Set<E> buildSetOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildSetTwo() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "3");
        set.add((E) "4");
        return set;
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

    // Tests for addComposited()
    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithMultipleSets() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> composite = new CompositeSet<>();
        composite.addComposited(setOne, setTwo);
        assertEquals(4, composite.size(), "Composite should contain all elements from both sets");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WithNullArguments() {
        final CompositeSet<E> composite = new CompositeSet<>();
        composite.addComposited((Set<E>[]) null);
        composite.addComposited(null, null);
        composite.addComposited(null, null, null);
        assertTrue(composite.isEmpty(), "Adding null sets should be ignored");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_AfterInitialization() {
        final CompositeSet<E> composite = new CompositeSet<>(buildSetOne());
        composite.addComposited(buildSetTwo());
        assertEquals(4, composite.size(), "Composite should contain elements from both sets");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WhenSetAlreadyAdded_ThrowsException() {
        final Set<E> setOne = buildSetOne();
        final CompositeSet<E> composite = new CompositeSet<>(setOne);
        assertThrows(UnsupportedOperationException.class, () -> composite.addComposited(setOne),
                "Cannot add the same set instance multiple times");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WhenCollisionNotResolved_ThrowsException() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> composite = new CompositeSet<>(setOne, setTwo);

        // Create a new set with overlapping element "1"
        final HashSet<E> setWithCollision = new HashSet<>();
        setWithCollision.add((E) "1");

        assertThrows(IllegalArgumentException.class, () -> composite.addComposited(setWithCollision),
                "Should throw when mutator fails to resolve collision");
    }

    // Tests for contains()
    @Test
    @SuppressWarnings("unchecked")
    void testContains_WhenElementPresent_ReturnsTrue() {
        final CompositeSet<E> composite = new CompositeSet<>(buildSetOne(), buildSetTwo());
        assertTrue(composite.contains("1"), "Should find element in first set");
        assertTrue(composite.contains("3"), "Should find element in second set");
    }

    // Tests for containsAll()
    @Test
    void testContainsAll_WithNullCollection_ReturnsFalse() {
        final CompositeSet<E> composite = new CompositeSet<>();
        assertFalse(composite.containsAll(null), "Null collection should always return false");
    }

    // Tests for remove()
    @Test
    @SuppressWarnings("unchecked")
    void testRemove_RemovesFromUnderlyingSet() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> composite = new CompositeSet<>(setOne, setTwo);

        composite.remove("1");
        assertFalse(setOne.contains("1"), "Element should be removed from underlying set");

        composite.remove("3");
        assertFalse(setTwo.contains("3"), "Element should be removed from underlying set");
    }

    // Tests for underlying set modifications
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveFromUnderlyingSet_ReflectedInComposite() {
        final Set<E> setOne = buildSetOne();
        final Set<E> setTwo = buildSetTwo();
        final CompositeSet<E> composite = new CompositeSet<>(setOne, setTwo);

        setOne.remove("1");
        assertFalse(composite.contains("1"), "Composite should reflect removal from first set");

        setTwo.remove("3");
        assertFalse(composite.contains("3"), "Composite should reflect removal from second set");
    }

    // Tests for removeAll()
    @Test
    void testRemoveAll_WithNullCollection_ReturnsFalse() {
        final CompositeSet<E> composite = new CompositeSet<>();
        assertFalse(composite.removeAll(null), "Null collection should always return false");
    }

    // Tests for collision resolution failure
    @Test
    @SuppressWarnings("unchecked")
    void testAddComposited_WhenMutatorDoesNotResolveCollision_ThrowsIllegalArgumentException() {
        final CompositeSet<E> composite = new CompositeSet<>();
        composite.setMutator(new SetMutator<E>() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean add(CompositeSet<E> composite, List<Set<E>> collections, E obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(CompositeSet<E> composite, List<Set<E>> collections, Collection<? extends E> coll) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void resolveCollision(CompositeSet<E> comp, Set<E> existing, Set<E> added, Collection<E> intersects) {
                // Mutator does not resolve collision
            }
        });

        final Set<E> setOne = buildSetOne();
        composite.addComposited(setOne);

        final HashSet<E> setWithCollision = new HashSet<>();
        setWithCollision.add((E) "1"); // Collides with element in setOne
        assertThrows(IllegalArgumentException.class, () -> composite.addComposited(setWithCollision),
                "Should throw when collision isn't resolved");
    }

    // Note: The commented testCreate method remains unchanged
}