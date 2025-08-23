package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * A helper class that intentionally throws exceptions on {@code equals()} and {@code hashCode()}
     * to simulate objects (like mocks) that cannot be safely stored in standard hash-based collections.
     */
    private static class ClassWithBrokenEqualsAndHashCode {
        @Override
        public final int hashCode() {
            throw new NullPointerException("This hashCode() method is intentionally broken.");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("This equals() method is intentionally broken.");
        }
    }

    @Test
    public void shouldCorrectlyStoreMocksWithBrokenEqualsAndHashCode() {
        // Arrange
        // Create mocks of a class with failing equals() and hashCode() methods.
        // A standard HashSet would throw an exception when adding these.
        ClassWithBrokenEqualsAndHashCode mockToBeAdded = mock(ClassWithBrokenEqualsAndHashCode.class);
        ClassWithBrokenEqualsAndHashCode anotherMock = mock(ClassWithBrokenEqualsAndHashCode.class);

        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();

        // Act
        safeSet.add(mockToBeAdded);

        // Assert
        // The set should rely on identity rather than the broken equals()/hashCode() methods.
        assertThat(safeSet).hasSize(1);
        assertThat(safeSet).contains(mockToBeAdded);
        assertThat(safeSet).doesNotContain(anotherMock);
    }
}