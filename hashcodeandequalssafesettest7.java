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

// Renamed from HashCodeAndEqualsSafeSetTestTest7 for conciseness and clarity.
public class HashCodeAndEqualsSafeSetTest {

    // A descriptive name for the rule is clearer than 'r'.
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    // This mock has problematic hashCode/equals methods, which is the core scenario for the safe set.
    @Mock
    private UnmockableHashCodeAndEquals mockWithBrokenHashCode;

    /**
     * A helper class designed to throw exceptions from {@code hashCode()} and {@code equals()},
     * simulating a problematic object that standard collections cannot handle.
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

    @Test
    public void removeAllShouldSucceedForCollectionContainingProblematicMock() {
        // Arrange
        Observer standardMock = mock(Observer.class);
        List<?> mockToRemain = mock(List.class);

        // The set under test, containing a problematic mock, a standard mock,
        // and a mock that should not be removed.
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(
            mockWithBrokenHashCode,
            standardMock,
            mockToRemain
        );

        // A collection containing the elements to be removed, including the problematic mock.
        HashCodeAndEqualsSafeSet elementsToRemove = HashCodeAndEqualsSafeSet.of(
            mockWithBrokenHashCode,
            standardMock
        );

        // Act
        boolean wasModified = safeSet.removeAll(elementsToRemove);

        // Assert
        // The operation should report that the set was changed.
        assertThat(wasModified).isTrue();

        // The set should now only contain the element that was not in the 'elementsToRemove' collection.
        // This single assertion verifies both the final size and the exact content of the set.
        assertThat(safeSet).containsExactly(mockToRemain);
    }
}