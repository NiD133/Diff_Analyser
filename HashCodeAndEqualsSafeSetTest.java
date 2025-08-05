/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

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
 * Tests for HashCodeAndEqualsSafeSet - a collection that can safely handle mocks
 * with failing hashCode() and equals() methods.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @Mock 
    private MockWithFailingHashCodeAndEquals mockWithFailingMethods;

    // ========== Basic Operations Tests ==========

    @Test
    public void shouldAllowAddingMockWithFailingHashCode() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        
        // When & Then - should not throw exception
        safeSet.add(mockWithFailingMethods);
    }

    @Test
    public void shouldHandleContainsOperationWithFailingEquals() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        safeSet.add(mockWithFailingMethods);
        MockWithFailingHashCodeAndEquals differentMock = mock(MockWithFailingHashCodeAndEquals.class);

        // When & Then
        assertThat(safeSet.contains(mockWithFailingMethods))
            .as("Set should contain the added mock")
            .isTrue();
            
        assertThat(safeSet.contains(differentMock))
            .as("Set should not contain a different mock")
            .isFalse();
    }

    @Test
    public void shouldAllowRemovingMockWithFailingMethods() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        safeSet.add(mockWithFailingMethods);

        // When
        safeSet.remove(mockWithFailingMethods);

        // Then
        assertThat(safeSet.isEmpty())
            .as("Set should be empty after removing the only element")
            .isTrue();
    }

    @Test
    public void shouldClearAllElements() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);
        
        // When
        safeSet.clear();

        // Then
        assertThat(safeSet)
            .as("Set should be empty after clear")
            .isEmpty();
    }

    // ========== Collection Operations Tests ==========

    @Test
    public void shouldAddAllElementsFromAnotherCollection() {
        // Given
        HashCodeAndEqualsSafeSet sourceSet = HashCodeAndEqualsSafeSet.of(
            mockWithFailingMethods, 
            mock(Observer.class)
        );
        HashCodeAndEqualsSafeSet targetSet = new HashCodeAndEqualsSafeSet();

        // When
        targetSet.addAll(sourceSet);

        // Then
        assertThat(targetSet.containsAll(sourceSet))
            .as("Target set should contain all elements from source set")
            .isTrue();
    }

    @Test
    public void shouldRetainOnlySpecifiedElements() {
        // Given
        Object observerMock = mock(Observer.class);
        Object listMock = mock(List.class);
        
        HashCodeAndEqualsSafeSet elementsToRetain = HashCodeAndEqualsSafeSet.of(
            mockWithFailingMethods, 
            observerMock
        );
        
        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();
        workingSet.addAll(elementsToRetain);
        workingSet.add(listMock); // This should be removed by retainAll

        // When
        boolean wasModified = workingSet.retainAll(elementsToRetain);

        // Then
        assertThat(wasModified)
            .as("RetainAll should return true when set was modified")
            .isTrue();
            
        assertThat(workingSet.containsAll(elementsToRetain))
            .as("Working set should contain all retained elements")
            .isTrue();
            
        assertThat(workingSet.contains(listMock))
            .as("Working set should not contain the non-retained element")
            .isFalse();
    }

    @Test
    public void shouldRemoveAllSpecifiedElements() {
        // Given
        Object observerMock = mock(Observer.class);
        Object listMock = mock(List.class);
        
        HashCodeAndEqualsSafeSet elementsToRemove = HashCodeAndEqualsSafeSet.of(
            mockWithFailingMethods, 
            observerMock
        );
        
        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();
        workingSet.addAll(elementsToRemove);
        workingSet.add(listMock); // This should remain after removeAll

        // When
        boolean wasModified = workingSet.removeAll(elementsToRemove);

        // Then
        assertThat(wasModified)
            .as("RemoveAll should return true when set was modified")
            .isTrue();
            
        assertThat(workingSet.containsAll(elementsToRemove))
            .as("Working set should not contain any removed elements")
            .isFalse();
            
        assertThat(workingSet.contains(listMock))
            .as("Working set should still contain the non-removed element")
            .isTrue();
    }

    // ========== Iterator Tests ==========

    @Test
    public void shouldSupportIterationOverElements() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(
            mockWithFailingMethods, 
            mock(Observer.class)
        );

        // When
        LinkedList<Object> iteratedElements = new LinkedList<>();
        for (Object element : safeSet) {
            iteratedElements.add(element);
        }

        // Then
        assertThat(iteratedElements)
            .as("Should be able to iterate over all elements")
            .isNotEmpty()
            .hasSize(2);
    }

    @Test
    public void shouldSupportRemovalThroughIterator() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);
        Iterator<Object> iterator = safeSet.iterator();

        // When
        iterator.next();
        iterator.remove();

        // Then
        assertThat(safeSet)
            .as("Set should be empty after removing element via iterator")
            .isEmpty();
    }

    // ========== Array Conversion Tests ==========

    @Test
    public void shouldConvertToObjectArray() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);

        // When
        Object[] array = safeSet.toArray();

        // Then
        assertThat(array[0])
            .as("Array should contain the same mock instance")
            .isSameAs(mockWithFailingMethods);
    }

    @Test
    public void shouldConvertToTypedArray() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);

        // When
        MockWithFailingHashCodeAndEquals[] typedArray = 
            safeSet.toArray(new MockWithFailingHashCodeAndEquals[0]);

        // Then
        assertThat(typedArray[0])
            .as("Typed array should contain the same mock instance")
            .isSameAs(mockWithFailingMethods);
    }

    // ========== Object Contract Tests ==========

    @Test
    public void shouldBeEqualToItself() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);
        
        // When & Then
        assertThat(safeSet)
            .as("Set should be equal to itself")
            .isEqualTo(safeSet);
    }

    @Test
    public void shouldNotBeEqualToDifferentTypeWithSameContent() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of();
        HashSet<Object> regularHashSet = new HashSet<>();

        // When & Then
        assertThat(safeSet)
            .as("SafeSet should not be equal to regular HashSet even with same content")
            .isNotEqualTo(regularHashSet);
    }

    @Test
    public void shouldNotBeEqualWhenContentDiffers() {
        // Given
        HashCodeAndEqualsSafeSet setWithContent = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of();

        // When & Then
        assertThat(setWithContent)
            .as("Sets with different content should not be equal")
            .isNotEqualTo(emptySet);
    }

    @Test
    public void shouldHaveSameHashCodeForEqualContent() {
        // Given
        HashCodeAndEqualsSafeSet firstSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);
        HashCodeAndEqualsSafeSet secondSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);

        // When & Then
        assertThat(firstSet.hashCode())
            .as("Sets with equal content should have same hash code")
            .isEqualTo(secondSet.hashCode());
    }

    @Test
    public void shouldHaveNonEmptyStringRepresentation() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(mockWithFailingMethods);

        // When & Then
        assertThat(safeSet.toString())
            .as("String representation should not be empty")
            .isNotEmpty();
    }

    // ========== Unsupported Operations Tests ==========

    @Test
    public void shouldThrowExceptionWhenCloningIsAttempted() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of();

        // When & Then
        assertThatThrownBy(safeSet::clone)
            .as("Clone operation should not be supported")
            .isInstanceOf(CloneNotSupportedException.class);
    }

    // ========== Test Helper Classes ==========

    /**
     * A mock class that intentionally fails on hashCode() and equals() calls.
     * This simulates real-world scenarios where mocks cannot be properly stubbed
     * for these fundamental Object methods.
     */
    private static class MockWithFailingHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new NullPointerException("Intentional failure in hashCode() - simulates unstubbed mock");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("Intentional failure in equals() - simulates unstubbed mock");
        }
    }
}