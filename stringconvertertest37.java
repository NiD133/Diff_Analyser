package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.joda.time.ReadWritableInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}.
 */
class StringConverterSetIntoIntervalTest {

    private final StringConverter converter = StringConverter.INSTANCE;

    /**
     * Tests that calling setInto() with an empty string fails, as it's not a valid
     * representation of an interval.
     */
    @Test
    void setInto_withEmptyString_throwsIllegalArgumentException() {
        // Arrange
        MutableInterval interval = new MutableInterval(0L, 1000L);
        String invalidInput = "";

        // Act & Assert
        // An empty string is not a valid format for an interval.
        assertThrows(IllegalArgumentException.class, () -> {
            converter.setInto(interval, invalidInput, null);
        });
    }
}