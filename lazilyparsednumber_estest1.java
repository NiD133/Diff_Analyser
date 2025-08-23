package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * Verifies that hashCode() does not attempt to parse the underlying string value.
     * Instead, it should simply delegate to the String's hashCode() method.
     * This is tested with a string that is not a valid number to ensure
     * no NumberFormatException is thrown and the behavior is correct.
     */
    @Test
    public void hashCode_shouldDelegateToStringHashCode() {
        String nonNumericValue = "this-is-not-a-number";
        LazilyParsedNumber lazyNumber = new LazilyParsedNumber(nonNumericValue);

        // The hashCode of the LazilyParsedNumber should be identical to the
        // hashCode of the underlying string it was constructed with.
        assertEquals(nonNumericValue.hashCode(), lazyNumber.hashCode());
    }
}