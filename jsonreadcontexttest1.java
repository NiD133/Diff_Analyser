package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its behavior
 * regarding duplicate field name detection.
 */
// The test class was renamed from "JsonReadContextTestTest1" to the standard "JsonReadContextTest".
public class JsonReadContextTest extends JUnit5TestBase {

    @Test
    // The test method name was improved to clearly describe the behavior under test
    // following a Behavior-Driven Development (BDD) style.
    void shouldThrowExceptionWhenSettingSameNameTwiceInObjectContext() throws Exception {
        // --- Arrange: Set up the test objects and state ---

        // 1. A DupDetector is required to enable duplicate field name checking.
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);

        // 2. Create a realistic context hierarchy. Using factory methods like `createRootContext`
        // and `createChildObjectContext` is more idiomatic and readable than calling a
        // constructor with arbitrary "magic numbers" (like 2441).
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);
        JsonReadContext objectContext = rootContext.createChildObjectContext(1, 1);

        // 3. Define the field name in a constant for clarity and reuse.
        final String fieldName = "dupField";

        // 4. Set the field name for the first time, which should succeed.
        objectContext.setCurrentName(fieldName);

        // --- Act & Assert: Perform the action and verify the result ---

        // Use JUnit 5's assertThrows for modern, concise exception testing.
        // This is more readable than a traditional try-catch-fail block.
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            // This second call with the same name is expected to fail.
            objectContext.setCurrentName(fieldName);
        });

        // Verify that the exception message clearly indicates a duplicate field error.
        String expectedMessageFragment = "Duplicate field '" + fieldName + "'";
        assertTrue(
            exception.getMessage().contains(expectedMessageFragment),
            "Exception message should report a duplicate field."
        );
    }
}