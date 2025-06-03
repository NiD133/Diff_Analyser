package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ByteOrderMarkTest {

    @Test
    void testUTF8MatchesItsOwnRawBytes() {
        // Arrange: Get the UTF-8 byte order mark.
        ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;

        // Act: Get the raw bytes representing the UTF-8 BOM.
        int[] rawBytes = utf8Bom.getRawBytes();

        // Assert: Verify that the UTF-8 BOM matches its own raw bytes.  This is a basic self-consistency check.
        assertTrue(utf8Bom.matches(rawBytes), "The UTF-8 BOM should match its own raw byte representation.");
    }
}