package org.apache.commons.lang3;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that toLocale() throws an IllegalArgumentException for a string
     * with a malformed country code.
     */
    @Test
    public void toLocaleShouldThrowExceptionForMalformedCountryCode() {
        // Arrange: A locale string is invalid if the country code part is not
        // 2 uppercase letters or 3 digits. "uCN" is an example of such a malformed code.
        final String malformedLocaleString = "zh-uCN";

        // Configure the expected exception
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid locale format: " + malformedLocaleString);

        // Act: Call the method under test with the invalid input.
        // The test will pass if the expected exception is thrown.
        LocaleUtils.toLocale(malformedLocaleString);
    }
}