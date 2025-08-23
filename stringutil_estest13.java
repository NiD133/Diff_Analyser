package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void isInvisibleCharShouldReturnFalseForVisibleCharacter() {
        // Arrange: The character '„' (U+201E, decimal 8222) is a "DOUBLE LOW-9 QUOTATION MARK".
        // This is a standard, visible character and should not be classified as invisible.
        final int visibleQuotationMarkCodePoint = '„';

        // Act
        boolean isConsideredInvisible = StringUtil.isInvisibleChar(visibleQuotationMarkCodePoint);

        // Assert
        assertFalse("The double low-9 quotation mark '„' should be considered a visible character.", isConsideredInvisible);
    }
}