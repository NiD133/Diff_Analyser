package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void of_shouldReturnEmptySet_whenCalledWithNullIterable() {
        // Arrange: No setup is needed as we are testing a static factory method with a null argument.

        // Act: Create a set from a null iterable.
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);

        // Assert: The resulting set should be non-null and empty.
        assertNotNull("The factory method should always return a set instance, even for null input.", safeSet);
        assertTrue("A set created from a null iterable should be empty.", safeSet.isEmpty());
    }
}