package com.itextpdf.text.error_messages;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests the correct handling of backslashes in localized error messages.
 * Specifically, this test verifies that file paths containing backslashes 
 * are correctly included in the error message without being misinterpreted.
 */
public class MessageLocalizationTest {

    @Before
    public void setUp() throws Exception {
        //  Setup operations before each test (none needed here).
    }

    @After
    public void tearDown() throws Exception {
        // Teardown operations after each test (none needed here).
    }

    @Test
    public void testBackslashesInErrorMessage() throws Exception {
        // Arrange: Define a test file path with backslashes.
        String testPath = "C:\\test\\file.txt";

        // Act: Retrieve the localized error message using the test path.
        String errorMessage = MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", testPath);

        // Assert: Verify that the error message contains the complete test path, 
        // ensuring that backslashes were not removed or misinterpreted.
        assertTrue("The error message should contain the full file path with backslashes.", errorMessage.contains(testPath));
    }
}