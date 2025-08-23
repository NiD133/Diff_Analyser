package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

/**
 * Provides unit tests for the {@link PreciseDurationDateTimeField} class.
 * <p>
 * Since PreciseDurationDateTimeField is abstract, its behavior is tested
 * through a concrete implementation, such as the millisOfSecond field
 * from a standard chronology.
 */
public class PreciseDurationDateTimeFieldTest {

    @Test
    public void getUnitMillis_forMillisOfSecondField_returnsOne() {
        // Arrange: Obtain a concrete instance of PreciseDurationDateTimeField.
        // The millisOfSecond field from GJChronology serves as a representative example.
        DateTimeField field = GJChronology.getInstanceUTC().millisOfSecond();
        PreciseDurationDateTimeField preciseField = (PreciseDurationDateTimeField) field;

        // Act: Call the method under test.
        long unitMillis = preciseField.getUnitMillis();

        // Assert: The unit for the millisOfSecond field must be 1 millisecond.
        assertEquals("The unit millis for millisOfSecond should be 1", 1L, unitMillis);
    }
}