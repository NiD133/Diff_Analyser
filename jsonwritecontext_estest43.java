package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state management.
 */
public class JsonWriteContextTest {

    /**
     * Tests that resetting a context with an unknown type code correctly updates
     * the type description to a question mark ("?").
     *
     * The {@code getTypeDesc()} method is expected to return "?" for any type code
     * that is not one of the predefined types (ROOT, ARRAY, OBJECT).
     */
    @Test
    public void resetWithUnknownTypeShouldYieldQuestionMarkTypeDescription() {
        // Arrange
        final int UNKNOWN_TYPE_CODE = -1; // An invalid type code to test fallback behavior.
        Object testValue = new Object();

        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonWriteContext contextWithDupDetector = rootContext.withDupDetector(dupDetector);

        // Act
        // Reset the context with the unknown type and a new current value.
        JsonWriteContext resetContext = contextWithDupDetector.reset(UNKNOWN_TYPE_CODE, testValue);

        // Assert
        // The type description for an unknown type should default to "?".
        assertEquals("The type description for an unknown type should be '?'",
                     "?", resetContext.getTypeDesc());
    }
}