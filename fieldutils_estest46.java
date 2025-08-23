package org.joda.time.field;

import org.junit.Test;

/**
 * Contains tests for the {@link FieldUtils} class, focusing on exception-handling scenarios.
 */
// Note: The original class name and inheritance from EvoSuite scaffolding
// are preserved for context. Ideally, this would be a standalone class named `FieldUtilsTest`.
public class FieldUtils_ESTestTest46 extends FieldUtils_ESTest_scaffolding {

    /**
     * Verifies that getWrappedValue(int, int, int) throws an IllegalArgumentException
     * when the minimum and maximum range boundaries are equal. The method's contract
     * requires that the maximum value must be strictly greater than the minimum value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getWrappedValueShouldThrowExceptionWhenMinEqualsMax() {
        // Arrange: Define a range where the minimum and maximum values are the same.
        // The 'value' parameter can be any integer for this test case.
        final int anyValue = 10;
        final int boundary = 5;

        // Act & Assert: This call is expected to throw an IllegalArgumentException
        // because minValue (5) is not strictly less than maxValue (5).
        FieldUtils.getWrappedValue(anyValue, boundary, boundary);
    }
}