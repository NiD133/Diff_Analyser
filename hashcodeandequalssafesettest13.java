package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet#equals(Object)}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void shouldNotBeEqualToDifferentSetImplementation() {
        // Arrange
        // Create an empty instance of the class under test.
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of();

        // Create an empty instance of a standard Set for comparison.
        Set<Object> standardHashSet = new HashSet<>();

        // Act & Assert
        // A HashCodeAndEqualsSafeSet should not be equal to other Set implementations
        // (like HashSet), even if their contents are identical (e.g., both are empty).
        // This verifies a specific contract of this custom Set implementation.
        assertThat(safeSet).isNotEqualTo(standardHashSet);
    }
}