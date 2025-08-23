package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the constructor throws a NullPointerException when the
     * DateTimeFieldType is null.
     *
     * The constructor {@code IllegalFieldValueException(DateTimeFieldType, Number, String)}
     * is expected to immediately use the provided field type to construct a message
     * (e.g., by calling {@code fieldType.getName()}). If the field type is null, this
     * operation will result in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNumberAndExplanation_shouldThrowNPE_whenDateTimeFieldTypeIsNull() {
        // Arrange: Define arguments for the constructor, with a null DateTimeFieldType.
        Number someValue = 123.45F;
        String someExplanation = "A test explanation";

        // Act: Call the constructor with the null DateTimeFieldType.
        // The @Test(expected=...) annotation will handle the assertion.
        new IllegalFieldValueException((DateTimeFieldType) null, someValue, someExplanation);
    }
}