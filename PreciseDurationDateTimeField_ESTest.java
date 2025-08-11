package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.TimeZone;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Weeks;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.ZonedChronology;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.PreciseDateTimeField;
import org.joda.time.field.PreciseDurationField;
import org.joda.time.field.ScaledDurationField;
import org.joda.time.field.UnsupportedDurationField;

/**
 * Test suite for PreciseDurationDateTimeField functionality.
 * Tests basic operations like rounding, setting values, and field properties.
 */
public class PreciseDurationDateTimeFieldTest {

    // Test data constants
    private static final long SAMPLE_TIMESTAMP_1 = 2946L;
    private static final long SAMPLE_TIMESTAMP_2 = 1902L;
    private static final long ONE_DAY_MILLIS = 86400000L;
    private static final long LARGE_TIMESTAMP = 992280585600000L;
    private static final int SAMPLE_VALUE = 82;
    private static final int LARGE_VALUE = 86399850;

    // Helper method to create a basic PreciseDateTimeField for testing
    private PreciseDateTimeField createBasicField() {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField unitField = (MillisDurationField) MillisDurationField.INSTANCE;
        JulianChronology chronology = JulianChronology.getInstance();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(chronology, DateTimeZone.UTC);
        DurationField rangeField = zonedChronology.days();
        
        return new PreciseDateTimeField(fieldType, unitField, rangeField);
    }

    // Helper method to create a field with custom duration
    private PreciseDateTimeField createFieldWithCustomDuration() {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        JulianChronology chronology = JulianChronology.getInstance();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(chronology, DateTimeZone.UTC);
        DurationField rangeField = zonedChronology.days();
        
        DurationFieldType durationFieldType = Days.MIN_VALUE.getFieldType();
        PreciseDurationField unitField = new PreciseDurationField(durationFieldType, 21859200000L);
        
        return new PreciseDateTimeField(fieldType, rangeField, unitField);
    }

    @Test
    public void testRemainder_WithPositiveTimestamp_ReturnsCorrectRemainder() {
        // Given: A clockhour field with seconds unit and days range
        DateTimeFieldType fieldType = DateTimeFieldType.clockhourOfHalfday();
        EthiopicChronology chronology = EthiopicChronology.getInstance();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(chronology, DateTimeZone.getDefault());
        DurationField unitField = zonedChronology.seconds();
        DurationField rangeField = zonedChronology.days();
        PreciseDateTimeField field = new PreciseDateTimeField(fieldType, unitField, rangeField);
        
        // When: Getting remainder of timestamp
        long remainder = field.remainder(SAMPLE_TIMESTAMP_1);
        
        // Then: Should return expected remainder
        assertEquals(946L, remainder);
    }

    @Test
    public void testRoundCeiling_WithPositiveTimestamp_RoundsUpCorrectly() {
        // Given: A field with custom duration
        PreciseDateTimeField field = createFieldWithCustomDuration();
        
        // When: Rounding ceiling of timestamp
        long rounded = field.roundCeiling(SAMPLE_TIMESTAMP_2);
        
        // Then: Should round up to next day boundary
        assertEquals(ONE_DAY_MILLIS, rounded);
    }

    @Test
    public void testRoundCeiling_WithZeroTimestamp_ReturnsZero() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Rounding ceiling of zero
        long rounded = field.roundCeiling(0L);
        
