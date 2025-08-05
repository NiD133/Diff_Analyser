/*
 *  Copyright 2001-2013 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for {@link PreciseDurationDateTimeField}.
 *
 * This suite verifies the core logic of precise duration fields, such as
 * setting values, rounding, and calculating remainders, using a concrete
 * package-private implementation.
 */
public class PreciseDurationDateTimeFieldTest {

    // A field representing milliseconds of a day. Unit = 1ms, Range = 1 day.
    private PreciseDateTimeField millisOfDayField;

    // A field representing days of a 100-day cycle. Unit = 1 day, Range = 100 days.
    private PreciseDateTimeField dayOfCycleField;

    @Before
    public void setUp() {
        // Arrange: Create common fields for tests using a standard UTC chronology.
        DurationField millisUnit = MillisDurationField.INSTANCE;
        DurationField dayUnit = ISOChronology.getInstanceUTC().days();

        millisOfDayField = new PreciseDateTimeField(
                DateTimeFieldType.millisOfDay(),
                millisUnit,
                dayUnit
        );

        DurationField hundredDayRange = new PreciseDurationField(
                DurationFieldType.days(),
                100L * 24 * 60 * 60 * 1000
        );
        dayOfCycleField = new PreciseDateTimeField(
                DateTimeFieldType.dayOfMonth(), // Type is illustrative
                dayUnit,
                hundredDayRange
        );
    }

    // --- Constructor Tests ---

    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldThrowException_whenUnitFieldIsImprecise() {
        // Arrange: An imprecise duration field (e.g., years)
        DurationField impreciseField = ISOChronology.getInstanceUTC().years();

        // Act & Assert: Attempting to create a field with an imprecise unit should fail.
        new PreciseDateTimeField(DateTimeFieldType.year(), impreciseField, impreciseField);
    }

    @Test
    public void constructor_shouldThrowException_whenUnitMillisIsLessThanOne() {
        // Arrange: A duration field with zero unit milliseconds
        DurationField zeroMillisUnit = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("UnsupportedDurationField should have 0 unit millis", 0L, zeroMillisUnit.getUnitMillis());

        // Act & Assert
        try {
            new PreciseDateTimeField(DateTimeFieldType.millisOfSecond(), zeroMillisUnit, zeroMillisUnit);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The unit milliseconds must be at least 1", e.getMessage());
        }
    }

    // --- Property Getter Tests ---

    @Test
    public void isLenient_shouldAlwaysReturnFalse() {
        assertFalse("PreciseDurationDateTimeField should not be lenient by default", millisOfDayField.isLenient());
    }

    @Test
    public void getUnitMillis_shouldReturnUnitDuration() {
        assertEquals(1L, millisOfDayField.getUnitMillis());
        assertEquals(24L * 60 * 60 * 1000, dayOfCycleField.getUnitMillis());
    }

    @Test
    public void getDurationField_shouldReturnThePreciseUnitDurationField() {
        // Act
        DurationField durationField = dayOfCycleField.getDurationField();

        // Assert
        assertTrue("Duration field must be precise", durationField.isPrecise());
        assertEquals(dayOfCycleField.getUnitMillis(), durationField.getUnitMillis());
    }

    @Test
    public void getMinimumValue_shouldAlwaysReturnZero() {
        assertEquals(0, millisOfDayField.getMinimumValue());
    }

    @Test
    public void getMaximumValue_shouldReturnRangeMinusOne() {
        // The maximum value is the number of units in the range, minus one.
        // For millisOfDay, range is 86,400,000 units (millis). Max value is 86,399,999.
        assertEquals(86400000 - 1, millisOfDayField.getMaximumValue());

        // For dayOfCycle, range is 100 units (days). Max value is 99.
        assertEquals(100 - 1, dayOfCycleField.getMaximumValue());
    }

    // --- Set Method Tests ---

