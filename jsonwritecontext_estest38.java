package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonWriteContext_ESTestTest38 extends JsonWriteContext_ESTest_scaffolding {

    /**
     * Tests that calling writeValue() within an object context correctly updates the
     * entry count and returns the expected status code after writing multiple entries.
     *
     * This test simulates writing two key-value pairs into a JSON object context.
     */
    @Test
    public void writeValueInObjectContextShouldUpdateStateCorrectly() {
        // Arrange: Set up a root context and a child object context, mimicking the
        // state of a JSON generator that has just started a new object.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();

        assertNotNull("The created child object context should not be null.", objectContext);

        // Act: Simulate writing two key-value pairs. In an object context,
        // writeValue() is always preceded by writeFieldName().
        objectContext.writeFieldName("field1");
        objectContext.writeValue(); // Write value for the first field

        objectContext.writeFieldName("field2");
        int finalStatus = objectContext.writeValue(); // Write value for the second field and capture its status

        // Assert: Verify the final state of the context.
        // After writing two entries, the count should be 2.
        assertEquals("The entry count should be 2 after writing two key-value pairs.",
                2, objectContext.getEntryCount());

        // The status returned by writeValue() in an object context should be
        // STATUS_OK_AFTER_COLON, indicating the generator can proceed.
        assertEquals("The status after writing a value should be STATUS_OK_AFTER_COLON.",
                JsonWriteContext.STATUS_OK_AFTER_COLON, finalStatus);
    }
}