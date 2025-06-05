package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link MessageLocalization} class.  Specifically, tests that
 * the message localization mechanism correctly handles backslashes in message parameters.
 */
public class MessageLocalizationTest {

    /**
     * Tests that backslashes in the parameter passed to {@link MessageLocalization#getComposedMessage(String, Object...)}
     * are correctly included in the resulting message.  This is important because file paths often contain backslashes.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testBackslashesInMessageParameter() throws Exception {
        // Arrange: Define a test file path that includes backslashes.
        String testPath = "C:\\test\\file.txt";

        // Act: Get a localized message using the test path as a parameter.
        // The key "1.not.found.as.file.or.resource" is assumed to exist in the
        // localization files and to include "{1}" as a placeholder for the path.
        String result = MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", testPath);

        // Assert: Verify that the resulting message contains the test path,
        // indicating that the backslashes were correctly handled.
        assertTrue("Result doesn't contain the test path", result.contains(testPath));
    }
}