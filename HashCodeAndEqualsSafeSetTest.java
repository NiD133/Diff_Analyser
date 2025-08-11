/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 *
 * This suite verifies that the set correctly handles objects that throw exceptions
 * from their {@code hashCode()} or {@code equals()} methods, while still
 * conforming to the {@link java.util.Set} contract.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Rule public MockitoRule r = MockitoJUnit.rule();
    @Mock private UnmockableHashCodeAndEquals problematicMock;

    // --- Core "Safe" Functionality ---

    @Test
    public void should_add_and_contain_mock_with_failing_hashCode_and_equals() {
        // Arrange
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        // Act & Assert
        // Add should not throw despite the problematic hashCode()
        set.add(problematicMock);

        // Contains should not throw and should correctly identify the mock
        assertThat(set.contains(problematicMock)).isTrue();

        // Contains should return false for other mocks
        UnmockableHashCodeAndEquals otherMock = mock(UnmockableHashCodeAndEquals.class);
        assertThat(set.contains(otherMock)).isFalse();
    }

    @Test
    public void should_remove_mock_with_failing_hashCode_and_equals() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);
        assertThat(set.isEmpty()).isFalse();

        // Act
        boolean removed = set.remove(problematicMock);

        // Assert
        assertThat(removed).isTrue();
        assertThat(set).isEmpty();
    }

    // --- Standard Set Operations ---

    @Test
    public void clear_should_remove_all_elements() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act
        set.clear();

        // Assert
        assertThat(set).isEmpty();
    }

    @Test
    public void addAll_should_add_all_elements_from_collection() {
        // Arrange
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        List<Object> elementsToAdd = List.of(problematicMock, mock(Observer.class));

        // Act
        set.addAll(elementsToAdd);

        // Assert
        assertThat(set).containsAll(elementsToAdd);
    }

    @Test
    public void removeAll_should_remove_elements_from_given_collection() {
        // Arrange
        Object mockToKeep = mock(List.class);
        HashCodeAndEqualsSafeSet set =
                HashCodeAndEqualsSafeSet.of(problematicMock, mock(Observer.class), mockToKeep);
        List<Object> elementsToRemove = List.of(problematicMock, mock(Observer.class));

        // Act
        boolean changed = set.removeAll(elementsToRemove);

        // Assert
        assertThat(changed).isTrue();
        assertThat(set).containsExactly(mockToKeep);
    }

    @Test
    public void retainAll_should_keep_only_elements_from_given_collection() {
        // Arrange
        Object mockToKeep1 = problematicMock;
        Object mockToKeep2 = mock(Observer.class);
        HashCodeAndEqualsSafeSet set =
                HashCodeAndEqualsSafeSet.of(mockToKeep1, mockToKeep2, mock(List.class));
        List<Object> elementsToRetain = List.of(mockToKeep1, mockToKeep2);

        // Act
        boolean changed = set.retainAll(elementsToRetain);

        // Assert
        assertThat(changed).isTrue();
        assertThat(set).containsExactlyInAnyOrder(mockToKeep1, mockToKeep2);
    }

    // --- Iteration and Conversion ---

    @Test
    public void iterator_should_allow_iterating_over_all_elements() {
        // Arrange
        Object mock2 = mock(Observer.class);
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock, mock2);

        // Act
        List<Object> iteratedElements = new LinkedList<>();
        set.iterator().forEachRemaining(iteratedElements::add);

        // Assert
        assertThat(iteratedElements).containsExactlyInAnyOrder(problematicMock, mock2);
    }

    @Test
    public void iterator_remove_should_remove_element_from_set() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);
        Iterator<Object> iterator = set.iterator();

        // Act
        iterator.next();
        iterator.remove();

        // Assert
        assertThat(set).isEmpty();
    }

    @Test
    public void toArray_should_return_object_array_containing_all_elements() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act
        Object[] result = set.toArray();

        // Assert
        assertThat(result).containsExactly(problematicMock);
    }

    @Test
    public void toArray_with_typed_array_should_return_typed_array_containing_all_elements() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act
        UnmockableHashCodeAndEquals[] result = set.toArray(new UnmockableHashCodeAndEquals[0]);

        // Assert
        assertThat(result).containsExactly(problematicMock);
    }

    // --- Object Contract (equals, hashCode, toString, clone) ---

    @Test
    @SuppressWarnings("SelfAssertion") // Explicitly testing self-equality.
    public void equals_should_return_true_for_same_instance() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act & Assert
        assertThat(set).isEqualTo(set);
    }

    @Test
    public void equals_should_return_true_for_different_instances_with_same_content() {
        // Arrange
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of(problematicMock);
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act & Assert
        assertThat(set1).isEqualTo(set2);
    }

    @Test
    public void equals_should_return_false_for_sets_with_different_content() {
        // Arrange
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of(problematicMock);
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of();

        // Act & Assert
        assertThat(set1).isNotEqualTo(set2);
    }

    @Test
    public void equals_should_return_false_for_different_set_types_with_same_content() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();

        // Act & Assert
        assertThat(set).isNotEqualTo(new HashSet<Object>());
    }

    @Test
    public void hashCode_should_be_equal_for_sets_with_same_content() {
        // Arrange
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of(problematicMock);
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act & Assert
        assertThat(set1.hashCode()).isEqualTo(set2.hashCode());
    }

    @Test
    public void toString_should_return_non_empty_string_for_non_empty_set() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);

        // Act & Assert
        assertThat(set.toString()).isNotEmpty();
    }

    @Test
    public void clone_should_throw_CloneNotSupportedException() {
        // Arrange
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();

        // Act & Assert
        assertThatThrownBy(set::clone).isInstanceOf(CloneNotSupportedException.class);
    }

    /**
     * A helper class whose {@code hashCode()} and {@code equals()} methods always throw an
     * exception. This simulates a problematic object, like a misconfigured mock or a legacy class,
     * that {@link HashCodeAndEqualsSafeSet} is designed to handle.
     */
    private static class UnmockableHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new NullPointerException("I'm failing on hashCode and I don't care");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("I'm failing on equals and I don't care");
        }
    }
}