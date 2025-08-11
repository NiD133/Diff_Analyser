/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Notice.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for MessageLocalization class functionality.
 * 
 * This test suite verifies that the MessageLocalization class correctly handles
 * message composition with various parameter types, particularly focusing on
 * edge cases like file paths with special characters.
 */
public class MessageLocalizationTest {

    private static final String FILE_NOT_FOUND_MESSAGE_KEY = "1.not.found.as.file.or.resource";
    private static final String WINDOWS_FILE_PATH_WITH_BACKSLASHES = "C:\\test\\file.txt";

    /**
     * Tests that MessageLocalization.getComposedMessage() correctly preserves backslashes
     * in Windows file paths when composing error messages.
     * 
     * This test addresses a potential issue where backslashes in file paths might be
     * interpreted as escape characters during message composition, which would result
     * in malformed file paths in error messages.
     * 
     * Expected behavior: The composed message should contain the original file path
     * with all backslashes intact.
     */
    @Test
    public void testGetComposedMessage_PreservesBackslashesInFilePaths() throws Exception {
        // Act: Compose an error message with a Windows file path containing backslashes
        String composedMessage = MessageLocalization.getComposedMessage(
            FILE_NOT_FOUND_MESSAGE_KEY, 
            WINDOWS_FILE_PATH_WITH_BACKSLASHES
        );
        
        // Assert: Verify the original file path (including backslashes) is preserved in the message
        assertTrue(
            "The composed error message should contain the original file path with backslashes intact. " +
            "Actual message: " + composedMessage, 
            composedMessage.contains(WINDOWS_FILE_PATH_WITH_BACKSLASHES)
        );
    }
}