package com.google.common.base;

import org.junit.Test;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Verifies that normalizeFirstWord throws a NullPointerException when the input string is null.
     * This is the expected behavior as per Guava's Preconditions.checkNotNull contract.
     */
    @Test(expected = NullPointerException.class)
    public void normalizeFirstWord_whenInputIsNull_throwsNullPointerException() {
        // Arrange: Select any CaseFormat instance, as the null-check behavior is common.
        CaseFormat format = CaseFormat.LOWER_CAMEL;

        // Act & Assert: Call the method with a null argument.
        // The test will pass only if a NullPointerException is thrown,
        // as specified by the `expected` attribute in the @Test annotation.
        format.normalizeFirstWord(null);
    }
}