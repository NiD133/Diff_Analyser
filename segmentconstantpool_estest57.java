package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;

// The original class name and inheritance are preserved to show a direct refactoring.
// In a real-world scenario, these might also be renamed for better organization.
public class SegmentConstantPool_ESTestTest57 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that getClassSpecificPoolEntry() throws a Pack200Exception when an unsupported
     * (e.g., negative) constant pool type is provided.
     */
    @Test(timeout = 4000)
    public void getClassSpecificPoolEntryWithInvalidTypeThrowsException() {
        // Arrange
        final int invalidCpType = -1;
        final long irrelevantIndex = 0L;
        final String irrelevantClassName = "any/class/Name";

        // The CpBands dependency is not used when the type check fails, so null is acceptable.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Act & Assert
        try {
            segmentConstantPool.getClassSpecificPoolEntry(invalidCpType, irrelevantIndex, irrelevantClassName);
            fail("Expected a Pack200Exception because the constant pool type is invalid.");
        } catch (Pack200Exception e) {
            // The method is expected to throw Pack200Exception, a subclass of IOException.
            String expectedMessage = "Type is not supported yet: " + invalidCpType;
            assertEquals("The exception message should indicate the unsupported type.", expectedMessage, e.getMessage());
        }
    }
}