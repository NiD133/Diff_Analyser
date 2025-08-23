package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Tests that {@link SerializedString#appendUnquoted(char[], int)} correctly
     * appends the unquoted string value into a character array at a specified offset.
     */
    @Test
    public void appendUnquotedShouldAppendValueToCharArrayAtGivenOffset() {
        // Arrange: Set up the test data and environment.
        final SerializedString serializedString = new SerializedString("j");
        final char[] destinationBuffer = new char[4];
        final int offset = 1;
        final char[] expectedBuffer = new char[]{'\u0000', 'j', '\u0000', '\u0000'};

        // Act: Call the method under test.
        final int charsAppended = serializedString.appendUnquoted(destinationBuffer, offset);

        // Assert: Verify the results.
        // 1. Check that the returned value is the number of characters appended.
        assertEquals("The method should return the length of the appended string.", 1, charsAppended);

        // 2. Check that the destination buffer was modified correctly.
        assertArrayEquals("The string's character should be placed at the specified offset.",
                expectedBuffer, destinationBuffer);
    }
}