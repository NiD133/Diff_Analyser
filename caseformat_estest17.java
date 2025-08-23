package com.google.common.base;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Verifies that converting a null string throws a NullPointerException,
     * as per the contract of the `to` method.
     */
    @Test(expected = NullPointerException.class)
    public void to_whenGivenNullString_throwsNullPointerException() {
        // The specific source and target formats (e.g., UPPER_UNDERSCORE to LOWER_CAMEL)
        // are arbitrary here, as the null check on the input string should
        // occur before any conversion logic.
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, null);
    }
}