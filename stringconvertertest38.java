package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}.
 */
class StringConverterSetIntoIntervalTest {

    @Test
    void setInto_withInvalidIntervalString_shouldThrowIllegalArgumentException() {
        // Arrange
        final MutableInterval interval = new MutableInterval(0L, 1000L);
        // The "/" character is the ISO8601 standard separator for intervals,
        // but it is invalid without a start and end instant.
        final String invalidIntervalString = "/";

        // Act & Assert
        // An IllegalArgumentException is expected because the string cannot be parsed.
        assertThrows(IllegalArgumentException.class, () -> {
            StringConverter.INSTANCE.setInto(interval, invalidIntervalString, null);
        });
    }
}