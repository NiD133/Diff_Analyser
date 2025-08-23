package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() can correctly parse a locale string
     * where the language and country codes are separated by a dash.
     */
    @Test
    public void toLocale_shouldParseLocaleStringWithDashSeparator() {
        // Arrange
        final String localeString = "zh-CN";
        final Locale expectedLocale = new Locale("zh", "CN");

        // Act
        final Locale actualLocale = LocaleUtils.toLocale(localeString);

        // Assert
        assertNotNull("The resulting locale should not be null", actualLocale);
        assertEquals("The parsed locale should match the expected locale", expectedLocale, actualLocale);
        
        // Also verify the standard string representation, which uses an underscore
        assertEquals("zh_CN", actualLocale.toString());
    }
}