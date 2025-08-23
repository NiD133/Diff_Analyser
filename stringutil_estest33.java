package org.jsoup.internal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@link StringUtil#isAscii(String)} method, focusing on invalid inputs.
 *
 * Note: The class name and inheritance are preserved from the original auto-generated test file.
 */
public class StringUtil_ESTestTest33 extends StringUtil_ESTest_scaffolding {

    // The ExpectedException rule is a clean, declarative way to assert that
    // a piece of code throws a specific exception.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that isAscii() throws an IllegalArgumentException when the input is null.
     * This is the expected behavior as the method relies on a non-null input.
     */
    @Test
    public void isAscii_whenInputIsNull_throwsIllegalArgumentException() {
        // Arrange: Define the expected exception type and message.
        // This makes the test's purpose clear from the start.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Object must not be null");

        // Act: Call the method with the problematic (null) input.
        StringUtil.isAscii(null);

        // Assert: The test passes if the expected exception is thrown by the 'Act' step.
        // The ExpectedException rule handles this assertion implicitly.
    }
}