package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
    private ObjectWithFailingHashCodeAndEquals mockWithFailingHashCode;

    /**
     * A helper class whose hashCode() and equals() methods are designed to fail.
     * This simulates a mock object where these methods have not been stubbed and
     * would throw an exception if called.
     */
    private static class ObjectWithFailingHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new NullPointerException("hashCode() has failed");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("equals() has failed");
        }
    }

    /**
     * This test verifies that the addAll() method of HashCodeAndEqualsSafeSet
     * works correctly even when the source collection contains an object
     * whose hashCode() and equals() methods throw exceptions. A standard
     * java.util.HashSet would fail in this scenario.
     */
    @Test
    public void addAllShouldSucceedForCollectionContainingObjectWithFailingHashCode() {
        // Arrange
        Observer regularMock = mock(Observer.class);
        HashCodeAndEqualsSafeSet sourceSet = HashCodeAndEqualsSafeSet.of(mockWithFailingHashCode, regularMock);
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();

        // Act
        safeSet.addAll(sourceSet);

        // Assert
        assertThat(safeSet)
            .hasSize(2)
            .containsAll(sourceSet);
    }
}