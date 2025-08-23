package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 *
 * This revised test focuses on clarity and maintainability,
 * replacing the auto-generated original.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void isEmpty_shouldReturnFalse_whenSetContainsAnElement() {
        // Arrange: Create a set and add one element to it.
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());

        // Act: Check if the set is empty.
        boolean result = set.isEmpty();

        // Assert: Verify that the set is not considered empty.
        assertFalse("A set containing an element should not be reported as empty.", result);
    }
}