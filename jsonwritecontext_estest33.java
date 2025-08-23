package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonWriteContext} class, focusing on its state
 * management when writing values.
 */
public class JsonWriteContextTest {

    /**
     * Tests that calling {@code writeValue()} on a newly created child object context
     * returns the status {@code STATUS_EXPECT_NAME}, indicating that a field name
     * is expected next.
     */
    @Test
    public void writeValue_onNewChildObjectContext_shouldReturnStatusExpectName() {
        // Arrange: Create a root context and a child object context from it.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext();

        // Act: Call the method under test.
        int status = childObjectContext.writeValue();

        // Assert: Verify the returned status and the state of the contexts.
        assertEquals("The status should indicate that a field name is expected next.",
                JsonWriteContext.STATUS_EXPECT_NAME, status);

        // Verify context properties are correctly set after creation.
        assertEquals("The child context should be at nesting depth 1.",
                1, childObjectContext.getNestingDepth());
        assertEquals("The parent context should remain a 'root' type.",
                "root", rootContext.typeDesc());
        assertEquals("The parent context should have no entries yet.",
                0, rootContext.getEntryCount());
    }
}