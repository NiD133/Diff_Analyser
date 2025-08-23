package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that the init() method throws an IllegalArgumentException
     * when called with a negative expected size.
     */
    @Test
    public void init_withNegativeExpectedSize_throwsIllegalArgumentException() {
        // Arrange: Create a new set and define an invalid (negative) expected size.
        CompactLinkedHashSet<Object> set = new CompactLinkedHashSet<>();
        int negativeExpectedSize = -1;

        // Act & Assert: Attempt to initialize the set and verify the exception.
        try {
            set.init(negativeExpectedSize);
            fail("Expected an IllegalArgumentException to be thrown for negative expected size.");
        } catch (IllegalArgumentException expectedException) {
            // The method under test relies on Guava's Preconditions, which provides this message.
            assertEquals("Expected size must be >= 0", expectedException.getMessage());
        }
    }
}