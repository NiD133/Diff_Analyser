package org.jsoup.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link StringUtil#releaseBuilder(StringBuilder)}.
 */
public class StringUtil_ESTestTest47 { // Note: Class name kept for consistency with the original file.

    @Test
    public void releaseBuilderShouldThrowExceptionForNullInput() {
        // The releaseBuilder method is expected to validate its input and reject nulls.
        try {
            StringUtil.releaseBuilder(null);
            fail("Expected an IllegalArgumentException to be thrown for a null StringBuilder.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and helpful.
            // This is the standard message from org.jsoup.helper.Validate.notNull.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}