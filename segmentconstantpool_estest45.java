package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Test suite for {@link SegmentConstantPool}.
 * This class demonstrates improvements to an auto-generated test case for clarity and maintainability.
 */
public class SegmentConstantPool_ESTestTest45 {

    /**
     * Verifies that {@code getInitMethodPoolEntry} throws an {@code IOException}
     * when invoked with a constant pool type other than {@code CP_METHOD}.
     *
     * The method is specifically designed to retrieve {@code <init>} methods, which
     * are required to be of type {@code CP_METHOD}. This test ensures that calls
     * with any other type are correctly rejected.
     */
    @Test
    public void getInitMethodPoolEntryShouldThrowExceptionForNonMethodType() {
        // Arrange
        // The CpBands can be null for this test because the type check occurs
        // before the bands are accessed within the method.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Use a constant pool type that is not CP_METHOD. The original test used the
        // magic number 8, which corresponds to the SIGNATURE constant.
        final int invalidCpType = SegmentConstantPool.SIGNATURE;
        final long anyIndex = 8L;
        final String anyClassName = "any/class/Name";
        final String expectedErrorMessage = "Nothing but CP_METHOD can be an <init>";

        // Act & Assert
        try {
            segmentConstantPool.getInitMethodPoolEntry(invalidCpType, anyIndex, anyClassName);
            fail("Expected an IOException to be thrown for a non-CP_METHOD type.");
        } catch (final IOException e) {
            // Verify that the exception has the expected, informative message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}