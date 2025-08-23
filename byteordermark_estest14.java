package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that a ByteOrderMark instance correctly matches the byte array
     * it was constructed with.
     */
    @Test
    public void testMatchesReturnsTrueForOwnBytes() {
        // Arrange: Create a custom BOM with a known byte sequence.
        // Using a descriptive charset name and a non-trivial byte array makes the test's intent clearer.
        final String charsetName = "CUSTOM-ENCODING";
        final int[] bomBytes = {0xCA, 0xFE, 0xBA, 0xBE};
        final ByteOrderMark bom = new ByteOrderMark(charsetName, bomBytes);

        // Act & Assert: A BOM should always match its own byte sequence.
        // The assertion message explains the expected behavior.
        assertTrue("A BOM should match the byte array it was created with.", bom.matches(bomBytes));
    }
}