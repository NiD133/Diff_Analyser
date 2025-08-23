package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on its core contract.
 */
public class AbstractPartialTest {

    /**
     * Verifies that isSupported() returns false when the field type is null,
     * as a null type can never be supported.
     */
    @Test
    public void isSupportedShouldReturnFalseForNullDateTimeFieldType() {
        // Arrange: Create an instance of a concrete subclass of AbstractPartial.
        // YearMonth is a simple and suitable choice for this test.
        YearMonth partial = YearMonth.now();

        // Act & Assert: Directly assert the expected outcome of calling the method under test.
        assertFalse("isSupported(null) should return false.", partial.isSupported(null));
    }
}