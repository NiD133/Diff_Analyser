package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable tests for IllegalFieldValueException.
 *
 * These tests focus on:
 * - Clear constructor behavior for DateTimeFieldType and DurationFieldType
 * - Getter behavior for number and string values
 * - Message formatting for supported scenarios
 * - Null-handling (where NullPointerException is expected vs allowed)
 * - prependMessage behavior
 */
public class IllegalFieldValueExceptionTest {

    // --- Constructors using DateTimeFieldType with numeric bounds ---

    @Test
    public void numberConstructor_withDateTimeFieldType_populatesFieldsAndRangeMessage() {
        DateTimeFieldType field = DateTimeFieldType.year();

        IllegalFieldValueException ex =
            new IllegalFieldValueException(field, 2025, 2000, 2030);

        assertSame(field, ex.getDateTimeFieldType());
        assertNull(ex.getDurationFieldType());
        assertEquals("year", ex.getFieldName());
        assertEquals(2025, ex.getIllegalNumberValue());
        assertNull(ex.getIllegalStringValue());
        assertEquals(2000, ex.getLowerBound());
        assertEquals(2030, ex.getUpperBound());
        assertEquals("Value 2025 for year must be in the range [2000,2030]", ex.getMessage());
    }

    // --- Constructors using DateTimeFieldType with string value ---

    @Test
    public void stringConstructor_withDateTimeFieldType_populatesFieldsAndMessage() {
        DateTimeFieldType field = DateTimeFieldType.secondOfMinute();

        IllegalFieldValueException ex =
            new IllegalFieldValueException(field, "");

        assertSame(field, ex.getDateTimeFieldType());
        assertNull(ex.getDurationFieldType());
        assertEquals("secondOfMinute", ex.getFieldName());
        assertNull(ex.getIllegalNumberValue());
        assertEquals("", ex.getIllegalStringValue());
        assertEquals("", ex.getIllegalValueAsString());
        assertNull(ex.getLowerBound());
        assertNull(ex.getUpperBound());
        assertEquals("Value \"\" for secondOfMinute is not supported", ex.getMessage());
    }

    // --- Constructors using DurationFieldType with string value ---

    @Test
    public void stringConstructor_withDurationFieldType_populatesFieldsAndMessage() {
        DurationFieldType field = DurationFieldType.eras();

        IllegalFieldValueException ex =
            new IllegalFieldValueException(field, "");

        assertNull(ex.getDateTimeFieldType());
        assertSame(field, ex.getDurationFieldType());
        assertEquals("eras", ex.getFieldName());
        assertNull(ex.getIllegalNumberValue());
        assertEquals("", ex.getIllegalStringValue());
        assertEquals("", ex.getIllegalValueAsString());
        assertNull(ex.getLowerBound());
        assertNull(ex.getUpperBound());
        assertEquals("Value \"\" for eras is not supported", ex.getMessage());
    }

    // --- Null-handling for field types (expected NullPointerException) ---

    @Test(expected = NullPointerException.class)
    public void constructor_withNullDateTimeFieldType_inStringCtor_throwsNPE() {
        new IllegalFieldValueException((DateTimeFieldType) null, "x");
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullDurationFieldType_inStringCtor_throwsNPE() {
        new IllegalFieldValueException((DurationFieldType) null, "");
    }

    @Test(expected = NullPointerException.class)
    public void numberConstructor_withNullDateTimeFieldType_throwsNPE() {
        new IllegalFieldValueException((DateTimeFieldType) null, 1, 0, 2);
    }

    @Test(expected = NullPointerException.class)
    public void numberConstructor_withNullDurationFieldType_throwsNPE() {
        new IllegalFieldValueException((DurationFieldType) null, 1, 0, 2);
    }

    // --- Field-name based constructor allows nulls and formats message ---

    @Test
    public void constructor_withNullFieldNameAndNullValue_formatsMessage() {
        IllegalFieldValueException ex =
            new IllegalFieldValueException((String) null, (Number) null, null, null);

        assertNull(ex.getDateTimeFieldType());
        assertNull(ex.getDurationFieldType());
        assertNull(ex.getFieldName());
        assertNull(ex.getIllegalNumberValue());
        assertNull(ex.getIllegalStringValue());
        assertEquals("null", ex.getIllegalValueAsString());
        assertNull(ex.getLowerBound());
        assertNull(ex.getUpperBound());
        assertEquals("Value null for null is not supported", ex.getMessage());
    }

    // --- prependMessage behavior ---

    @Test
    public void prependMessage_withEmptyString_prefixesColonOnly() {
        IllegalFieldValueException ex =
            new IllegalFieldValueException("", (Number) null, null, null);

        ex.prependMessage("");

        // Formats as ": <original message>"
        assertTrue(ex.getMessage().startsWith(": "));
        assertTrue(ex.getMessage().endsWith("is not supported"));
    }

    @Test
    public void prependMessage_withNull_doesNothing() {
        IllegalFieldValueException ex =
            new IllegalFieldValueException("field", 5, 1, 10);

        String original = ex.getMessage();
        ex.prependMessage(null);

        assertEquals(original, ex.getMessage());
    }

    // --- Constructor that includes an explanatory message ---

    @Test
    public void numberConstructor_withExplain_includesExplanationInMessage() {
        DateTimeFieldType field = DateTimeFieldType.hourOfDay();

        IllegalFieldValueException ex =
            new IllegalFieldValueException(field, null, null, null, "because this is invalid here");

        assertTrue(ex.getMessage().contains("because this is invalid here"));
    }

    // --- Getters return nulls when not applicable ---

    @Test
    public void getters_returnNulls_whenBoundsNotProvided() {
        IllegalFieldValueException ex =
            new IllegalFieldValueException(DateTimeFieldType.minuteOfHour(), "xx");

        assertNull(ex.getLowerBound());
        assertNull(ex.getUpperBound());
        assertNull(ex.getIllegalNumberValue());
        assertNotNull(ex.getIllegalStringValue()); // "xx"
        assertEquals("xx", ex.getIllegalValueAsString());
    }
}