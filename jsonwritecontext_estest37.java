package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the state management of {@link JsonWriteContext}.
 */
public class JsonWriteContextTest {

    /**
     * Verifies that calling writeFieldName() when a value is expected (i.e., immediately
     * after another writeFieldName() call) returns the STATUS_EXPECT_VALUE status code.
     * This tests the context's state machine for handling invalid sequences.
     */
    @Test
    public void writeFieldName_WhenCalledConsecutively_ReturnsStatusExpectValue() throws JsonProcessingException {
        // Arrange: Create a child object context and write the first field name.
        // After writing a name, the context's internal state expects a value to be written next.
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.writeFieldName("first-name");

        // Act: Attempt to write a second field name immediately, without an intervening value.
        int status = objectContext.writeFieldName("second-name");

        // Assert: The context should return the status indicating that a value was expected.
        assertEquals("Should return STATUS_EXPECT_VALUE when a name is written while a value is expected",
                JsonWriteContext.STATUS_EXPECT_VALUE, status);

        // Further assert that the context's current name was updated to the most recent one.
        assertTrue("Context should have a current name after the second call", objectContext.hasCurrentName());
        assertEquals("Current name should be updated to the second name", "second-name", objectContext.getCurrentName());
    }
}