package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalField;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#range(TemporalField)} method.
 */
class DayOfYearTest {

    /**
     * The Javadoc for the range() method implies its 'field' parameter is non-null.
     * This test verifies that passing a null argument correctly throws a NullPointerException.
     */
    @Test
    void rangeWithNullFieldThrowsException() {
        // Arrange: Create a sample DayOfYear instance. The specific value (12) is arbitrary.
        DayOfYear dayOfYear = DayOfYear.of(12);

        // Act & Assert: Verify that calling range() with a null field throws the expected exception.
        // The cast to (TemporalField) is necessary to resolve method overloading ambiguity for the compiler.
        assertThrows(NullPointerException.class, () -> dayOfYear.range((TemporalField) null));
    }
}