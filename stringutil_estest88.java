package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 * This example focuses on the {@code isBlank()} method.
 */
// Note: The original test class 'StringUtil_ESTestTest88' was renamed for clarity.
public class StringUtilTest {

    @Test
    public void isBlankShouldReturnTrueForNullInput() {
        // The StringUtil.isBlank() method is expected to treat a null string as blank.
        assertTrue("A null string should be considered blank", StringUtil.isBlank(null));
    }
}