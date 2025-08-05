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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the
 * {@link CompositeSet} implementation.
 */
@DisplayName("CompositeSet Test")
public class CompositeSetTest extends AbstractSetTest<String> {

    private Set<String> setA;
    private Set<String> setB;

    @BeforeEach
    public void setUp() {
        setA = createSetWith("1", "2");
        setB = createSetWith("3", "4");
    }

    private HashSet<String> createSetWith(final String... elements) {
        final HashSet<String> set = new HashSet<>();
        for (final String element : elements) {
            set.add(element);
        }
        return set;
    }

    //--- AbstractSetTest methods ---

    @Override
    public CompositeSet<String> makeObject() {
        // Required for AbstractSetTest, which needs an empty set that can be modified.
        final HashSet<String> contained = new HashSet<>();
        final CompositeSet<String> set = new CompositeSet<>(contained);
        // An empty mutator allows the abstract tests to add elements to the first set.
        set.setMutator(new EmptySetMutator<>(contained));
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

    //--- CompositeSet specific tests ---

    @Test
    @DisplayName("addComposited should combine multiple sets into a single view")
    public void addComposited_withMultipleSets_createsUnionView() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA);

        // Act
        compositeSet.addComposited(setB);

        // Assert
        assertEquals(4, compositeSet.size());
        assertTrue(compositeSet.containsAll(List.of("1", "2", "3", "4")));
    }

    @Test
    @DisplayName("addComposited should ignore null set arguments")
    public void addComposited_withNullArguments_isIgnored() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA);
        final int initialSize = compositeSet.size();

        // Act
        compositeSet.addComposited((Set<String>) null);
        compositeSet.addComposited((Set<String>[]) null);
        compositeSet.addComposited(null, null);

        // Assert
        assertEquals(initialSize, compositeSet.size());
        assertEquals(setA, compositeSet.toSet());
    }

    @Test
    @DisplayName("addComposited should throw UnsupportedOperationException for overlapping sets when no mutator is present")
    public void addComposited_withOverlappingSetsAndNoMutator_throwsException() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA); // Contains "1", "2"
        final Set<String> overlappingSet = createSetWith("2", "3"); // "2" is the collision

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(overlappingSet),
                "Adding a set with common elements should fail without a collision resolver.");
    }

    @Test
    @DisplayName("addComposited should throw IllegalArgumentException if the mutator fails to resolve a collision")
    public void addComposited_whenCollisionResolutionFails_throwsIllegalArgumentException() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA, setB);
        compositeSet.setMutator(new SetMutator<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean add(final CompositeSet<String> composite, final List<Set<String>> collections, final String obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(final CompositeSet<String> composite, final List<Set<String>> collections, final Collection<? extends String> coll) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void resolveCollision(final CompositeSet<String> comp, final Set<String> existing,
                final Set<String> added, final Collection<String> intersects) {
                // This resolver does nothing, so the collision persists.
            }
        });

        final Set<String> overlappingSet = createSetWith("1", "5"); // "1" is the collision

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> compositeSet.addComposited(overlappingSet),
                "Should throw because the no-op resolver did not resolve the collision.");
    }

    @Test
    @DisplayName("remove on composite set should remove the element from the correct underlying set")
    public void remove_fromCompositeSet_removesFromUnderlyingSet() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA, setB);
        assertTrue(setA.contains("1"), "Precondition: setA should contain '1'");
        assertTrue(compositeSet.contains("1"), "Precondition: compositeSet should contain '1'");

        // Act
        final boolean removed = compositeSet.remove("1");

        // Assert
        assertTrue(removed);
        assertFalse(setA.contains("1"), "Element '1' should be removed from the underlying setA.");
        assertFalse(compositeSet.contains("1"), "Element '1' should be removed from the composite set.");
    }

    @Test
    @DisplayName("remove on an underlying set should be reflected in the composite set")
    public void remove_fromUnderlyingSet_isReflectedInCompositeSet() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA, setB);
        assertTrue(compositeSet.contains("1"), "Precondition: compositeSet should contain '1'");

        // Act
        setA.remove("1");

        // Assert
        assertFalse(compositeSet.contains("1"), "Change in underlying setA should be reflected in composite set.");
    }

    @Test
    @DisplayName("containsAll with a null collection should throw NullPointerException")
    public void containsAll_withNullCollection_throwsNullPointerException() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA, setB);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> compositeSet.containsAll(null));
    }

    @Test
    @DisplayName("removeAll with a null collection should throw NullPointerException")
    public void removeAll_withNullCollection_throwsNullPointerException() {
        // Arrange
        final CompositeSet<String> compositeSet = new CompositeSet<>(setA, setB);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> compositeSet.removeAll(null));
    }
}