    @Test
    public void set_shouldReplaceTheFieldValueInTheInstant() {
        // Set to a new value: original instant is 2979ms, set value to 1000. New instant should be 1000ms.
        assertEquals(1000L, millisOfDayField.set(2979L, 1000));

        // Set to zero: original instant is 2979ms, set value to 0. New instant should be 0ms.
        assertEquals(0L, millisOfDayField.set(2979L, 0));

        // Instant is a multiple of the range: 31536000000 is a multiple of 1 day.
        // The field value is 0. Setting it to 86399850 should add that many millis.
        assertEquals(31536000000L + 86399850L, millisOfDayField.set(31536000000L, 86399850));
    }

    @Test
    public void set_shouldWorkCorrectlyForNegativeInstants() {
        // This test verifies the complex logic for negative instants.
        // For the millisOfDay field, set(-3012L, 82) should result in -86399918L.
        long instant = -3012L;
        int value = 82;
        long expected = -86399918L;

        // Act
        long result = millisOfDayField.set(instant, value);

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_shouldThrowException_whenValueIsOutOfRange() {
        // Arrange: The max value for dayOfCycleField is 99.
        int invalidValue = 100;

        // Act & Assert: Attempting to set a value equal to the range should fail.
        dayOfCycleField.set(12345L, invalidValue);
    }

    // --- Rounding and Remainder Tests ---

    @Test
    public void roundFloor_shouldRoundDownToNearestUnit() {
        long unitMillis = dayOfCycleField.getUnitMillis();
        long instant = 3 * unitMillis + 500; // 3 days and 500ms
        long expected = 3 * unitMillis;

        assertEquals(expected, dayOfCycleField.roundFloor(instant));
    }

    @Test
    public void roundCeiling_shouldRoundUpToNextUnit() {
        long unitMillis = dayOfCycleField.getUnitMillis();
        long instant = 3 * unitMillis + 500; // 3 days and 500ms
        long expected = 4 * unitMillis;

        assertEquals(expected, dayOfCycleField.roundCeiling(instant));
    }

    @Test
    public void roundCeiling_shouldRoundNegativeValueTowardsZero() {
        long instant = -500L; // -500ms
        long expected = 0L; // The next highest multiple of a day is 0.

        assertEquals(expected, dayOfCycleField.roundCeiling(instant));
    }

    @Test
    public void remainder_shouldReturnTheModulusOfTheUnit() {
        long unitMillis = dayOfCycleField.getUnitMillis();
        long instant = 3 * unitMillis + 500;
        long expected = 500L;

        assertEquals(expected, dayOfCycleField.remainder(instant));
    }

    @Test
    public void remainder_shouldHandleNegativeInstants() {
        // Remainder is defined as instant - roundFloor(instant).
        // For -5225ms with a day unit:
        // roundFloor(-5225) = -86400000ms (i.e., -1 day)
        // remainder = -5225 - (-86400000) = 86394775
        long instant = -5225L;
        long expected = 86394775L;

        assertEquals(expected, dayOfCycleField.remainder(instant));
    }

    // --- Tests for 1-Millisecond Unit ---

    @Test
    public void rounding_shouldBeNoOp_whenUnitIsOneMillisecond() {
        // When the unit is 1ms, rounding should not change the value.
        assertEquals(12345L, millisOfDayField.roundFloor(12345L));
        assertEquals(12345L, millisOfDayField.roundCeiling(12345L));
        assertEquals(-12345L, millisOfDayField.roundFloor(-12345L));
        assertEquals(-12345L, millisOfDayField.roundCeiling(-12345L));
        assertEquals(0L, millisOfDayField.roundFloor(0L));
        assertEquals(0L, millisOfDayField.roundCeiling(0L));
    }

    @Test
    public void remainder_shouldBeZero_whenUnitIsOneMillisecond() {
        // When the unit is 1ms, the remainder is always 0.
        assertEquals(0L, millisOfDayField.remainder(12345L));
        assertEquals(0L, millisOfDayField.remainder(-12345L));
    }
}