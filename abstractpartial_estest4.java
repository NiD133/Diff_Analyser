package org.joda.time.base;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This test class focuses on the behavior of methods inherited from {@link AbstractPartial}.
 */
public class AbstractPartial_ESTestTest4 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false when comparing two different types of partials,
     * even if one contains a subset of the other's data.
     *
     * A LocalDateTime (containing date and time fields) should not be considered equal to a
     * LocalDate (containing only date fields), as the `AbstractPartial.equals()` contract
     * requires the field types to be identical.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingPartialsWithDifferentFieldTypes() {
        // Arrange: Create a full date-time object and a date-only object from it.
        LocalDateTime dateTimeWithTime = new LocalDateTime();
        LocalDate dateOnly = dateTimeWithTime.toLocalDate();

        // Act & Assert: Verify that they are not equal.
        // The equals method, inherited from AbstractPartial, checks that both objects
        // have the same class, chronology, and set of field types before comparing values.
        // Since LocalDateTime and LocalDate have different field types, this must be false.
        assertFalse("A LocalDateTime should not be equal to a LocalDate", dateTimeWithTime.equals(dateOnly));
    }
}