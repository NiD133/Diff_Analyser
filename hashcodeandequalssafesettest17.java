package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Iterator;
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
    private MockWithBrokenContracts problematicMock;

    // This helper class simulates an object where hashCode() and equals() are final
    // and throw exceptions. This is the exact scenario HashCodeAndEqualsSafeSet
    // is designed to handle, as standard collections would fail.
    private static class MockWithBrokenContracts {
        @Override
        public final int hashCode() {
            throw new UnsupportedOperationException("Intentionally broken hashCode()");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new UnsupportedOperationException("Intentionally broken equals()");
        }
    }

    /**
     * Verifies that the iterator's remove() method works correctly
     * even for elements with broken hashCode() and equals() methods.
     */
    @Test
    public void shouldRemoveElementUsingIterator() {
        // Arrange: Create a set containing a single mock that would break a standard HashSet.
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(problematicMock);
        Iterator<Object> iterator = set.iterator();

        // Act: Advance the iterator and remove the element.
        // This should succeed without throwing an exception.
        iterator.next();
        iterator.remove();

        // Assert: The set should be empty after the element is removed.
        assertThat(set).isEmpty();
    }
}