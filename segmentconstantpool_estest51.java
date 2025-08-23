package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;

public class SegmentConstantPool_ESTestTest51 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that getConstantPoolEntry() throws an IOException when requested
     * to retrieve an entry of the currently unsupported SIGNATURE type.
     */
    @Test
    public void getConstantPoolEntryForUnsupportedSignatureTypeThrowsIOException() throws IOException {
        // Arrange
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int unsupportedType = SegmentConstantPool.SIGNATURE;
        final long arbitraryIndex = 8L;
        final String expectedMessage = "Type SIGNATURE is not supported yet: " + unsupportedType;

        // Act & Assert
        try {
            segmentConstantPool.getConstantPoolEntry(unsupportedType, arbitraryIndex);
            fail("Expected an IOException for unsupported constant pool type, but none was thrown.");
        } catch (final IOException e) {
            assertEquals("The exception message did not match the expected format.", expectedMessage, e.getMessage());
        }
    }
}