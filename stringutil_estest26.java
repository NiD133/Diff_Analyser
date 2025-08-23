package org.jsoup.internal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test suite for the {@link StringUtil#padding(int)} method.
 */
public class StringUtilPaddingTest {

    // The ExpectedException rule allows for clean, declarative testing of exceptions.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void paddingWithNegativeWidthThrowsIllegalArgumentException() {
        // Arrange: Define the expected exception type and message.
        // This makes the test's intent clear from the start.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("width must be >= 0");

        // Act: Call the method with an invalid argument that should trigger the exception.
        StringUtil.padding(-1);

        // Assert: The test passes if the expected exception is thrown.
        // The 'thrown' rule handles the assertion implicitly.
    }
}