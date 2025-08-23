package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
// Renamed from HashCodeAndEqualsSafeSetTestTest2 for clarity and to follow conventions.
public class HashCodeAndEqualsSafeSetTest {

    /**
     * A helper class that simulates an object whose hashCode() and equals() methods
     * are broken or not stubbed on a mock, causing them to throw an exception when invoked.
     */
    private static class ObjectWithFailingHashCodeAndEquals {

        @Override
        public final int hashCode() {
            throw new UnsupportedOperationException("hashCode() is designed to fail for this test");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new UnsupportedOperationException("equals() is designed to fail for this test");
        }
    }

    @Test
    // Renamed from "mock_with_failing_hashCode_method_can_be_added" for convention and clarity.
    // The test now explicitly verifies the state of the set after the operation.
    public void shouldAddObjectWhoseHashCodeAndEqualsFail() {
        // Arrange
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object elementWithFailingHashCode = new ObjectWithFailingHashCodeAndEquals();

        // Act
        boolean wasAdded = set.add(elementWithFailingHashCode);

        // Assert
        // Verify that the element was added successfully without invoking its problematic methods.
        assertThat(wasAdded).isTrue();
        assertThat(set).hasSize(1);
        assertThat(set).contains(elementWithFailingHashCode);
    }
}