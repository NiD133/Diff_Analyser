package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Tests that converting a string that consists only of a separator character
     * from a different format results in the original string being returned.
     *
     * <p>The "best effort" conversion logic treats the input as a single word
     * since it doesn't contain the source format's separator ('_'), and thus the
     * "word" is returned unchanged.
     */
    @Test
    public void to_whenInputIsOnlySeparatorFromDifferentFormat_returnsInputUnchanged() {
        // Arrange
        CaseFormat sourceFormat = CaseFormat.UPPER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.LOWER_HYPHEN;
        String inputWithForeignSeparator = "-";

        // Act
        String result = sourceFormat.to(targetFormat, inputWithForeignSeparator);

        // Assert
        assertEquals(inputWithForeignSeparator, result);
    }
}