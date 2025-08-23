package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void saturatedCast_withLongAtUpperBoundary_shouldReturnMaxValue() {
        // The saturatedCast method should correctly handle a long value that is
        // exactly at the upper boundary of the byte range.
        long input = Byte.MAX_VALUE; // 127L
        
        byte result = SignedBytes.saturatedCast(input);
        
        assertEquals(Byte.MAX_VALUE, result);
    }
}