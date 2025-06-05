package com.itextpdf.text.error_messages;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the MessageLocalization class.
 * This test suite verifies the behavior of message localization, particularly
 * focusing on the handling of file paths with backslashes.
 * 
 * Author: Kevin Day
 */
public class MessageLocalizationTest {

    /**
     * Sets up the test environment before each test method.
     * Currently, no setup is required.
     */
    @Before
    public void setUp() {
        // No setup required for these tests
    }

    /**
     * Cleans up the test environment after each test method.
     * Currently, no teardown is required.
     */
    @After
    public void tearDown() {
        // No teardown required for these tests
    }

    /**
     * Tests that the composed message correctly includes a file path with backslashes.
     * This ensures that the localization system can handle Windows-style file paths.
     */
    @Test
    public void shouldIncludeFilePathInComposedMessage() {
        // Given a file path with backslashes
        String testPath = "C:\\test\\file.txt";

        // When composing a message with the file path
        String result = MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", testPath);

        // Then the result should contain the test path
        assertTrue("The composed message should contain the test path", result.contains(testPath));
    }
}