package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;

import java.util.Locale;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} returns null when the input string is null.
     * This is the documented behavior for null inputs.
     */
    @Test
    public void toLocaleShouldReturnNullForNullInput() {
        // Act: Call the method under test with a null argument.
        final Locale result = LocaleUtils.toLocale(null);

        // Assert: Verify that the method returns null as expected.
        assertNull("Calling toLocale with a null string should return null.", result);
    }
}