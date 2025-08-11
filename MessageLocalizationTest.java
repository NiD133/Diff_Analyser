package com.itextpdf.text.error_messages;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Verifies that MessageLocalization.getComposedMessage preserves backslashes in
 * parameter values (e.g., Windows file paths) when inserting them into a message.
 *
 * Assumptions:
 * - The default (English) bundle contains the key "1.not.found.as.file.or.resource"
 *   with a single {1} placeholder where the path will be inserted.
 */
public class MessageLocalizationTest {

    private static final String KEY_FILE_NOT_FOUND = "1.not.found.as.file.or.resource";
    private static final String WINDOWS_PATH = "C:\\test\\file.txt";

    @Test
    public void preservesBackslashesInParameterValues() {
        String localizedMessage = MessageLocalization.getComposedMessage(KEY_FILE_NOT_FOUND, WINDOWS_PATH);

        assertTrue(
            "The localized message should contain the original path, including backslashes: " + WINDOWS_PATH,
            localizedMessage.contains(WINDOWS_PATH)
        );
    }
}