package com.itextpdf.text.error_messages;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test class for MessageLocalization.
 */
public class MessageLocalizationTest {

    @Before
    public void setUp() throws Exception {
        // No setup required for this test
    }

    @After
    public void tearDown() throws Exception {
        // No teardown required for this test
    }

    /**
     * Test that getComposedMessage correctly handles backslashes in the path.
     */
    @Test
    public void testBackslashesInPath() throws Exception {
        // Arrange
        String testPath = "C:\\test\\file.txt";
        String expectedKey = "1.not.found.as.file.or.resource";

        // Act
        String result = MessageLocalization.getComposedMessage(expectedKey, testPath);

        // Assert
        assertTrue("Result doesn't contain the test path", result.contains(testPath));
    }
}