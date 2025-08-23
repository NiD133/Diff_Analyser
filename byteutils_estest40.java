package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// The test class structure is preserved from the original.
public class ByteUtils_ESTestTest40 extends ByteUtils_ESTest_scaffolding {

    /**
     * Tests that fromLittleEndian throws an IOException when the ByteSupplier
     * signals an end of data (-1) before the requested number of bytes has been read.
     */
    @Test(timeout = 4000)
    public void fromLittleEndianWithSupplierShouldThrowIOExceptionOnPrematureEndOfData() throws IOException {
        // Arrange: Create a mock supplier that immediately signals the end of data.
        // The contract of ByteSupplier#getAsByte is similar to InputStream#read(),
        // returning -1 to indicate no more bytes are available.
        ByteUtils.ByteSupplier mockSupplier = mock(ByteUtils.ByteSupplier.class);
        when(mockSupplier.getAsByte()).thenReturn(-1);

        // Act & Assert: Attempt to read one byte and verify that the correct exception is thrown.
        try {
            ByteUtils.fromLittleEndian(mockSupplier, 1);
            fail("Expected an IOException due to premature end of data, but none was thrown.");
        } catch (IOException e) {
            // Verify that the exception has the expected message.
            assertEquals("Premature end of data", e.getMessage());
        }

        // Verify that the getAsByte() method was indeed called on the supplier.
        verify(mockSupplier).getAsByte();
    }
}