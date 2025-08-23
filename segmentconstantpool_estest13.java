package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

/*
 * The following annotations and class structure are preserved from the original
 * to maintain compatibility with the existing test suite infrastructure.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true)
public class SegmentConstantPool_ESTestTest13 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that getConstantPoolEntry() throws an IOException when passed an unsupported,
     * negative constant pool type.
     */
    @Test(timeout = 4000)
    public void getConstantPoolEntryShouldThrowIOExceptionForUnsupportedType() {
        // Arrange: Set up the test conditions and expected outcomes.
        final int unsupportedType = -13;
        final long randomIndex = 1152L; // The index value is arbitrary for this test.
        final SegmentConstantPool constantPool = new SegmentConstantPool(null);
        final String expectedErrorMessage = "Type is not supported yet: " + unsupportedType;

        // Act & Assert: Execute the method and verify the expected exception.
        try {
            constantPool.getConstantPoolEntry(unsupportedType, randomIndex);
            fail("Expected an IOException for an unsupported constant pool type, but none was thrown.");
        } catch (final IOException e) {
            // Verify that the exception message is correct.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}