/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for DefaultSplitCharacter to verify hyphen splitting behavior.
 * The class should split on hyphens in regular text but preserve date formats.
 */
public class DefaultSplitCharacterTest {
    
    // Test data with various hyphen scenarios
    private static final String TEXT_WITH_HYPHENS_AND_DATE = "tha111-is one that should-be-splitted-right-herel-2018-12-18";
    private static final String TEXT_WITH_DATE_FORMAT = "anddate format2 01-01-1920";
    
    // Character positions for testing (0-based indexing)
    private static final int HYPHEN_IN_DATE_POSITION = 21; // Position of first hyphen in "01-01-1920"
    private static final int HYPHEN_BEFORE_DATE_POSITION = 49; // Position of hyphen before "2018-12-18"
    private static final int HYPHEN_IN_REGULAR_TEXT_POSITION = 6; // Position of hyphen in "tha111-is"
    
    @Test
    public void shouldNotSplitOnHyphenInDateFormat() {
        // Given: A text containing a date in format "01-01-1920"
        // When: Checking if hyphen within the date should be a split character
        // Then: It should NOT be splittable to preserve date integrity
        boolean canSplit = isSplitCharacter(HYPHEN_IN_DATE_POSITION, TEXT_WITH_DATE_FORMAT);
        
        Assert.assertFalse("Hyphen within date format should not be splittable", canSplit);
    }

    @Test
    public void shouldNotSplitOnHyphenInsideDate() {
        // Given: A text containing a date format
        // When: Checking if hyphen inside the date should be a split character  
        // Then: It should NOT be splittable to keep date format intact
        boolean canSplit = isSplitCharacter(HYPHEN_IN_DATE_POSITION, TEXT_WITH_DATE_FORMAT);
        
        Assert.assertFalse("Hyphen inside date should not be splittable", canSplit);
    }

    @Test
    public void shouldSplitOnHyphenBeforeDate() {
        // Given: A text with hyphen immediately before a date (e.g., "text-2019-01-01")
        // When: Checking if hyphen before the date should be a split character
        // Then: It should be splittable as it separates text from date
        boolean canSplit = isSplitCharacter(HYPHEN_BEFORE_DATE_POSITION, TEXT_WITH_HYPHENS_AND_DATE);
        
        Assert.assertTrue("Hyphen before date should be splittable", canSplit);
    }

    @Test
    public void shouldSplitOnHyphenInRegularText() {
        // Given: A text with hyphens in regular words (e.g., "some-text-here")
        // When: Checking if hyphen in regular text should be a split character
        // Then: It should be splittable for normal text wrapping
        boolean canSplit = isSplitCharacter(HYPHEN_IN_REGULAR_TEXT_POSITION, TEXT_WITH_HYPHENS_AND_DATE);
        
        Assert.assertTrue("Hyphen in regular text should be splittable", canSplit);
    }

    /**
     * Helper method to test if a character at a specific position is a split character.
     * 
     * @param characterPosition The position of the character to test (0-based)
     * @param text The text containing the character
     * @return true if the character can be used for splitting, false otherwise
     */
    private boolean isSplitCharacter(int characterPosition, String text) {
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        
        return splitCharacter.isSplitCharacter(
            75,                          // start position (arbitrary for this test)
            characterPosition,           // current position to test
            text.length() + 1,          // end position
            text.toCharArray(),         // character array
            null                        // PdfChunk array (not needed for this test)
        );
    }
}