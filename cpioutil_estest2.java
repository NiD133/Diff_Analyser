package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

// The test class name and runner are kept to show a refactoring of the original code.
@RunWith(EvoRunner.class)
public class CpioUtil_ESTestTest2 extends CpioUtil_ESTest_scaffolding {

    /**
     * Tests that CpioUtil.fileType correctly returns 0 when the input mode is 0.
     * A mode of 0 has no file type bits set, so the extracted file type should also be 0.
     */
    @Test(timeout = 4000)
    public void fileTypeShouldReturnZeroForZeroMode() {
        // Arrange: A mode of 0L represents an entry with no file type information.
        final long modeWithNoFileType = 0L;
        final long expectedFileType = 0L;

        // Act: Extract the file type from the mode.
        final long actualFileType = CpioUtil.fileType(modeWithNoFileType);

        // Assert: The extracted file type should be 0.
        assertEquals(expectedFileType, actualFileType);
    }
}