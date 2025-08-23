package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;

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

    // A mock of a class whose hashCode() and equals() methods are final and throw exceptions.
    // This simulates a "problematic" mock that would break standard hash-based collections.
    @Mock
    private MockWithUnsafeImplementations mockWithFailingHashCodeAndEquals;

    private static class MockWithUnsafeImplementations {
        @Override
        public final int hashCode() {
            throw new NullPointerException("Intentionally failing hashCode()");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("Intentionally failing equals()");
        }
    }

    /**
     * This test verifies that the {@link HashCodeAndEqualsSafeSet#clear()} method
     * functions correctly. The key scenario is that the set is first populated
     * with a mock that has problematic {@code hashCode()} and {@code equals()}
     * implementations, which should not prevent the set from being cleared.
     */
    @Test
    public void shouldBeEmptyAfterClearIsCalled() {
        // Arrange: Create a set containing a mock that throws exceptions on hashCode() and equals().
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mockWithFailingHashCodeAndEquals);

        // Act: Clear the set.
        set.clear();

        // Assert: The set should now be empty.
        assertThat(set).isEmpty();
    }
}