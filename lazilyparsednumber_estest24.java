package com.google.gson.internal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    @Test
    public void longValue_whenConstructedWithNull_throwsNullPointerException() {
        // Arrange: Create a LazilyParsedNumber with a null string value.
        // While the constructor's contract states the value must not be null,
        // this test verifies the defensive behavior of the class.
        LazilyParsedNumber number = new LazilyParsedNumber(null);

        // Act & Assert: Verify that attempting to parse the null value as a long
        // results in a NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            number.longValue();
        });
    }
}