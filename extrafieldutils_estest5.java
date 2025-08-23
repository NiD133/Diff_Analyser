package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the inner class {@link ExtraFieldUtils.UnparseableExtraField}.
 */
public class ExtraFieldUtilsTest {

    @Test
    public void getKeyForSkipActionShouldReturnCorrectConstant() {
        // Arrange: The expected key is defined as a public constant, which we should use
        // for clarity and to make the test resilient to future refactoring.
        final int expectedKey = ExtraFieldUtils.UnparseableExtraField.SKIP_KEY;
        final ExtraFieldUtils.UnparseableExtraField skipAction = ExtraFieldUtils.UnparseableExtraField.SKIP;

        // Act: Retrieve the key from the SKIP instance.
        final int actualKey = skipAction.getKey();

        // Assert: Verify that the returned key matches the public constant.
        assertEquals("The key for the SKIP action should match its defined constant.", expectedKey, actualKey);
    }
}