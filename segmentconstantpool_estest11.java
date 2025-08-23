package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class has been refactored from an auto-generated EvoSuite test
 * to improve readability and maintainability.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that {@link SegmentConstantPool#getValue(int, long)} throws an {@link Error}
     * when provided with an unknown constant pool (cp) type. The method is expected to
     * throw an Error for any type that is not a predefined constant.
     */
    @Test
    public void getValueShouldThrowErrorForUnknownConstantPoolType() {
        // Arrange
        // The CpBands object is not accessed when the cp type is invalid, so it can be null for this test.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        
        // An arbitrary negative value representing an invalid constant pool type.
        int unknownCpType = -1;
        
        // The index value is not used in this error path, so a simple value is used.
        long index = 0L;

        // Act & Assert
        try {
            segmentConstantPool.getValue(unknownCpType, index);
            fail("Expected an Error to be thrown for an unknown constant pool type, but no exception was thrown.");
        } catch (Error e) {
            // Verify that the correct Error is thrown with the expected message.
            String expectedMessage = "Tried to get a value I don't know about: " + unknownCpType;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}