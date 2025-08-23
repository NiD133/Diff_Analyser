package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * This test class has been refactored from an auto-generated EvoSuite test
 * to improve its clarity and maintainability. It focuses on testing the
 * behavior of the LZMAUtils class with invalid inputs.
 */
// The original test class name and inheritance are preserved to show a direct
// comparison and improvement over the original code.
public class LZMAUtils_ESTestTest11 extends LZMAUtils_ESTest_scaffolding {

    /**
     * Verifies that calling the deprecated {@code getCompressedFilename(String)} method
     * with a null input throws a {@code NullPointerException}.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getCompressedFilenameWithNullInputShouldThrowNullPointerException() {
        // The contract for this utility method requires a non-null filename.
        // This test ensures that passing null results in the expected exception.
        LZMAUtils.getCompressedFilename(null);
    }
}