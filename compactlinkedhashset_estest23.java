package com.google.common.collect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    @Test
    public void createWithExpectedSize_shouldThrowIllegalArgumentException_whenSizeIsNegative() {
        try {
            CompactLinkedHashSet.createWithExpectedSize(-1);
            fail("Expected an IllegalArgumentException to be thrown for a negative expected size, but it was not.");
        } catch (IllegalArgumentException e) {
            assertEquals("Expected size must be >= 0", e.getMessage());
        }
    }
}