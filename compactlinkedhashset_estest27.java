package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link CompactLinkedHashSet}.
 * Note: The original test class was a single, tool-generated test case. 
 * It has been refactored into a standard, understandable unit test.
 */
public class CompactLinkedHashSetTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when initialized with a negative expected size.
     */
    @Test
    public void createWithExpectedSize_negativeSize_throwsIllegalArgumentException() {
        int negativeExpectedSize = -1;

        try {
            // The constructor is package-private, but the static factory method is public.
            // We test the public API.
            CompactLinkedHashSet.createWithExpectedSize(negativeExpectedSize);
            fail("Expected an IllegalArgumentException to be thrown for a negative expected size.");
        } catch (IllegalArgumentException expected) {
            // This assertion verifies the public contract of the method, which includes
            // throwing an exception with a clear, user-facing message.
            assertEquals(
                "Expected size must be >= 0",
                expected.getMessage());
        }
    }
}