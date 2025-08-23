package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Observer;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UnmockableHashCodeAndEquals mockWithFaultyHashCode;

    /**
     * A helper class whose instances cannot be stored in a standard HashSet
     * because its core methods throw exceptions.
     */
    private static class UnmockableHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new NullPointerException("Deliberately failing hashCode()");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("Deliberately failing equals()");
        }
    }

    @Test
    public void retainAll_shouldCorrectlyFilterSet_evenWhenContainingObjectWithFaultyHashCode() {
        // Arrange
        Object regularMock = mock(Observer.class);
        Object mockToBeRemoved = mock(List.class);

        // The set under test contains a mix of objects, including one that would break a normal HashSet.
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(
            mockWithFaultyHashCode,
            regularMock,
            mockToBeRemoved
        );

        // This collection defines the elements that should remain after the retainAll operation.
        HashCodeAndEqualsSafeSet elementsToRetain = HashCodeAndEqualsSafeSet.of(
            mockWithFaultyHashCode,
            regularMock
        );

        // Act
        // Perform the retainAll operation, which should remove any elements not in 'elementsToRetain'.
        boolean wasModified = set.retainAll(elementsToRetain);

        // Assert
        // The method should report that the set was modified.
        assertThat(wasModified).isTrue();

        // The set should now contain only the elements from 'elementsToRetain'.
        assertThat(set)
            .hasSize(2)
            .containsExactlyInAnyOrder(mockWithFaultyHashCode, regularMock);
    }
}