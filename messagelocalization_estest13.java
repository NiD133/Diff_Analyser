package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MessageLocalization} class, focusing on message composition.
 */
public class MessageLocalizationTest {

    /**
     * Verifies that getComposedMessage correctly formats a message string
     * by substituting a placeholder with an integer value.
     */
    @Test
    public void getComposedMessage_withIntegerParameter_shouldReturnFormattedString() {
        // Arrange: Define the message key, the parameter, and the expected output.
        // The key corresponds to the message: "You can only add cells to rows, no objects of type {1}."
        String messageKey = "you.can.only.add.cells.to.rows.no.objects.of.type.1";
        int objectTypeParameter = 42;
        String expectedMessage = "You can only add cells to rows, no objects of type " + objectTypeParameter + ".";

        // Act: Call the method under test.
        String actualMessage = MessageLocalization.getComposedMessage(messageKey, objectTypeParameter);

        // Assert: Verify that the actual output matches the expected formatted string.
        assertEquals(expectedMessage, actualMessage);
    }
}