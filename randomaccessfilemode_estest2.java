package org.apache.commons.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that the values() method returns all expected enum constants.
     * This acts as a safeguard to ensure that if a new mode is added, this test
     * will fail, prompting a review of related logic.
     */
    @Test
    public void testValuesReturnsAllDefinedModes() {
        // The RandomAccessFileMode enum is expected to have four constants:
        // READ_ONLY, READ_WRITE, READ_WRITE_SYNC_ALL, and READ_WRITE_SYNC_CONTENT.
        final RandomAccessFileMode[] modes = RandomAccessFileMode.values();

        // Verify that the number of enum constants is correct.
        assertEquals("The RandomAccessFileMode enum should contain exactly 4 constants.", 4, modes.length);
    }
}