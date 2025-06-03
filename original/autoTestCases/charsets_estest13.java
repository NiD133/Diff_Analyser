package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.util.SortedMap;

// A more readable and understandable test case for Charsets.requiredCharsets()
class CharsetsTest {

    @Test
    void testRequiredCharsetsIsNotEmpty() {
        // Arrange: No explicit setup needed, as we're testing a static method.

        // Act: Call the method being tested.
        SortedMap<String, Charset> availableCharsets = Charsets.requiredCharsets();

        // Assert: Verify that the returned map is not empty.
        // This ensures that the required charsets are indeed available.
        assertFalse(availableCharsets.isEmpty(), "The requiredCharsets map should not be empty, indicating that the essential charsets are available.");
    }
}