package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
    // Descriptive name for the mock, clarifying its role in the test.
    private MockWithBrokenEqualsAndHashCode mockWithBrokenMethods;

    /**
     * A helper class that simulates an object where `hashCode()` and `equals()`
     * are final and throw exceptions. This is the primary use case for
     * {@link HashCodeAndEqualsSafeSet}, which must handle such objects gracefully
     * without throwing exceptions.
     */
    private static class MockWithBrokenEqualsAndHashCode {
        @Override
        public final int hashCode() {
            throw new UnsupportedOperationException("This hashCode() method is intentionally broken.");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new UnsupportedOperationException("This equals() method is intentionally broken.");
        }
    }

    /**
     * Verifies that the {@link HashCodeAndEqualsSafeSet#equals(Object)} method returns false
     * when comparing two sets with different contents.
     * <p>
     * Crucially, this test ensures the comparison does not fail even when the set's elements
     * have `equals()` or `hashCode()` methods that throw exceptions.
     */
    @Test
    public void shouldBeUnequalForSetsWithDifferentContent() {
        // Arrange
        HashCodeAndEqualsSafeSet setWithElement = HashCodeAndEqualsSafeSet.of(mockWithBrokenMethods);
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of();

        // Act & Assert
        // The `equals` implementation of HashCodeAndEqualsSafeSet should correctly
        // identify the sets as different without throwing an exception from the problematic element.
        assertThat(setWithElement).isNotEqualTo(emptySet);

        // Also verify the equals contract is symmetrical.
        assertThat(emptySet).isNotEqualTo(setWithElement);
    }
}