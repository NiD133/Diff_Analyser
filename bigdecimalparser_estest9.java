package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test class verifies the exception handling of the {@link BigDecimalParser} class,
 * specifically for invalid inputs.
 */
public class BigDecimalParser_ESTestTest9 extends BigDecimalParser_ESTest_scaffolding {

    /**
     * Verifies that calling the {@code parse} method with a null character array
     * throws a {@link NullPointerException}. This is the expected behavior because the
     * underlying JDK methods (like the {@code BigDecimal} constructor) do not
     * handle null array inputs.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullCharArrayShouldThrowNullPointerException() {
        // Call the method under test with a null character array.
        // The offset and length arguments are not relevant when the array is null,
        // so we use 0 as a convention.
        BigDecimalParser.parse(null, 0, 0);
    }
}