package org.mockito.internal.util.collections;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 *
 * This test suite focuses on verifying that the set correctly handles elements
 * whose 'hashCode()' or 'equals()' methods are "broken" (i.e., they throw exceptions).
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * A helper class that simulates an object whose hashCode() and equals() methods
     * are implemented improperly and throw exceptions when called.
     */
    private static class ObjectWithThrowingHashCodeAndEquals {
        @Override
        public final int hashCode() {
            throw new UnsupportedOperationException("This hashCode() method is broken.");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new UnsupportedOperationException("This equals() method is broken.");
        }
    }

    @Test
    public void hashCode_shouldBeEqual_forSetsContainingTheSameProblematicElement() {
        // Arrange
        // Create an element whose hashCode() and equals() methods throw exceptions.
        // The HashCodeAndEqualsSafeSet is designed to handle such objects gracefully.
        ObjectWithThrowingHashCodeAndEquals problematicElement = new ObjectWithThrowingHashCodeAndEquals();

        // Create two different set instances containing the exact same element.
        HashCodeAndEqualsSafeSet set1 = HashCodeAndEqualsSafeSet.of(problematicElement);
        HashCodeAndEqualsSafeSet set2 = HashCodeAndEqualsSafeSet.of(problematicElement);

        // Act
        int hashCode1 = set1.hashCode();
        int hashCode2 = set2.hashCode();

        // Assert
        // According to the Set contract, two sets that are equal must have the same hash code.
        // This test verifies that our safe set implementation adheres to this rule, even with
        // problematic elements.
        assertThat(hashCode1).isEqualTo(hashCode2);
    }
}