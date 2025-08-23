package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    @Test
    public void isCaseSensitive_withInsensitiveEnum_shouldReturnFalse() {
        // Arrange: Define the input for the test
        final IOCase insensitive = IOCase.INSENSITIVE;

        // Act: Call the method under test
        final boolean isSensitive = IOCase.isCaseSensitive(insensitive);

        // Assert: Verify the result is as expected
        assertFalse("isCaseSensitive should return false for IOCase.INSENSITIVE", isSensitive);
    }
}