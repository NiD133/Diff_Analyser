package com.google.common.base;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Verifies that the {@code to} method throws a NullPointerException when the input string
     * is null, as mandated by its use of {@code Preconditions.checkNotNull}.
     */
    @Test(expected = NullPointerException.class)
    public void to_whenGivenNullString_shouldThrowNullPointerException() {
        // The specific source and target formats do not matter for this test,
        // as the null check on the input string occurs before any conversion logic.
        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, null);
    }
}