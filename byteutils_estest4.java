package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#toLittleEndian(ByteUtils.ByteConsumer, long, int)}
     * does not perform any action or throw an exception when provided with a negative length.
     * The method is expected to return immediately without interacting with the consumer.
     */
    @Test
    public void toLittleEndianWithNegativeLengthShouldDoNothing() {
        // This test verifies that a negative length argument causes the method to exit early.
        // A null consumer is intentionally passed. If the method attempted to process any bytes,
        // it would throw a NullPointerException, causing the test to fail.
        // The test passes if no exception is thrown.
        ByteUtils.toLittleEndian(null, 0L, -1);
    }
}