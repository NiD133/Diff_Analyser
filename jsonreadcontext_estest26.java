package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on name handling.
 */
public class JsonReadContextTest {

    /**
     * Verifies that after setting a current name, even an empty one,
     * the context correctly reports that it has a current name.
     */
    @Test
    public void hasCurrentName_shouldReturnTrue_afterSettingEmptyName() throws Exception {
        // Arrange: Create a root JsonReadContext.
        // A DupDetector is required, but its behavior is not under test here.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        final String fieldName = "";

        // Act: Set the current name to an empty string.
        context.setCurrentName(fieldName);

        // Assert: Verify the context now has a current name and it matches the one set.
        assertTrue("Context should report having a current name after one is set.", context.hasCurrentName());
        assertEquals("The current name should be the empty string that was set.", fieldName, context.getCurrentName());
    }
}