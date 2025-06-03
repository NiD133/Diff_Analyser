package com.itextpdf.text.error_messages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Import static assertion methods for cleaner code

class BackslashPathTest { // Renamed the class for better clarity and meaning

    private String testPath; // Declared as instance variable for readability
    private String expectedMessage; // Define expected message part for the purpose of clarifying assertion

    @BeforeEach // Changed to JUnit 5 annotation
    void setUp() {
        testPath = "C:\\test\\file.txt"; // Clear variable name.
        expectedMessage = "1.not.found.as.file.or.resource";
    }

    @AfterEach // Changed to JUnit 5 annotation
    void tearDown() {
        testPath = null; // Clean up after the test
    }

    @Test
    void testErrorMessageContainsBackslashPath() { // More descriptive test name
        // Arrange (Setup):  The test path is already set up in the @BeforeEach method.

        // Act: Get the error message using MessageLocalization.
        String errorMessage = MessageLocalization.getComposedMessage(expectedMessage, testPath);

        // Assert:  Verify that the error message contains the original file path.
        assertNotNull(errorMessage, "Error message should not be null.");
        assertTrue(errorMessage.contains(testPath), "Error message should contain the test path: " + testPath);
    }
}