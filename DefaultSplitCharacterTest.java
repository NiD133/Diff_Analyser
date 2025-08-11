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
 * Tests the behavior of the {@link DefaultSplitCharacter} class, focusing on its special handling
 * of hyphens, particularly in and around date formats.
 */
public class DefaultSplitCharacterTest {

    /**
     * Tests that a hyphen used as a separator within a standard word or phrase
     * is considered a valid split character.
     */
    @Test
    public void isSplitCharacter_whenHyphenInRegularText_shouldReturnTrue() {
        // Arrange
        String text = "this-is-some-text";
        // We are testing a standard hyphen between words.
        int indexOfHyphen = text.indexOf('-');

        // Act
        boolean isSplit = isSplitCharAtIndex(indexOfHyphen, text);

        // Assert
        Assert.assertTrue("A standard hyphen in text (e.g., 'this-is') should be a split character.", isSplit);
    }

    /**
     * Tests that a hyphen immediately preceding a date string is considered a valid
     * split character, allowing a line break before the date.
     */
    @Test
    public void isSplitCharacter_whenHyphenIsBeforeDate_shouldReturnTrue() {
        // Arrange
        // The original test for this case had a likely off-by-one error in the index,
        // which this refactoring corrects to test the intended character (the hyphen).
        String text = "some-text-2018-12-18";
        // We are testing the hyphen that immediately precedes the date.
        int indexOfHyphen = text.indexOf("-2018");

        // Act
        boolean isSplit = isSplitCharAtIndex(indexOfHyphen, text);

        // Assert
        Assert.assertTrue("A hyphen before a date (e.g., 'text-2018') should be a split character.", isSplit);
    }

    /**
     * Tests that a hyphen used inside a date string (e.g., "2018-12-18") is NOT
     * considered a split character, to prevent breaking dates across lines.
     */
    @Test
    public void isSplitCharacter_whenHyphenIsInsideDate_shouldReturnFalse() {
        // Arrange
        String text = "date is 01-01-1920";
        // We are testing the first hyphen inside the date "01-01-1920".
        int indexOfHyphen = text.indexOf('-');

        // Act
        boolean isSplit = isSplitCharAtIndex(indexOfHyphen, text);

        // Assert
        Assert.assertFalse("A hyphen within a date (e.g., '01-01-1920') should not be a split character.", isSplit);
    }

    /**
     * Helper method to call the DefaultSplitCharacter.isSplitCharacter method.
     * <p>
     * This wrapper isolates the complex and somewhat opaque signature of the method under test.
     * The `start` (75) and `end` (length + 1) parameters are preserved from the original
     * test to maintain its behavior, as their exact purpose is not clear without deeper
     * context of the SUT's typical usage.
     *
     * @param index The index of the character to test in the string.
     * @param text  The string containing the character to test.
     * @return The result of the isSplitCharacter call.
     */
    private boolean isSplitCharAtIndex(int index, String text) {
        final int ARBITRARY_START_PARAM = 75;
        final int ARBITRARY_END_PARAM_OFFSET = 1;

        return new DefaultSplitCharacter().isSplitCharacter(ARBITRARY_START_PARAM, index,
                text.length() + ARBITRARY_END_PARAM_OFFSET, text.toCharArray(), null);
    }
}