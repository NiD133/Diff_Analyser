package com.itextpdf.text.error_messages;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the MessageLocalization class.
 * This test suite verifies the localization of error messages.
 * 
 * Author: Kevin Day
 */
public class MessageLocalizationTest {

    /**
     * Sets up the test environment before each test.
     * Currently, no setup is required.
     */
    @Before
    public void setUp() {
        // No setup required for these tests
    }

    /**
     * Cleans up the test environment after each test.
     * Currently, no teardown is required.
     */
    @After
    public void tearDown() {
        // No teardown required for these tests
    }

    /**
     * Tests that the getComposedMessage method correctly includes the file path
     * in the resulting message when a file is not found.
     */
    @Test
    public void testBackslashesInFilePath() {
        // Define the test file path with backslashes
        String testFilePath = "C:\\test\\file.txt";

        // Get the composed message using the test file path
        String resultMessage = MessageLocalization.getComposedMessage(
            "1.not.found.as.file.or.resource", testFilePath);

        // Assert that the result message contains the test file path
        assertTrue("The result message should contain the test file path.",
                   resultMessage.contains(testFilePath));
    }
}