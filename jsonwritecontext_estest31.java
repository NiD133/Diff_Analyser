package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on state management.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that the {@link JsonWriteContext#reset(int)} method correctly
     * changes the context's type. This test specifically checks the transition
     * from a ROOT context to an OBJECT context.
     */
    @Test
    public void resetShouldUpdateContextTypeToObject() {
        // Arrange: Create a root-level JsonWriteContext.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext context = new JsonWriteContext(
            JsonStreamContext.TYPE_ROOT, 
            null, // No parent for a root context
            dupDetector, 
            null  // No initial current value
        );

        // Sanity-check the initial state.
        assertFalse("Initially, the context should not be of type OBJECT.", context.inObject());
        assertEquals("Initially, the type description should be 'ROOT'.", "ROOT", context.getTypeDesc());

        // Act: Reset the context to an OBJECT type.
        context.reset(JsonStreamContext.TYPE_OBJECT);

        // Assert: Verify that the context type has been successfully updated.
        assertTrue("After reset, the context should be of type OBJECT.", context.inObject());
        assertEquals("After reset, the type description should be 'OBJECT'.", "OBJECT", context.getTypeDesc());
    }
}