        // Then: Should return zero
        assertEquals(0L, rounded);
    }

    @Test
    public void testRoundFloor_WithZeroTimestamp_ReturnsZero() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Rounding floor of zero
        long rounded = field.roundFloor(0L);
        
        // Then: Should return zero
        assertEquals(0L, rounded);
    }

    @Test
    public void testSet_WithZeroValue_ReturnsZero() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Setting value to zero
        long result = field.set(2979L, 0);
        
        // Then: Should return zero
        assertEquals(0L, result);
    }

    @Test
    public void testSet_WithLargePositiveValue_CalculatesCorrectly() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Setting a large positive value
        long result = field.set(31536000000L, LARGE_VALUE);
        
        // Then: Should calculate new timestamp correctly
        assertEquals(31622399850L, result);
    }

    @Test
    public void testSet_WithNegativeTimestamp_HandlesNegativeValues() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Setting value on negative timestamp
        long result = field.set(-3012L, SAMPLE_VALUE);
        
        // Then: Should handle negative values correctly
        assertEquals(-86399918L, result);
    }

    @Test
    public void testRoundFloor_WithLargeTimestamp_PreservesValue() {
        // Given: A day-of-week field with minute unit and halfday range
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        LenientChronology lenientChronology = LenientChronology.getInstance(chronology);
        ZonedChronology zonedChronology = ZonedChronology.getInstance(lenientChronology, DateTimeZone.forID(null));
        DurationField unitField = zonedChronology.minutes();
        DurationField rangeField = zonedChronology.halfdays();
        PreciseDateTimeField field = new PreciseDateTimeField(fieldType, unitField, rangeField);
        
        // When: Rounding floor of large timestamp
        long rounded = field.roundFloor(LARGE_TIMESTAMP);
        
        // Then: Should preserve the value (already aligned)
        assertEquals(LARGE_TIMESTAMP, rounded);
    }

    @Test
    public void testIsLenient_ReturnsFalse() {
        // Given: Any precise date time field
        PreciseDateTimeField field = createBasicField();
        
        // When: Checking if field is lenient
        boolean isLenient = field.isLenient();
        
        // Then: Should return false (precise fields are not lenient)
        assertFalse(isLenient);
    }

    @Test
    public void testGetUnitMillis_ReturnsOne() {
        // Given: A field with millisecond precision
        PreciseDateTimeField field = createBasicField();
        
        // When: Getting unit milliseconds
        long unitMillis = field.getUnitMillis();
        
        // Then: Should return 1 (millisecond precision)
        assertEquals(1L, unitMillis);
    }

    @Test
    public void testGetMinimumValue_ReturnsZero() {
        // Given: A day-of-week field
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        IslamicChronology chronology = IslamicChronology.getInstance();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(chronology, DateTimeZone.forTimeZone(null));
        DurationField unitField = zonedChronology.halfdays();
        DurationField rangeField = zonedChronology.days();
        PreciseDateTimeField field = new PreciseDateTimeField(fieldType, unitField, rangeField);
        
        // When: Getting minimum value
        int minValue = field.getMinimumValue();
        
        // Then: Should return zero
        assertEquals(0, minValue);
    }

    @Test
    public void testGetMaximumValueForSet_ReturnsExpectedMaximum() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Getting maximum value for set operation
        int maxValue = field.getMaximumValueForSet(-3870L, -2146901673);
        
        // Then: Should return expected maximum
        assertEquals(86399999, maxValue);
    }

    @Test
    public void testGetDurationField_ReturnsPreciseField() {
        // Given: A basic field
        PreciseDateTimeField field = createBasicField();
        
        // When: Getting duration field
        DurationField durationField = field.getDurationField();
        
        // Then: Should return a precise duration field
        assertTrue(durationField.isPrecise());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithUnsupportedDurationField_ThrowsException() {
        // Given: An unsupported duration field
        DateTimeFieldType fieldType = DateTimeFieldType.clockhourOfDay();
        DurationFieldType durationFieldType = DurationFieldType.centuries();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(durationFieldType);
        
        // When: Creating field with unsupported duration
        // Then: Should throw IllegalArgumentException
        new PreciseDateTimeField(fieldType, unsupportedField, unsupportedField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithImpreciseDurationField_ThrowsException() {
        // Given: An imprecise duration field (years)
        DateTimeFieldType fieldType = DateTimeFieldType.halfdayOfDay();
        DurationFieldType durationFieldType = DurationFieldType.years();
        DurationField impreciseField = durationFieldType.getField(null);
        
        // When: Creating field with imprecise duration
        // Then: Should throw IllegalArgumentException
        new PreciseDateTimeField(fieldType, impreciseField, impreciseField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_WithValueOutOfRange_ThrowsException() {
        // Given: A minute-of-day field with custom scaling
        DateTimeFieldType fieldType = DateTimeFieldType.minuteOfDay();
        Weeks weeks = Weeks.ONE;
        DurationFieldType durationFieldType = weeks.getFieldType();
        GJChronology chronology = GJChronology.getInstance();
        DurationField durationField = durationFieldType.getField(chronology);
        ScaledDurationField scaledField = new ScaledDurationField(durationField, durationFieldType, 1058);
        PreciseDateTimeField field = new PreciseDateTimeField(fieldType, durationField, scaledField);
        
        // When: Setting value outside valid range
        // Then: Should throw IllegalArgumentException
        field.set(1058L, 1058); // Value 1058 is out of range [0,1057]
    }
}