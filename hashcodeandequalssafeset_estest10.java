package org.mockito.internal.util.collections;

import org.junit.Test;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void of_withNullIterable_shouldCreateEmptySetWithValidIterator() {
        // Arrange: The static factory method 'of' is called with a null iterable.
        // This is a test for robust handling of null inputs.
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);

        // Act: Get the iterator from the resulting set.
        Iterator<Object> iterator = emptySet.iterator();

        // Assert: The iterator should be non-null and have no elements,
        // confirming that an empty set was created.
        assertNotNull("The iterator should never be null, even for an empty set.", iterator);
        assertFalse("The iterator from a set created with a null iterable should be empty.", iterator.hasNext());
    }
}