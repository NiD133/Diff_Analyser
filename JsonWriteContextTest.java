package com.fasterxml.jackson.core.json;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonWriteContext} class.
 */
public class JsonWriteContextTest extends JUnit5TestBase {

    /**
     * A simple extension of {@link JsonWriteContext} for testing purposes.
     */
    static class TestJsonWriteContext extends JsonWriteContext {
        public TestJsonWriteContext(int type, JsonWriteContext parent, DupDetector dups, Object currValue) {
            super(type, parent, dups, currValue);
        }
    }

    /**
     * Test to verify that an instance of {@link TestJsonWriteContext} can be created successfully.
     * This test ensures that the constructor of the extended class works as expected.
     */
    @Test
    void shouldCreateTestJsonWriteContextInstance() {
        // Arrange: Create an instance of the TestJsonWriteContext with sample parameters
        TestJsonWriteContext context = new TestJsonWriteContext(0, null, null, 0);

        // Assert: Verify that the context instance is not null
        assertNotNull(context, "The TestJsonWriteContext instance should be successfully created.");
    }
}