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

public class HashCodeAndEqualsSafeSetTest {

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @Mock 
    private UnmockableHashCodeAndEquals failingMock;

    // Test adding a mock with a failing hashCode method
    @Test
    public void shouldAddMockWithFailingHashCode() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(failingMock);
    }

    // Test if a mock with a failing equals method can be used correctly
    @Test
    public void shouldContainMockWithFailingEquals() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(failingMock);

        assertThat(set.contains(failingMock)).isTrue();

        UnmockableHashCodeAndEquals anotherMock = mock(UnmockableHashCodeAndEquals.class);
        assertThat(set.contains(anotherMock)).isFalse();
    }

    // Test removing a mock from the set
    @Test
    public void shouldRemoveMock() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(failingMock);
        set.remove(failingMock);

        assertThat(set.isEmpty()).isTrue();
    }

    // Test adding a collection of mocks to the set
    @Test
    public void shouldAddCollectionOfMocks() {
        HashCodeAndEqualsSafeSet initialSet = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));
        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(initialSet);

        assertThat(workingSet.containsAll(initialSet)).isTrue();
    }

    // Test retaining a collection of mocks in the set
    @Test
    public void shouldRetainCollectionOfMocks() {
        HashCodeAndEqualsSafeSet initialSet = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));
        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(initialSet);
        workingSet.add(mock(List.class));

        assertThat(workingSet.retainAll(initialSet)).isTrue();
        assertThat(workingSet.containsAll(initialSet)).isTrue();
    }

    // Test removing a collection of mocks from the set
    @Test
    public void shouldRemoveCollectionOfMocks() {
        HashCodeAndEqualsSafeSet initialSet = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));
        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(initialSet);
        workingSet.add(mock(List.class));

        assertThat(workingSet.removeAll(initialSet)).isTrue();
        assertThat(workingSet.containsAll(initialSet)).isFalse();
    }

    // Test iterating over the set
    @Test
    public void shouldIterateOverMocks() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock, mock(Observer.class));

        LinkedList<Object> collectedMocks = new LinkedList<>();
        for (Object mock : set) {
            collectedMocks.add(mock);
        }
        assertThat(collectedMocks).isNotEmpty();
    }

    // Test converting the set to an array
    @Test
    public void shouldConvertToArray() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);

        assertThat(set.toArray()[0]).isSameAs(failingMock);
        assertThat(set.toArray(new UnmockableHashCodeAndEquals[0])[0]).isSameAs(failingMock);
    }

    // Test that cloning the set is not supported
    @Test
    public void shouldThrowExceptionOnClone() {
        assertThatThrownBy(() -> HashCodeAndEqualsSafeSet.of().clone())
                .isInstanceOf(CloneNotSupportedException.class);
    }

    // Test that the set is empty after clearing
    @Test
    public void shouldBeEmptyAfterClear() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        set.clear();

        assertThat(set).isEmpty();
    }

    // Test that the set is equal to itself
    @Test
    public void shouldBeEqualToItself() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        assertThat(set).isEqualTo(set);
    }

    // Test that the set is not equal to a different type of set with the same content
    @Test
    public void shouldNotBeEqualToDifferentSetType() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();
        assertThat(set).isNotEqualTo(new HashSet<>());
    }

    // Test that the set is not equal when content is different
    @Test
    public void shouldNotBeEqualWhenContentIsDifferent() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        assertThat(set).isNotEqualTo(HashCodeAndEqualsSafeSet.of());
    }

    // Test that hashCode is equal if content is equal
    @Test
    public void shouldHaveEqualHashCodeIfContentIsEqual() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        assertThat(set.hashCode()).isEqualTo(HashCodeAndEqualsSafeSet.of(failingMock).hashCode());
    }

    // Test that toString is not null or empty
    @Test
    public void shouldHaveNonEmptyToString() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        assertThat(set.toString()).isNotEmpty();
    }

    // Test removing an element using an iterator
    @Test
    public void shouldRemoveByIterator() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(failingMock);
        Iterator<Object> iterator = set.iterator();
        iterator.next();
        iterator.remove();

        assertThat(set).isEmpty();
    }

    // Inner class to simulate a mock with failing hashCode and equals methods
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