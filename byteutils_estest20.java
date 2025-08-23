package org.apache.commons.compress.utils;

import java.io.OutputStream;
import org.junit.Test;

/**
 * This test class contains tests for the {@link ByteUtils} class.
 * Note: The class name and inheritance are preserved from the original auto-generated test.
 * A manual rewrite would likely rename the class to ByteUtilsTest and remove the inheritance.
 */
public class ByteUtils_ESTestTest20 extends ByteUtils_ESTest_scaffolding {

    /**
     * Tests that {@link ByteUtils#toLittleEndian(OutputStream, long, int)} throws
     * a {@code NullPointerException} when the provided OutputStream is null.
     */
    @Test(expected = NullPointerException.class)
    public void toLittleEndianWithNullOutputStreamThrowsNullPointerException() {
        // Arrange: Define dummy arguments for the method call.
        // The actual value and length do not matter for this test, as the
        // NullPointerException should be thrown when accessing the null stream.
        final long DUMMY_VALUE = 0L;
        final int DUMMY_LENGTH = 4;
        final OutputStream nullStream = null;

        // Act & Assert: Call the method with a null stream, expecting an exception.
        ByteUtils.toLittleEndian(nullStream, DUMMY_VALUE, DUMMY_LENGTH);
    }
}