package com.google.common.base;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test(expected = NullPointerException.class)
    public void to_whenInputStringIsNull_throwsNullPointerException() {
        // The specific source and target formats do not matter for this test,
        // as the null check on the input string happens before any conversion logic.
        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, null);
    }
}