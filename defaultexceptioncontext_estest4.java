package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DefaultExceptionContext}.
 */
public class DefaultExceptionContextTest {

    /**
     * Tests that the formatted exception message is generated correctly when the context
     * contains a null label and a null value, and the base message is also null.
     */
    @Test
    public void testGetFormattedExceptionMessageWithNullContextAndNullBaseMessage() {
        // Arrange: Create a context and add an entry with a null label and null value.
        final DefaultExceptionContext context = new DefaultExceptionContext();
        context.setContextValue(null, null);

        // Act: Generate the formatted exception message with a null base message.
        final String formattedMessage = context.getFormattedExceptionMessage(null);

        // Assert: Verify that the output string matches the expected format.
        final String expectedMessage = "Exception Context:\n" +
                                       "\t[1:null=null]\n" +
                                       "---------------------------------";
        assertEquals(expectedMessage, formattedMessage);
    }
}